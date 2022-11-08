package com.epam.training.webshop.core.product;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.product.persistence.entity.Product;
import com.epam.training.webshop.core.product.persistence.repository.ProductRepository;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProductServiceImplTest {

    private static final Product ENTITY = new Product("Hypo", 550.0, "HUF");
    private static final ProductDto DTO = new ProductDto.Builder()
        .withName("Hypo")
        .withNetPrice(new Money(550.0, Currency.getInstance("HUF")))
        .build();

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final ProductServiceImpl underTest = new ProductServiceImpl(productRepository);


    @Test
    void testGetProductListShouldReturnAStaticListWithTwoElements() {
        // Given
        Mockito.when(productRepository.findAll()).thenReturn(List.of(ENTITY));
        List<ProductDto> expected = List.of(DTO);

        // Mockito.when
        List<ProductDto> actual = underTest.getProductList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findAll();
    }

    @Test
    void testGetProductByNameShouldReturnHypoWhenInputProductNameIsHypo() {
        // Given
        Mockito.when(productRepository.findByName("Hypo")).thenReturn(Optional.of(ENTITY));
        Optional<ProductDto> expected = Optional.of(DTO);

        // Mockito.when
        Optional<ProductDto> actual = underTest.getProductByName("Hypo");

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findByName("Hypo");
    }

    @Test
    void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameDoesNotExist() {
        // Given
        Mockito.when(productRepository.findByName("dummy")).thenReturn(Optional.empty());
        Optional<ProductDto> expected = Optional.empty();

        // Mockito.when
        Optional<ProductDto> actual = underTest.getProductByName("dummy");

        // Then
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findByName("dummy");
    }

    @Test
    void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameIsNull() {
        // Given
        Mockito.when(productRepository.findByName(null)).thenReturn(Optional.empty());
        Optional<ProductDto> expected = Optional.empty();

        // Mockito.when
        Optional<ProductDto> actual = underTest.getProductByName(null);

        // Then
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
        Mockito.verify(productRepository).findByName(null);
    }

    @Test
    void testCreateProductShouldStoreTheGivenProductWhenTheInputProductIsValid() {
        // Given
        Mockito.when(productRepository.save(ENTITY)).thenReturn(ENTITY);

        // Mockito.when
        underTest.createProduct(DTO);

        // Then
        Mockito.verify(productRepository).save(ENTITY);
    }
}
