package org.pure.java.project.persistence;

import org.pure.java.project.model.Question;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 25.02.26
 * Time: 14:47:30
 */
public interface QuestionLoaderService {
    List<Question> loadAllQuestions();
    List<Question> loadByDifficulty(String level);
    List<Question> loadShuffleQuestions(int amount, String level);
}