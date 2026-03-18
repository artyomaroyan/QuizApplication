package org.pure.java.project.service;

import org.pure.java.project.model.dto.QuestionInputDto;
import org.pure.java.project.model.result.QuestionSaveResult;

/**
 * Author: Artyom Aroyan
 * Date: 25.02.26
 * Time: 02:06:06
 */
public interface QuestionService {
    QuestionSaveResult save(QuestionInputDto inputDto);
}