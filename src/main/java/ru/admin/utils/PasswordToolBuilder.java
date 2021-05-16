package ru.admin.utils;

import org.passay.*;
import ru.admin.config.PasswordGeneratorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PasswordToolBuilder {
    private final List<Rule> rules = new ArrayList<>(10);
    private final List<CharacterRule> characterRules = new ArrayList<>();
    private LengthRule lengthRule;

    public PasswordToolBuilder length(int min, int max) {
        LengthRule rule = new LengthRule(min, max);
        rules.add(rule);
        lengthRule = rule;
        return this;
    }

    public PasswordToolBuilder characterRule(CharacterData characterData, int count) {
        if (count > 0) {
            CharacterRule rule = new CharacterRule(characterData, count);
            rules.add(rule);
            characterRules.add(rule);
        }
        return this;
    }

    public PasswordToolBuilder sequenceRule(SequenceData sequenceData, int count) {
        if (count > 0) {
            rules.add(new IllegalSequenceRule(sequenceData, count, false));
        }
        return this;
    }

    public PasswordToolBuilder repeatCharacterRule(int count) {
        if (count > 0) {
            rules.add(new RepeatCharactersRule(count, 1));
        }
        return this;
    }

    public PasswordToolBuilder whitespaceRule() {
        rules.add(new WhitespaceRule());
        return this;
    }

    public PasswordValidator buildValidator() {
        return new PasswordValidator(rules);
    }

    public PasswordGeneratorTemplate buildGenerator(int min, int max) {
        if (lengthRule != null) {
            min = Math.max(lengthRule.getMinimumLength(), min);
            max = Math.min(lengthRule.getMaximumLength(), max);
        }
        return new PasswordGeneratorTemplate(min, max, characterRules);
    }
}
