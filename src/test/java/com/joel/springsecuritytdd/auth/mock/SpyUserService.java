package com.joel.springsecuritytdd.auth.mock;

import com.joel.springsecuritytdd.user.dto.UserDto;
import com.joel.springsecuritytdd.user.service.UserService;

public class SpyUserService implements UserService {

    public String receivedPassword;

    @Override
    public UserDto addUser(UserDto user) {
        this.receivedPassword = user.getEncryptedPassword();
        return user;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto joel) {
        return null;
    }

    @Override
    public void deleteUser(UserDto user) {

    }
}
