package ru.admin.validation.validator;

import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import ru.admin.validation.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    private final PasswordValidator passwordValidator;
    private Password annotation;

    public PasswordConstraintValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void initialize(Password constraintAnnotation) {
        annotation = constraintAnnotation;
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null)
            return true;

        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid())
            return true;
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                annotation.message() + ": " + passwordValidator.getMessages(result).stream()
                        .map(message -> message.substring(0, message.length() - 1).toLowerCase())
                        .collect(Collectors.joining("; "))
        ).addConstraintViolation();
        return false;

    }
}
