package com.effectivemobile.authservice.config;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Configuration
public class DefaultClassesConfig {

    @Bean
    public CompletableFuture<OneTimeTokenDto> oneTimeTokenFuture() {
        return new CompletableFuture<>();
    }

}
