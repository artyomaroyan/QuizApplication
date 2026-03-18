package org.pure.java.project.model.result;

import org.pure.java.project.model.Question;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 15.03.26
 * Time: 00:14:34
 */
public class QuestionSaveResult {
    private final boolean success;
    private final String message;
    private final Question question;
    private final List<String> errors;

    private QuestionSaveResult(boolean success, String message, Question question, List<String> errors) {
        this.success = success;
        this.message = message;
        this.question = question;
        this.errors = errors != null ? Collections.unmodifiableList(errors) : Collections.emptyList();
    }

    public static QuestionSaveResult success(String message, Question question) {
        return new QuestionSaveResult(true, message, question, null);
    }

    public static QuestionSaveResult failure(List<String> errors) {
        return new QuestionSaveResult(false, null, null, errors);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Optional<Question> getQuestion() {
        return Optional.ofNullable(question);
    }

    public List<String> getErrors() {
        return errors;
    }
}