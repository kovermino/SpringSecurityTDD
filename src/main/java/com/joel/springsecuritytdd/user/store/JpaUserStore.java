package com.joel.springsecuritytdd.user.store;

import com.joel.springsecuritytdd.constant.ExceptionConstant;
import com.joel.springsecuritytdd.user.entity.UserEntity;
import com.joel.springsecuritytdd.user.exception.UserException;
import com.joel.springsecuritytdd.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserStore implements UserStore {

    private final UserRepository userRepository;

    @Override
    public UserEntity addUser(UserEntity user) {
        UserEntity savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser != null) {
            throw new UserException(ExceptionConstant.USER__USER_ALREADY_EXISTS);
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) throw new UserException(ExceptionConstant.USER__NO_SUCH_USER);
        return user;
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        UserEntity savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser == null) throw new UserException(ExceptionConstant.USER__NO_SUCH_USER);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UserEntity user) {
        UserEntity savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser == null) throw new UserException(ExceptionConstant.USER__NO_SUCH_USER);
        userRepository.delete(user);
    }
}
