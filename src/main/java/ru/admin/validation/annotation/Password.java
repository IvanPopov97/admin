package ru.admin.validation.annotation;

import ru.admin.validation.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface Password {

    String message() default "пароль слишком простой и не удовлетворяет бизнес-правилам";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
