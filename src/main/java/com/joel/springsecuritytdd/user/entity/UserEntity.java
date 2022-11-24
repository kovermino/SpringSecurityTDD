package com.joel.springsecuritytdd.user.entity;

import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.user.converter.StringListJsonConverter;
import com.joel.springsecuritytdd.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;

    @Convert(converter = StringListJsonConverter.class)
    private Set<UserRole> roles;

    public UserDto toUserDto() {
        return UserDto.builder()
                .email(this.email)
                .encryptedPassword(this.password)
                .roles(this.roles.stream().toList())
                .build();
    }
}
