package org.pure.java.project.service;

import org.pure.java.project.model.entity.Question;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 25.02.26
 * Time: 14:47:30
 */
public interface QuestionLoaderService {
    List<Question> loadAllQuestions();
    List<Question> loadByDifficulty(String difficulty);
    List<Question> loadShuffleQuestions(String difficulty, int amount);
}