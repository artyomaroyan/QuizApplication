package org.pure.java.project.model.validator;

import org.pure.java.project.model.dto.QuestionInputDto;
import org.pure.java.project.model.entity.Question;
import org.pure.java.project.model.enums.Difficulty;
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
    private static final int MAX_ANSWER_LENGTH = 200;
    private static final Pattern VALID_DIFFICULTY_PATTERN = Pattern.compile("EASY|MEDIUM|HARD", Pattern.CASE_INSENSITIVE);
    private static final Pattern INVALID_CHARS_PATTERN = Pattern.compile("[<>\"&]");

    private static final List<String> ERRORS = new ArrayList<>();

    public ValidationResult validate(QuestionInputDto input) {
        if (input == null) {
            ERRORS.add("Input can not be null");
            return new ValidationResult(false, ERRORS);
        }
        ERRORS.addAll(validateQuestion(input.question()));

        boolean isValid = ERRORS.isEmpty();
        if (!isValid) {
            LOGGER.debug("Validation failed with {} errors: {}", ERRORS.size(), ERRORS);
        }
        return new ValidationResult(isValid, ERRORS);
    }

    public List<String> validateQuestion(String question) {
        if (question == null) {
            ERRORS.add("Question can not be null");
            return ERRORS;
        }

        String trimmed = question.trim();
        if (trimmed.isEmpty()) {
            ERRORS.add("Question can not be empty");
        } else if (trimmed.length() < MIN_QUESTION_LENGTH) {
            ERRORS.add(String.format("Question must be at least %d characters long", MIN_QUESTION_LENGTH));
        } else if (trimmed.length() > MAX_QUESTION_LENGTH) {
            ERRORS.add(String.format("Question can not exceed %d characters", MAX_QUESTION_LENGTH));
        }

        if (INVALID_CHARS_PATTERN.matcher(trimmed).find()) {
            ERRORS.add("Question contains invalid characters (<, >, \", ', &)");
        }

        if (!trimmed.endsWith("?")) {
            ERRORS.add("Question should end with question mark (?)");
        }
        return ERRORS;
    }

    public List<String> validateAnswers(List<String> answers, int correctIndex) {
        if (answers == null) {
            ERRORS.add("Answers can not be null");
            return ERRORS;
        }

        if (answers.size() < MIN_ANSWERS) {
            ERRORS.add(String.format("At least %d answers are required", MIN_ANSWERS));
        } else if (answers.size() > MAX_ANSWERS) {
            ERRORS.add(String.format("Can not have more than %d answers", MAX_ANSWERS));
        }

        Set<String> uniqueAnswers = new HashSet<>();
        for (String answer : answers) {
            if (answer == null) {
                ERRORS.add("Answer can not be null");
                continue;
            }

            String trimmed = answer.trim();
            if (trimmed.isEmpty()) {
                ERRORS.add("Answer can not be empty");
            } else if (trimmed.length() > MAX_ANSWER_LENGTH) {
                ERRORS.add(String.format("Answer can not exceed %d characters: '%s...'", MAX_ANSWER_LENGTH, trimmed.substring(0, 10)));
            }

            if (!uniqueAnswers.add(trimmed.toLowerCase())) {
                ERRORS.add(String.format("Duplicate answer found: %s", trimmed));
            }
        }

        if (answers.isEmpty()) {
            ERRORS.add("Can not validate correct index with empty answers");
        }
        if (correctIndex < 0) {
            ERRORS.add("Correct answer index can not be negative");
        } else if (correctIndex >= answers.size()) {
            ERRORS.add(String.format("Correct answer index (%d) must be less then number of answers (%d)", correctIndex, answers.size()));
        }
        return ERRORS;
    }

    public List<String> validateSingleAnswer(String answer) {
        if (answer == null) {
            ERRORS.add("Answer can not be null");
            return ERRORS;
        }

        String trimmed = answer.trim();
        if (trimmed.isEmpty()) {
            ERRORS.add("Answer can not be empty");
        } else if (trimmed.length() > MAX_ANSWER_LENGTH) {
            ERRORS.add(String.format("Answer can not exceed %d characters", MAX_ANSWER_LENGTH));
        }

        if (INVALID_CHARS_PATTERN.matcher(trimmed).matches()) {
            ERRORS.add("Answer contains invalid characters (<, >, \", ', &)");
        }
        return ERRORS;
    }

    public List<String> validateDifficulty(String difficulty) {
        if (difficulty == null) {
            ERRORS.add("Difficulty can not be null");
            return ERRORS;
        }

        String trimmed = difficulty.trim();
        if (trimmed.isEmpty()) {
            ERRORS.add("Difficulty can not be empty");
        } else if (!VALID_DIFFICULTY_PATTERN.matcher(trimmed).matches()) {
            ERRORS.add(String.format("Invalid difficulty: '%s'. Must be EASY, MEDIUM or HARD", trimmed));
        }
        return ERRORS;
    }

    public ValidationResult validateQuestionExists(Long id, List<Question> questions) {
        if (id == null) {
            return new ValidationResult(false, List.of("Question id can not be null"));
        }

        if (questions == null) {
            return new ValidationResult(false, List.of("Question list can not be null"));
        }

        boolean exists = questions.stream()
                .anyMatch(q -> q.id().equals(id));

        if (!exists) {
            return new ValidationResult(false, List.of("Question with id " + id + " not found"));
        }
        return new ValidationResult(true, List.of());
    }

    public ValidationResult validateExamParameters(String difficulty, int questionsCount, List<Question> availableQuestions) {
        List<String> difficultyErrors = validateDifficulty(difficulty);
        ERRORS.addAll(difficultyErrors);

        if (questionsCount <= 0) {
            ERRORS.add("Question count must be positive");
        }

        if (availableQuestions != null && difficultyErrors.isEmpty()) {
            try {
                Difficulty diff = Difficulty.valueOf(difficulty.trim().toUpperCase());
                long availableCount = availableQuestions.stream()
                        .filter(q -> q.difficulty() == diff)
                        .count();

                if (availableCount == 0) {
                    ERRORS.add("No question available for difficulty: " + difficulty);
                } else if (questionsCount > availableCount) {
                    ERRORS.add(String.format("Only %d questions available for %s difficulty, but %d requested",
                            availableCount, difficulty, questionsCount));
                }
            } catch (IllegalArgumentException _) {
            }
        }
        return new ValidationResult(ERRORS.isEmpty(), ERRORS);
    }
}