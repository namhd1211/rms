package com.mitrais.rms.service.impl;

import com.mitrais.rms.entity.Role;
import com.mitrais.rms.repository.RoleRepository;
import com.mitrais.rms.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> saveAll(List<Role> roleList) {
        return roleRepository.saveAll(roleList);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

}
