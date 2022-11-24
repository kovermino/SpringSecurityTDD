package com.joel.springsecuritytdd.user.dto;

import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.user.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDto {
    private String email;
    private String encryptedPassword;
    private List<UserRole> roles;

    public UserEntity toUserEntity() {
        return UserEntity.builder()
                .email(this.email)
                .password(this.encryptedPassword)
                .roles(this.roles.stream().collect(Collectors.toSet()))
                .build();
    }
}
