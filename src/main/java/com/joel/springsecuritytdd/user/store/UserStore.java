package com.joel.springsecuritytdd.user.store;

import com.joel.springsecuritytdd.user.entity.UserEntity;

public interface UserStore {
    UserEntity addUser(UserEntity user);
    UserEntity getUserByEmail(String email);
    UserEntity updateUser(UserEntity user);
    void deleteUser(UserEntity user);
}
