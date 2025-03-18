package com.effectivemobile.authservice.config;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
@Import(DefaultClassesConfig.class)
class DefaultClassesConfigTest extends AbstractContainerTest {

    @Autowired
    private CompletableFuture<OneTimeTokenDto> oneTimeTokenFuture;

    @Test
    void oneTimeTokenFutureShouldBeCreated() {
        Assertions.assertNotNull(oneTimeTokenFuture, "CompletableFuture должен быть создан");
        Assertions.assertTrue(oneTimeTokenFuture instanceof CompletableFuture, "Объект должен быть CompletableFuture");
    }
}
