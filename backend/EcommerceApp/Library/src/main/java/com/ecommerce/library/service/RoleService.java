package com.ecommerce.library.service;

import com.ecommerce.library.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role addRole(Role role);

    boolean isRoleExists(Role role);
}
