package com.flab.securitydemo.user.service;

import com.flab.securitydemo.auth.domain.UserRole;
import com.flab.securitydemo.user.dto.UserDto;
import com.flab.securitydemo.user.entity.UserEntity;
import com.flab.securitydemo.user.exception.UserException;
import com.flab.securitydemo.user.mock.SpyStubUserStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultUserServiceTest {

    private UserService sut;
    private SpyStubUserStore userStore;
    private UserEntity joel;
    private UserEntity jay;

    @BeforeEach
    void setUp() {
        joel = new UserEntity(
                "joel@sample.com",
                "1234",
                List.of(UserRole.ROLE_ADMIN)
        );
        jay = new UserEntity(
                "jay@sample.com",
                "5678",
                List.of(UserRole.ROLE_PLAIN)
        );
        userStore = new SpyStubUserStore(List.of(joel, jay));
        sut = new DefaultUserService(userStore);
    }

    @Test
    void addUser_사용자를_생성할_수_있다() {
        UserDto user = UserDto.builder()
                .email("beat@sample.com")
                .encryptedPassword("someEncryptedPassword")
                .roles(List.of(UserRole.ROLE_PLAIN))
                .build();


        UserDto savedUser = sut.addUser(user);


        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void addUser_이미_존재하는_사용자_이메일로_계정을_생성하면_예외가_발생한다() {
        assertThrows(UserException.class, () -> sut.addUser(joel.toUserDto()));
    }

    @Test
    void getUserByEmail_이메일을_통해_사용자를_조회할_수_있다() {
        UserDto user = sut.getUserByEmail(joel.getEmail());


        assertEquals(joel.getEmail(), user.getEmail());
    }

    @Test
    void getUserByEmail_존재하지_않는_사용자를_조회하면_예외가_발생한다() {
        assertThrows(UserException.class, () -> sut.getUserByEmail("nosuchuser@sample.com"));
    }

    @Test
    void updateUser_사용자_비밀번호를_업데이트할_수_있다() {
        String changedPassword = "changedPassword";
        joel.setPassword(changedPassword);


        UserDto savedJoel = sut.updateUser(joel.toUserDto());


        assertEquals(changedPassword, savedJoel.getEncryptedPassword());
        assertTrue(userStore.updatedPasswordEquals(changedPassword));
    }

    @Test
    void updateUser_존재하지_않는_사용자의_업데이트를_시도하면_예외가_발생한다() {
        joel.setEmail("notjoel@sample.com");


        assertThrows(UserException.class, () -> sut.updateUser(joel.toUserDto()));
    }

    @Test
    void deleteUser_사용자를_삭제할_수_있다() {
        sut.deleteUser(joel.toUserDto());


        assertThrows(UserException.class, () -> sut.getUserByEmail(joel.getEmail()));
    }

    @Test
    void deleteUser_존재하지_않는_사용자는_삭제할_수_없다() {
        joel.setEmail("notjoel@sample.com");


        assertThrows(UserException.class, () -> sut.deleteUser(joel.toUserDto()));
    }
}