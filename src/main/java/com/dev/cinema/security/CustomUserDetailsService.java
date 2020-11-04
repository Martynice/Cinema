package com.dev.cinema.security;

import com.dev.cinema.model.User;
import com.dev.cinema.service.UserService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user != null) {
            UserBuilder builder = org.springframework.security.core.userdetails
                    .User.withUsername(user.getEmail());
            builder.password(user.getPassword());
            builder.roles(user.getRoles().stream()
                    .map(x -> x.getRoleName().toString())
                    .toArray(String[]::new));
            return builder.build();
        }
        throw new UsernameNotFoundException("Couldn't find user by email " + email);
    }
}
