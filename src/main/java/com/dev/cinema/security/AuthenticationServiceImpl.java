package com.dev.cinema.security;

import com.dev.cinema.exceptions.AuthenticationException;
import com.dev.cinema.lib.Inject;
import com.dev.cinema.lib.Service;
import com.dev.cinema.model.User;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import com.dev.cinema.util.HashUtil;
import java.util.Optional;
import org.apache.log4j.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);
    @Inject
    private UserService userService;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        logger.info("User with email " + email + " is trying to login");
        Optional<User> userFromDB = userService.findByEmail(email);
        if (userFromDB.isEmpty() || !isPasswordValid(password, userFromDB.get())) {
            throw new AuthenticationException("Incorrect login or password");
        }
        logger.info("User with email " + email + " successfully logged in");
        return userFromDB.get();
    }

    @Override
    public User register(String email, String password) {
        logger.info("User with email " + email + " is trying to register");
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userService.add(user);
        shoppingCartService.registerNewShoppingCart(user);
        logger.info("User with email " + email + " successfully registered");
        return user;
    }

    private boolean isPasswordValid(String password, User user) {
        return HashUtil.hashPassword(password, user.getSalt()).equals(user.getPassword());
    }
}
