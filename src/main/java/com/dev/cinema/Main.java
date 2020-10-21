package com.dev.cinema;

import com.dev.cinema.config.AppConfig;
import com.dev.cinema.exceptions.AuthenticationException;
import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.User;
import com.dev.cinema.security.AuthenticationService;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j
public class Main {
    private static final AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

    public static void main(String[] args) {
        Movie movie = new Movie();
        movie.setTitle("Fast and Furious");
        MovieService movieService = context.getBean(MovieService.class);
        movieService.add(movie);
        movieService.getAll().forEach(log::info);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        CinemaHallService cinemaHallService = context.getBean(CinemaHallService.class);
        cinemaHallService.add(cinemaHall);
        cinemaHallService.getAll().forEach(log::info);

        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movie);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setShowTime(LocalDateTime.now());
        MovieSessionService movieSessionService = context.getBean(MovieSessionService.class);
        movieSessionService.add(movieSession);
        movieSessionService.findAvailableSessions(movie.getId(), LocalDate.now())
                .forEach(log::info);

        AuthenticationService authenticationService = context.getBean(AuthenticationService.class);
        User admin = authenticationService.register("admin@admin.com", "1111");
        try {
            log.info(authenticationService.login("admin@admin.com", "1111"));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Couldn't login", e);
        }

        ShoppingCartService shoppingCartService = context.getBean(ShoppingCartService.class);
        shoppingCartService.addSession(movieSession, admin);
        log.info(shoppingCartService.getByUser(admin));

        OrderService orderService = context.getBean(OrderService.class);
        orderService.completeOrder(shoppingCartService.getByUser(admin));
        orderService.getOrderHistory(admin).forEach(log::info);
    }
}
