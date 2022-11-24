package com.flab.securitydemo.user.service;

import com.flab.securitydemo.constant.ExceptionConstant;
import com.flab.securitydemo.user.dto.UserDto;
import com.flab.securitydemo.user.entity.UserEntity;
import com.flab.securitydemo.user.exception.UserException;
import com.flab.securitydemo.user.store.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserStore userStore;

    @Override
    public UserDto addUser(UserDto user) {
        UserEntity savedUser = userStore.getUserByEmail(user.getEmail());
        if(savedUser != null) {
            throw new UserException(ExceptionConstant.USER__USER_ALREADY_EXISTS);
        }
        savedUser = userStore.addUser(user.toUserEntity());
        return savedUser.toUserDto();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity user = userStore.getUserByEmail(email);
        if(user == null) throw new UserException(ExceptionConstant.USER__NO_SUCH_USER);
        return user.toUserDto();
    }

    @Override
    public UserDto updateUser(UserDto user) {
        UserEntity savedUser = userStore.getUserByEmail(user.getEmail());
        if(savedUser == null) throw new UserException(ExceptionConstant.USER__NO_SUCH_USER);
        UserEntity updatedUser = userStore.updateUser(user.toUserEntity());
        return updatedUser.toUserDto();
    }

    @Override
    public void deleteUser(UserDto user) {
        UserEntity savedUser = userStore.getUserByEmail(user.getEmail());
        if(savedUser == null) throw new UserException(ExceptionConstant.USER__NO_SUCH_USER);
        userStore.deleteUser(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
