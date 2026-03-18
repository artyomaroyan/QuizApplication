package org.pure.java.project.model;

import org.pure.java.project.model.dto.QuestionInputDto;
import org.pure.java.project.model.result.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 15.03.26
 * Time: 00:44:03
 */
public class InputValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputValidator.class);
    private static final int MIN_QUESTION_LENGTH = 5;
    private static final int MAX_QUESTION_LENGTH = 500;
    private static final int MIN_ANSWERS = 2;
    private static final int MAX_ANSWERS = 10;
    private static final int MIN_ANSWER_LENGTH = 1;
    private static final int MAX_ANSWER_LENGTH = 200;
    private static final Pattern VALID_DIFFICULTY_PATTERN = Pattern.compile("EASY|MEDIUM|HARD", Pattern.CASE_INSENSITIVE);
    private static final Pattern INVALID_CHARS_PATTERN = Pattern.compile("[<>\"&]");

    public ValidationResult validate(QuestionInputDto input) {
        List<String> errors = new ArrayList<>();
        if (input == null) {
            errors.add("Input can not be null");
            return new ValidationResult(false, errors);
        }
        errors.addAll(validateQuestion(input.question()));

        boolean isValid = errors.isEmpty();
        if (!isValid) {
            LOGGER.debug("Validation failed with {} errors: {}", errors.size(), errors);
        }
        return new ValidationResult(isValid, errors);
    }

    public List<String> validateQuestion(String question) {
        List<String> errors = new ArrayList<>();
        if (question == null) {
            errors.add("Question can not be null");
            return errors;
        }

        String trimmed = question.trim();
        if (trimmed.isEmpty()) {
            errors.add("Question can not be empty");
        } else if (trimmed.length() < MIN_QUESTION_LENGTH) {
            errors.add(String.format("Question must be at least %d characters long", MIN_QUESTION_LENGTH));
        } else if (trimmed.length() > MAX_QUESTION_LENGTH) {
            errors.add(String.format("Question can not exceed %d characters", MAX_QUESTION_LENGTH));
        }

        if (INVALID_CHARS_PATTERN.matcher(trimmed).find()) {
            errors.add("Question contains invalid characters (<, >, \", ', &)");
        }

        if (!trimmed.endsWith("?")) {
            errors.add("Question should end with question mark (?)");
        }
        return errors;
    }

    public List<String> validateAnswers(List<String> answers, int correctIndex) {
        List<String> errors = new ArrayList<>();
        if (answers == null) {
            errors.add("Answers can not be null");
            return errors;
        }

        if (answers.size() < MIN_ANSWERS) {
            errors.add(String.format("At least %d answers are required", MIN_ANSWERS));
        } else if (answers.size() > MAX_ANSWERS) {
            errors.add(String.format("Can not have more than %d answers", MAX_ANSWERS));
        }

        Set<String> uniqueAnswers = new HashSet<>();
        for (String answer : answers) {
            if (answer == null) {
                errors.add("Answer can not be null");
                continue;
            }

            String trimmed = answer.trim();
            if (trimmed.isEmpty()) {
                errors.add("Answer can not be empty");
            } else if (trimmed.length() > MAX_ANSWER_LENGTH) {
                errors.add(String.format("Answer can not exceed %d characters: '%s...'", MAX_ANSWER_LENGTH, trimmed.substring(0, Math.min(10, trimmed.length()))));
            }

            if (!uniqueAnswers.add(trimmed.toLowerCase())) {
                errors.add(String.format("Duplicate answer found: %s", trimmed));
            }
        }

        if (answers.isEmpty()) {
            errors.add("Can not validate correct index with empty answers");
        }
        if (correctIndex < 0) {
            errors.add("Correct answer index can not be negative");
        } else if (correctIndex >= answers.size()) {
            errors.add(String.format("Correct answer index (%d) must be less then number of answers (%d)", correctIndex, answers.size()));
        }
        return errors;
    }
}