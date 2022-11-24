package com.joel.springsecuritytdd.user.store;

import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.constant.ExceptionConstant;
import com.joel.springsecuritytdd.user.entity.UserEntity;
import com.joel.springsecuritytdd.user.exception.UserException;
import com.joel.springsecuritytdd.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaUserStoreTest {

    private JpaUserStore sut;
    private UserRepository userRepository;
    private UserEntity joel;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        sut = new JpaUserStore(userRepository);
        joel = UserEntity.builder()
                .email("joel@sample.com")
                .password("samplePassword")
                .roles(Set.of(UserRole.ROLE_ADMIN))
                .build();
    }

    @Test
    void addUser_일반_사용자를_생성한다() {
        when(userRepository.save(joel)).thenReturn(joel);


        UserEntity savedJoel = sut.addUser(joel);


        assertEquals(joel.getEmail(), savedJoel.getEmail());
        verify(userRepository, times(1)).save(joel);
    }

    @Test
    void addUser_이미_존재하는_이메일로_사용자를_생성하면_예외가_발생한다() {
        when(userRepository.findByEmail(joel.getEmail())).thenReturn(joel);


        UserException ex = assertThrows(UserException.class, () -> sut.addUser(joel));
        assertEquals(ExceptionConstant.USER__USER_ALREADY_EXISTS, ex.getMessage());
    }

    @Test
    void getUserByEmail_이메일로_사용자를_조회할_수_있다() {
        when(userRepository.findByEmail(joel.getEmail())).thenReturn(joel);


        UserEntity user = sut.getUserByEmail(joel.getEmail());


        assertEquals(joel.getEmail(), user.getEmail());
        verify(userRepository, times(1)).findByEmail(joel.getEmail());
    }

    @Test
    void getUserByEmail_해당_이메일의_사용자가_없는_경우에는_예외가_발생한다() {
        when(userRepository.findByEmail(joel.getEmail())).thenReturn(null);


        UserException ex = assertThrows(UserException.class, () -> sut.getUserByEmail(joel.getEmail()));
        assertEquals(ExceptionConstant.USER__NO_SUCH_USER, ex.getMessage());
    }

    @Test
    void updateUser_사용자_정보를_업데이트_할_수_있어야한다() {
        UserEntity joelWithNewPassword = UserEntity.builder()
                .email(joel.getEmail())
                .password("newPassword")
                .roles(joel.getRoles())
                .build();
        when(userRepository.findByEmail(joel.getEmail())).thenReturn(joel);
        when(userRepository.save(joelWithNewPassword)).thenReturn(joelWithNewPassword);


        UserEntity user = sut.updateUser(joelWithNewPassword);


        assertEquals(joelWithNewPassword.getPassword(), user.getPassword());
        verify(userRepository, times(1)).save(joelWithNewPassword);
    }

    @Test
    void updateUser_존재하지_않는_사용자를_업데이트하면_예외가_발생한다() {
        UserException ex = assertThrows(UserException.class, () -> sut.updateUser(joel));
        assertEquals(ExceptionConstant.USER__NO_SUCH_USER, ex.getMessage());
    }

    @Test
    void deleteUser_사용자를_삭제한다() {
        when(userRepository.findByEmail(joel.getEmail())).thenReturn(joel);


        sut.deleteUser(joel);


        verify(userRepository, times(1)).delete(joel);
    }

    @Test
    void deleteUser_존재하지_않는_사용자를_삭제하면_예외가_발생한다() {
        when(userRepository.findByEmail(joel.getEmail())).thenReturn(null);


        UserException ex = assertThrows(UserException.class, () -> sut.deleteUser(joel));
        assertEquals(ExceptionConstant.USER__NO_SUCH_USER, ex.getMessage());
    }
}