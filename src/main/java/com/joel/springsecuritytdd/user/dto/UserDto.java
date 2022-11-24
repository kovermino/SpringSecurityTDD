package com.flab.securitydemo.user.dto;

import com.flab.securitydemo.auth.domain.UserRole;
import com.flab.securitydemo.user.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String email;
    private String encryptedPassword;
    private List<UserRole> roles;

    public UserEntity toUserEntity() {
        return new UserEntity(
                this.email,
                this.encryptedPassword,
                this.roles
        );
    }
}
