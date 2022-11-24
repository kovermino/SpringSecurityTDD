package com.joel.springsecuritytdd.user.service;

import com.joel.springsecuritytdd.user.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto user);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto user);
    void deleteUser(UserDto user);
}
