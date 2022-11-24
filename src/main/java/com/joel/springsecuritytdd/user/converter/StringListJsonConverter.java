package com.joel.springsecuritytdd.user.converter;

import com.google.gson.Gson;
import com.joel.springsecuritytdd.auth.domain.UserRole;

import javax.persistence.AttributeConverter;
import java.util.Set;
import java.util.stream.Collectors;

public class StringListJsonConverter implements AttributeConverter<Set<UserRole>, String> {

    private final Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(Set<UserRole> strings) {
        try
        {
            if (strings == null)
            {
                return null;
            }
            else
            {
                return gson.toJson(strings);
            }
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    @Override
    public Set<UserRole> convertToEntityAttribute(String s) {
        try
        {
            if (s == null)
            {
                return null;
            }
            else
            {
                Set<String> roles = gson.fromJson(s, Set.class);
                return roles.stream().map(UserRole::valueOf).collect(Collectors.toSet());
            }
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
