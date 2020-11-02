package com.dev.cinema.controllers;

import com.dev.cinema.mappers.OrderMapper;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.dto.OrderResponseDto;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, ShoppingCartService shoppingCartService,
                           UserService userService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/complete")
    public void complete(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        ShoppingCart shoppingCart = shoppingCartService.getByUser(userService.findByEmail(email));
        orderService.completeOrder(shoppingCart);
    }

    @GetMapping
    public List<OrderResponseDto> getOrderHistory(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return orderService.getOrderHistory(userService.findByEmail(email)).stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
