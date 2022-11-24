package com.joel.springsecuritytdd.auth.helper;

import com.joel.springsecuritytdd.user.service.UserService;

public class SpyUserService implements UserService {

    public String receivedPassword;

    @Override
    public void createUser(String userName, String encryptedPassword) {
        this.receivedPassword = encryptedPassword;
    }
}
