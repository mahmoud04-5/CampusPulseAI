package com.example.campuspulseai.Common.Converter;

import com.example.campuspulseai.domain.Enum.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RoleListConverter implements AttributeConverter<Set<Role>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return Set.of();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}