// AuthenticationControllerTest.java
package com.effectivemobile.authservice.controller;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.config.security.SecurityConfiguration;
import com.effectivemobile.authservice.entity.CustomUser;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.service.AuthenticationService;
import com.effectivemobile.authservice.service.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class) // Явно импортируем конфигурацию безопасности
class AuthenticationControllerTest extends AbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private JwtService jwtService; // Добавляем мок JwtService

    @MockitoBean(name = "myUserDetailService")
    private UserDetailsService userDetailsService; // Мок для UserDetailsService

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signIn_ShouldReturnPendingMessage() throws Exception {
        CustomUser user = new CustomUser();
        user.setEmail("test@example.com");

        Mockito.doNothing().when(authenticationService).signIn(any());

        mockMvc.perform(post("/api/api/otp-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(
                        "An email with an activation code will be sent to your email within 15 minutes."));
    }

    @Test
    void getBarrierToken_ValidToken_ShouldReturnJwt() throws Exception {
        OneTimeTokenDto token = OneTimeTokenDto.builder()
                .userToken(UUID.randomUUID().toString())
                .build();
        String expectedJwt = "test.jwt.token";

        Mockito.when(authenticationService.getBarrierToken(any()))
                .thenReturn(Optional.of(expectedJwt));

        mockMvc.perform(post("/api/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(token)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJwt));
    }

    @Test
    @WithMockUser
    void barrierToken_WithValidJwt_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/api/api/security")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"test\""))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    @WithAnonymousUser
    void barrierToken_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/api/security")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"test\""))
                .andExpect(status().isForbidden());
    }

    @Test
    void signIn_InvalidInput_ShouldReturnBadRequest() throws Exception {
        CustomUser invalidUser = new CustomUser();
        invalidUser.setEmail("not-an-email");

        mockMvc.perform(post("/api/api/otp-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}