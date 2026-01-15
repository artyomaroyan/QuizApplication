package org.pure.java.project.model;

import java.util.List;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 15.01.26
 * Time: 23:56:36
 */
public record Question(
        UUID id,
        String text,
        List<String> options,
        int correctIndex,
        Difficulty difficulty
) {
}