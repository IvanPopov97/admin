package ru.admin.utils;

import org.passay.*;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidatorBuilder {
    private final List<Rule> rules = new ArrayList<>(10);

    public PasswordValidatorBuilder length(int min, int max) {
        rules.add(new LengthRule(min, max));
        return this;
    }

    public PasswordValidatorBuilder characterRule(CharacterData characterData, int count) {
        if (count > 0) {
            rules.add(new CharacterRule(characterData, count));
        }
        return this;
    }

    public PasswordValidatorBuilder sequenceRule(SequenceData sequenceData, int count) {
        if (count > 0) {
            rules.add(new IllegalSequenceRule(sequenceData, count, false));
        }
        return this;
    }

    public PasswordValidatorBuilder repeatCharacterRule(int count) {
        if (count > 0) {
            rules.add(new RepeatCharactersRule(count, 1));
        }
        return this;
    }

    public PasswordValidatorBuilder whitespaceRule() {
        rules.add(new WhitespaceRule());
        return this;
    }

    public PasswordValidator build() {
        return new PasswordValidator(rules);
    }

}
