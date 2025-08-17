package com.example.campuspulseai.common.validation.impl;

import com.example.campuspulseai.common.validation.ValidCategoryNullable;
import com.example.campuspulseai.southbound.Enum.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryNullableValidator implements ConstraintValidator<ValidCategoryNullable, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        for (Category c : Category.values()) {
            if (c.name().equals(value) || c.getCategory().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
