package com.dev.cinema.controllers;

import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import com.dev.cinema.service.RoleService;
import com.dev.cinema.service.UserService;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private final RoleService roleService;
    private final UserService userService;

    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    private void injectData() {
        roleService.add(Role.of(USER));
        roleService.add(Role.of(ADMIN));
        User admin = new User();
        admin.setEmail("admin@email.com");
        admin.setPassword("1234");
        admin.setRoles(Set.of(roleService.getRoleByName(ADMIN)));
        User user = new User();
        user.setEmail("user@email.com");
        user.setPassword("1111");
        user.setRoles(Set.of(roleService.getRoleByName(USER)));
        userService.add(admin);
        userService.add(user);
    }
}
