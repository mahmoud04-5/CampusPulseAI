package com.example.campuspulseai.common.validation.impl;

import com.example.campuspulseai.common.validation.ValidCloudinaryPath;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Pattern;

public class CloudinaryPathValidator implements ConstraintValidator<ValidCloudinaryPath, String> {
    @Value("${cloudinary.url_pattern}")
    private String urlPattern;

    private Pattern pattern;

    @PostConstruct
    public void init() {
        pattern = Pattern.compile(urlPattern);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || pattern.matcher(value).matches();
    }
}
