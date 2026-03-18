package org.pure.java.project.model.dto;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:20:25
 */
public record QuestionInputDto(
        String question,
        List<String> answers,
        int correctIndex,
        String difficulty) {
}