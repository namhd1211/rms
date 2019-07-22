package com.mitrais.rms.service;

import com.mitrais.rms.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();

    User createUser(User user);

    User editUser(User user);

    void deleteUser(Long userId);
}
