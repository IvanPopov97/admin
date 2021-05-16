package ru.admin.config;

import org.passay.CharacterRule;
import org.passay.PasswordGenerator;

import java.util.List;
import java.util.Random;

public class PasswordGeneratorTemplate extends PasswordGenerator {
    private final List<CharacterRule> rules;
    private final int minLength;
    private final int maxLength;

    public PasswordGeneratorTemplate(int minLength, int maxLength, List<CharacterRule> rules) {
        this.rules = rules;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String generatePassword() {
        int defaultPasswordLength = 10;
        int length = new Random().ints(minLength, maxLength)
                .findFirst()
                .orElse(defaultPasswordLength);
        return super.generatePassword(length, rules);
    }
}
