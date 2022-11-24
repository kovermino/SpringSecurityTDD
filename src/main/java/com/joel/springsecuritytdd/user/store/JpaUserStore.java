package com.flab.securitydemo.user.store;

import com.flab.securitydemo.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaUserStore implements UserStore {

    @Override
    public UserEntity addUser(UserEntity user) {
        return null;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        return null;
    }

    @Override
    public void deleteUser(UserEntity user) {

    }
}
