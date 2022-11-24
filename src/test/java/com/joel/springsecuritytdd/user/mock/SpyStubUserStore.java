package com.joel.springsecuritytdd.user.mock;

import com.joel.springsecuritytdd.user.entity.UserEntity;
import com.joel.springsecuritytdd.user.store.UserStore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpyStubUserStore implements UserStore {

    private Map<String, UserEntity> users;
    private String updatedPassword;
    private String deletedUserEmail;

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
        this.deletedUserEmail = user.getEmail();
        users.remove(user.getEmail());
    }

    public boolean updatedPasswordEquals(String password) {
        return this.updatedPassword.equals(password);
    }
    public String getDeletedUserEmail() { return this.deletedUserEmail; }
}
