package org.pure.java.project.service;

import org.pure.java.project.model.entity.Question;

import java.util.List;
import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 25.02.26
 * Time: 14:47:30
 */
public interface QuestionLoaderService {
    Optional<Question> findQuestionById(Long id);
    List<Question> loadAllQuestions();
    List<Question> loadByDifficulty(String difficulty);
    List<Question> loadShuffleQuestions(String difficulty, int amount);
}