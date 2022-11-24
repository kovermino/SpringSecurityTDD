package com.flab.securitydemo.user.store;

import com.flab.securitydemo.user.entity.UserEntity;

public interface UserStore {
    UserEntity addUser(UserEntity user);
    UserEntity getUserByEmail(String email);
    UserEntity updateUser(UserEntity user);
    void deleteUser(UserEntity user);
}
