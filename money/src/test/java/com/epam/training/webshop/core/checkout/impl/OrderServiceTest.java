package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.checkout.persistence.model.Order;
import com.epam.training.webshop.core.checkout.persistence.model.OrderItem;
import com.epam.training.webshop.core.checkout.persistence.repository.OrderRepository;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import com.epam.training.webshop.core.user.persistence.repository.UserRepository;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Currency CURRENCY = Currency.getInstance("HUF");
    private static final String PRODUCT_NAME = "TV";
    private static final double NET_PRICE_AMOUNT = 100;
    private static final double GROSS_PRICE_AMOUNT = 200;
    private static final User USER = new User("username", "password", User.Role.USER);
    private static final UserDto USER_DTO = new UserDto("username", User.Role.USER);

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService underTest;

    @Test
    void testHandleOrderShouldThrowIllegalArgumentExceptionWhenUserIsNotLoggedIn() {
        // Given
        Mockito.when(userService.describe()).thenReturn(Optional.empty());

        // When - Then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.handleOrder(createOrderDto()));
        Assertions.assertEquals("You need to first login!", exception.getMessage());
        Mockito.verify(userService).describe();
        Mockito.verifyNoInteractions(userRepository, orderRepository);
    }

    @Test
    void testHandleOrderShouldThrowIllegalArgumentExceptionWhenUserDoesNotExist() {
        // Given
        Mockito.when(userService.describe()).thenReturn(Optional.of(USER_DTO));
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        // When - Then
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.handleOrder(createOrderDto()));
        Assertions.assertEquals("No such username!", exception.getMessage());
        Mockito.verify(userService).describe();
        Mockito.verify(userRepository).findByUsername("username");
        Mockito.verifyNoInteractions(orderRepository);
    }

    @Test
    void testHandleOrderShouldCallOrderRepositorySaveMethodWhenUserIsLoggedIn() {
        // Given
        Mockito.when(userService.describe()).thenReturn(Optional.of(USER_DTO));
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(USER));

        // When
        underTest.handleOrder(createOrderDto());

        // Then
        Mockito.verify(userService).describe();
        Mockito.verify(userRepository).findByUsername("username");
        Mockito.verify(orderRepository).save(createOrderEntity());
    }

    @Test
    void testRetrieveOrdersForUserShouldReturnWithAListOfOrders() {
        // Given
        Mockito.when(orderRepository.findByUserUsername("username")).thenReturn(List.of(createOrderEntity()));
        List<OrderDto> expected = List.of(createOrderDto());

        // When
        List<OrderDto> actual = underTest.retrieveOrdersForUser(USER_DTO);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(orderRepository).findByUserUsername("username");
        Mockito.verifyNoInteractions(userRepository, userService);
    }

    private Order createOrderEntity() {
        OrderItem orderItem = OrderItem.builder()
            .name(PRODUCT_NAME)
            .netPriceAmount(NET_PRICE_AMOUNT)
            .netPriceCurrencyCode("HUF")
            .build();
        return Order.builder()
            .orderItems(List.of(orderItem))
            .user(USER)
            .netPriceAmount(NET_PRICE_AMOUNT)
            .netPriceCurrencyCode("HUF")
            .grossPriceAmount(GROSS_PRICE_AMOUNT)
            .grossPriceCurrencyCode("HUF")
            .build();
    }

    private OrderDto createOrderDto() {
        Money orderNetPrice = new Money(NET_PRICE_AMOUNT, CURRENCY);
        Money orderGrossPrice = new Money(GROSS_PRICE_AMOUNT, CURRENCY);
        ProductDto productDto = ProductDto.builder()
            .withName(PRODUCT_NAME)
            .withNetPrice(new Money(NET_PRICE_AMOUNT, CURRENCY))
            .build();
        return new OrderDto(List.of(productDto), orderNetPrice, orderGrossPrice);
    }
}
