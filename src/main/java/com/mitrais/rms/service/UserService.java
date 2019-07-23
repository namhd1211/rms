package com.mitrais.rms.service;

import com.mitrais.rms.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();

    User save(User user);

    User edit(User user);

    void delete(Long userId);

    User findByUserName(String userName);
}
