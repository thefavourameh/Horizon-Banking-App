package com.favour.Horizon.Banking.App.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

    String message() default "invalid email";

    Class<?> [] groups() default {};

    Class<? extends Payload>[] payload() default {};





}


