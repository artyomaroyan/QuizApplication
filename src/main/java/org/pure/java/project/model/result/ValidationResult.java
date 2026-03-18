package org.pure.java.project.model.result;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:20:42
 */
public record ValidationResult(boolean isValid, List<String> errors) {
}