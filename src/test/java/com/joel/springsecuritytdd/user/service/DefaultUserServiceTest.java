package com.joel.springsecuritytdd.user.service;

import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.user.dto.UserDto;
import com.joel.springsecuritytdd.user.entity.UserEntity;
import com.joel.springsecuritytdd.user.mock.SpyStubUserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultUserServiceTest {

    private DefaultUserService sut;
    private SpyStubUserStore userStore;
    private UserEntity joel;
    private UserEntity jay;

    @BeforeEach
    void setUp() {
        joel = UserEntity.builder()
                .email("joel@sample.com")
                .password("1234")
                .roles(Set.of(UserRole.ROLE_ADMIN))
                .build();
        jay = UserEntity.builder()
                .email("jay@sample.com")
                .password("5678")
                .roles(Set.of(UserRole.ROLE_PLAIN))
                .build();
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
    void getUserByEmail_이메일을_통해_사용자를_조회할_수_있다() {
        UserDto user = sut.getUserByEmail(joel.getEmail());


        assertEquals(joel.getEmail(), user.getEmail());
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
    void deleteUser_사용자를_삭제할_수_있다() {
        sut.deleteUser(joel.toUserDto());


        assertEquals(joel.getEmail(), userStore.getDeletedUserEmail());
    }

    @Test
    void loadUserByUsername_올바른_사용자_이메일을_리턴한다() {
        UserDetails savedJoel = sut.loadUserByUsername(joel.getEmail());
        UserDetails savedJay = sut.loadUserByUsername(jay.getEmail());


        assertEquals(joel.getEmail(), savedJoel.getUsername());
        assertEquals(jay.getEmail(), savedJay.getUsername());
    }

    @Test
    void loadUserByUsername_올바른_사용자_권한을_리턴한다() {
        UserDetails savedJoel = sut.loadUserByUsername(joel.getEmail());
        List<String> userRoles = savedJoel.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());


        assertEquals(joel.getRoles().stream().toList().get(0).name(), userRoles.get(0));
    }
}