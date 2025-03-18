package com.effectivemobile.authservice.config.security;

import com.effectivemobile.authservice.AbstractContainerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootTest
class SecurityConfigurationTest extends AbstractContainerTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Test
    void securityFilterChainShouldBeConfigured() throws Exception {
        Assertions.assertNotNull(securityFilterChain);
        // Можно добавить дополнительные проверки конфигурации безопасности
    }

    @Test
    void passwordEncoderShouldBeBCrypt() {
        Assertions.assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void authenticationManagerShouldBeAvailable() {
        Assertions.assertNotNull(securityConfiguration.authenticationManager());
    }
}
