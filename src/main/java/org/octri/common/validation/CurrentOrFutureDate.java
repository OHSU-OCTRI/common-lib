package org.octri.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation that adds {@link CurrentOrFutureDateValidator} validation to an entity field.
 */
@Documented
@Constraint(validatedBy = CurrentOrFutureDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CurrentOrFutureDate {

	String message() default "Date may not be in the past";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
