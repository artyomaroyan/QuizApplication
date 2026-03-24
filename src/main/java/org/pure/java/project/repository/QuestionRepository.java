package org.pure.java.project.repository;

import org.pure.java.project.model.entity.Question;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 15.03.26
 * Time: 19:37:04
 */
public interface QuestionRepository {
    List<Question> findAll();
    Optional<Question> findById(Long id);
    Question save(Question question) throws IOException;
    void savAll(List<Question> questions) throws IOException;
}