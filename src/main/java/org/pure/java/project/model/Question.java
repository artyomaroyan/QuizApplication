package org.pure.java.project.model;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 15.01.26
 * Time: 23:56:36
 */
public record Question(
        Long id,
        String question,
        List<String> answers,
        int correctIndex,
        Difficulty difficulty) {
}