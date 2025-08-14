package com.example.campuspulseai.common.validation;

import com.example.campuspulseai.common.validation.Impl.CategoryNullableValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryNullableValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoryNullable {
    String message() default "Invalid event category";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
