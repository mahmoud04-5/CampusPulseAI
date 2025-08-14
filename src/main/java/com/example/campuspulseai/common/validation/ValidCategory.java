package com.example.campuspulseai.domain.validation;

import com.example.campuspulseai.common.validation.impl.CategoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategory {
    String message() default "Invalid event category";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}