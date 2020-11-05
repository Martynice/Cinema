package com.dev.cinema.dao;

import com.dev.cinema.model.Role;

public interface RoleDao {
    void add(Role role);

    Role getRoleByName(String roleName);
}
