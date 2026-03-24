package org.pure.java.project.service;

import org.pure.java.project.model.validator.InputValidator;
import org.pure.java.project.model.entity.Question;
import org.pure.java.project.model.dto.QuestionInputDto;
import org.pure.java.project.model.enums.Difficulty;
import org.pure.java.project.model.result.QuestionSaveResult;
import org.pure.java.project.model.result.ValidationResult;
import org.pure.java.project.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 25.02.26
 * Time: 02:06:23
 */
public class QuestionServiceImpl implements QuestionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImpl.class);
    private final QuestionLoaderService questionLoaderService;
    private final QuestionRepository questionRepository;
    private final InputValidator inputValidator;

    public QuestionServiceImpl(QuestionLoaderService questionLoaderService, QuestionRepository questionRepository, InputValidator inputValidator) {
        this.questionLoaderService = Objects.requireNonNull(questionLoaderService);
        this.questionRepository = Objects.requireNonNull(questionRepository);
        this.inputValidator = Objects.requireNonNull(inputValidator);
    }

    @Override
    public QuestionSaveResult save(QuestionInputDto inputDto) {
        LOGGER.info("Attempting to save question");

        ValidationResult validation = inputValidator.validate(inputDto);
        if (!validation.isValid()) {
            LOGGER.warn("Invalid question input: {}", validation.getAllErrorsAsString());
            return QuestionSaveResult.failure(validation.errors());
        }

        try {
            List<Question> existingQuestions = questionLoaderService.loadAllQuestions();
            boolean isDuplicate = existingQuestions.stream()
                    .anyMatch( q -> q.question().equalsIgnoreCase(inputDto.question().trim()));

            if (isDuplicate) {
                String error = "Question already exists: " + inputDto.question();
                LOGGER.warn(error);
                return QuestionSaveResult.failure(List.of(error));
            }

            Question question = createQuestion(inputDto);
            Question saved = questionRepository.save(question);

            if (questionLoaderService instanceof QuestionLoaderServiceImpl) {
                ((QuestionLoaderServiceImpl) questionLoaderService).clearCache();
            }

            LOGGER.info("Question saved successfully with ID: {}", saved.id());
            return QuestionSaveResult.success("Question added successfully!", saved);
        } catch (IOException ex) {
            LOGGER.error("Failed to save question", ex);
            return QuestionSaveResult.failure(List.of("Failed to save: " + ex.getMessage()));
        }
    }

    private Question createQuestion(QuestionInputDto inputDto) {
        Long nextId = generateNextId();
        Difficulty difficulty = Difficulty.valueOf(inputDto.difficulty().toUpperCase());

        return new Question(
                nextId,
                inputDto.question(),
                new ArrayList<>(inputDto.answers()),
                inputDto.correctIndex(),
                difficulty
        );
    }

    private Long generateNextId() {
        return questionLoaderService.loadAllQuestions().stream()
                .mapToLong(Question::id)
                .max()
                .orElse(0L) + 1;
    }
}