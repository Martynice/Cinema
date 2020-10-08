package com.dev.cinema;

import com.dev.cinema.exceptions.AuthenticationException;
import com.dev.cinema.lib.Injector;
import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.User;
import com.dev.cinema.security.AuthenticationService;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    private static Injector injector = Injector.getInstance("com.dev.cinema");

    public static void main(String[] args) {
        Movie movie = new Movie();
        movie.setTitle("Fast and Furious");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(movie);
        movieService.getAll().forEach(System.out::println);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        cinemaHallService.add(cinemaHall);
        cinemaHallService.getAll().forEach(System.out::println);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.now());
        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);
        movieSessionService.findAvailableSessions(movie.getId(), LocalDate.now())
                .forEach(System.out::println);

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        User admin = authenticationService.register("admin@admin.com", "1111");
        try {
            System.out.println(authenticationService.login("admin@admin.com", "1111"));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Couldn't login", e);
        }

        ShoppingCartService shoppingCartService
                = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        shoppingCartService.registerNewShoppingCart(admin);
        shoppingCartService.addSession(movieSession, admin);
        System.out.println(shoppingCartService.getByUser(admin));
        shoppingCartService.clear(shoppingCartService.getByUser(admin));
        System.out.println(shoppingCartService.getByUser(admin));
    }
}
