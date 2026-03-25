package org.pure.java.project.service;

import org.pure.java.project.model.entity.Question;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 16.01.26
 * Time: 00:01:40
 */
public interface QuizService {
    void startExam(List<Question> questions);
}