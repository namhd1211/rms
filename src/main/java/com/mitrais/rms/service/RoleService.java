package com.mitrais.rms.service;

import com.mitrais.rms.entity.Role;

import java.util.List;

public interface RoleService {
    Role save(Role role);

    List<Role> saveAll(List<Role> roleList);

    List<Role> findAll();
}
