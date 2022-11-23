package com.epam.training.webshop.core;

import com.epam.training.webshop.core.product.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.result.DefaultResultHandler;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
class ApplicationIT {

    @Autowired
    private Shell shell;

    @Autowired
    private DefaultResultHandler defaultResultHandler;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testCreateNewProductShouldPersistTheProductToTheDatabaseWhenAdminIsLoggedIn() {
        // Given
        defaultResultHandler.handleResult(shell.evaluate(() -> "user login admin admin"));

        // When
        defaultResultHandler.handleResult(shell.evaluate(() -> "admin product create Tej 300 HUF"));

        // Then
        Assertions.assertEquals(2, productRepository.findAll().size());
    }
}
