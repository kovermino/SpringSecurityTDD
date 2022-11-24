package com.joel.springsecuritytdd.user.service;

import com.joel.springsecuritytdd.auth.domain.UserAuthModel;
import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.user.dto.UserDto;
import com.joel.springsecuritytdd.user.entity.UserEntity;
import com.joel.springsecuritytdd.user.store.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserStore userStore;

    @Override
    public UserDto addUser(UserDto user) {
        UserEntity savedUser = userStore.addUser(user.toUserEntity());
        return savedUser.toUserDto();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity user = userStore.getUserByEmail(email);
        return user.toUserDto();
    }

    @Override
    public UserDto updateUser(UserDto user) {
        UserEntity updatedUser = userStore.updateUser(user.toUserEntity());
        return updatedUser.toUserDto();
    }

    @Override
    public void deleteUser(UserDto user) {
        UserEntity savedUser = userStore.getUserByEmail(user.getEmail());
        userStore.deleteUser(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userStore.getUserByEmail(username);
        return new UserAuthModel(user.getEmail(), user.getPassword(), user.getRoles().stream().map(UserRole::name).collect(Collectors.toList()));
    }
}
