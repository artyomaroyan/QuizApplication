package org.pure.java.project.model.enums;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:19:40
 */
public enum MainMenuOption {
    ADD,
    READ,
    EXAM,
    EXIT;

    public static MainMenuOption fromString(String input) {
        return valueOf(input.trim().toUpperCase());
    }
}