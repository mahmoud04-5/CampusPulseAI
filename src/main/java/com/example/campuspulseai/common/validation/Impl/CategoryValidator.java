package com.example.campuspulseai.common.validation.Impl;

import com.example.campuspulseai.southBound.Enum.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.campuspulseai.domain.validation.ValidCategory;

public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        for (Category c : Category.values()) {
            if (c.name().equals(value) || c.getCategory().equals(value)) {
                return true;
            }
        }
        return false;
    }
}