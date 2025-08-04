package com.jc.clinical_managment.common.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Security annotation to check if current user can access specific user data
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@userAccessValidator.canAccess(authentication, #userId)")
public @interface RequireUserAccess {
    String value() default "userId";
}