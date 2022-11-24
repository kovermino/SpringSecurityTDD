package com.joel.springsecuritytdd.user.converter;

import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import java.util.List;

public class AuthorityJsonConverter implements AttributeConverter<List<String>, String> {

    private final Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
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
    public List<String> convertToEntityAttribute(String s) {
        try
        {
            if (s == null)
            {
                return null;
            }
            else
            {
                return gson.fromJson(s, List.class);
            }
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
