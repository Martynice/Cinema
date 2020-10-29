package com.dev.cinema.security;

import com.dev.cinema.model.User;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    public AuthenticationServiceImpl(UserService userService,
                                     ShoppingCartService shoppingCartService) {
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public User register(String email, String password) {
        log.info("User with email " + email + " is trying to register");
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userService.add(user);
        shoppingCartService.registerNewShoppingCart(user);
        log.info("User with email " + email + " successfully registered");
        return user;
    }
}
