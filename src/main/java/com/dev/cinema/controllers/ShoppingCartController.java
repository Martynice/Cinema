package com.dev.cinema.controllers;

import com.dev.cinema.mappers.ShoppingCartMapper;
import com.dev.cinema.model.dto.ShoppingCartResponseDto;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final MovieSessionService movieSessionService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(MovieSessionService movieSessionService,
                                  ShoppingCartMapper shoppingCartMapper,
                                  ShoppingCartService shoppingCartService,
                                  UserService userService) {
        this.movieSessionService = movieSessionService;
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @PostMapping("/movie-sessions")
    public void addMovieSession(Authentication authentication, @RequestParam Long movieSessionId) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        shoppingCartService.addSession(
                movieSessionService.get(movieSessionId), userService.findByEmail(email));
    }

    @GetMapping("/by-user")
    public ShoppingCartResponseDto getByUser(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return shoppingCartMapper.toResponseDto(
                shoppingCartService.getByUser(userService.findByEmail(email)));
    }
}
