package com.epam.training.webshop.core.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.Product;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductServiceImplTest {

    private ProductServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductServiceImpl();
        underTest.initProducts();
    }

    @Test
    void testGetProductByNameShouldReturnHypoWhenInputProductNameIsHypo() {
        // Given
        Product expected = new Product("Hypo", new Money(300, Currency.getInstance("HUF")));

        // When
        Optional<Product> actual = underTest.getProductByName("Hypo");

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameDoesNotExist() {
        // Given
        Optional<Product> expected = Optional.empty();

        // When
        Optional<Product> actual = underTest.getProductByName("Liszt");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameIsNull() {
        // Given
        Optional<Product> expected = Optional.empty();

        // When
        Optional<Product> actual = underTest.getProductByName(null);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void testCreateProductShouldStoreTheGivenProductWhenTheInputProductIsValid() {
        // Given
        Product expected = new Product.Builder()
            .withName("Retek")
            .withNetPrice(new Money(230.0, Currency.getInstance("HUF")))
            .build();

        // When
        underTest.createProduct(expected);

        // Then
        Product actual = underTest.getProductByName("Retek").get();
        assertEquals(expected, actual);
    }
}
