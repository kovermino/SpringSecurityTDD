package com.flab.securitydemo.user.entity;

import com.flab.securitydemo.auth.domain.UserRole;
import com.flab.securitydemo.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserEntity {
    private String email;
    private String password;
    private List<UserRole> roles;

    public UserDto toUserDto() {
        return UserDto.builder()
                .email(this.email)
                .encryptedPassword(this.password)
                .roles(this.roles)
                .build();
    }
}
