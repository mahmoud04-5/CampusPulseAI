package com.example.campuspulseai.common.validation;

import com.example.campuspulseai.common.validation.impl.CloudinaryPathValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CloudinaryPathValidator.class)
public @interface ValidCloudinaryPath {
    String message() default "Invalid Cloudinary URL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
