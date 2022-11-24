package com.flab.securitydemo.user.mock;

import com.flab.securitydemo.user.entity.UserEntity;
import com.flab.securitydemo.user.store.UserStore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpyStubUserStore implements UserStore {

    private Map<String, UserEntity> users;
    private String updatedPassword;

    public SpyStubUserStore(List<UserEntity> users) {
        this.users = users.stream().collect(Collectors.toMap(user -> user.getEmail(), user -> user));
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        users.put(user.getEmail(), user);
        return users.get(user.getEmail());
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return users.get(email);
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        updatedPassword = user.getPassword();
        users.put(user.getEmail(), user);
        return users.get(user.getEmail());
    }

    @Override
    public void deleteUser(UserEntity user) {
        users.remove(user.getEmail());
    }

    public boolean updatedPasswordEquals(String password) {
        return this.updatedPassword.equals(password);
    }
}
