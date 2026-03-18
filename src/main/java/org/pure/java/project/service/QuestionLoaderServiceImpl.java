package org.pure.java.project.service;

import org.pure.java.project.model.Question;
import org.pure.java.project.model.enums.Difficulty;
import org.pure.java.project.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Author: Artyom Aroyan
 * Date: 16.01.26
 * Time: 00:00:54
 */
public class QuestionLoaderServiceImpl implements QuestionLoaderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionLoaderServiceImpl.class.getName());
    private final QuestionRepository questionRepository;
    private final boolean cacheEnabled;
    private List<Question> cache;

    public QuestionLoaderServiceImpl(QuestionRepository questionRepository, boolean cacheEnabled) {
        this.questionRepository = Objects.requireNonNull(questionRepository);
        this.cacheEnabled = cacheEnabled;
    }

    @Override
    public List<Question> loadAllQuestions() {
        if (cacheEnabled && cache != null) {
            LOGGER.debug("Returning cached questions");
            return new ArrayList<>(cache);
        }

        LOGGER.debug("Loading questions from repository");
        List<Question> questions = questionRepository.findAll();

        if (cacheEnabled) {
            cache = new ArrayList<>(questions);
        }
        return questions;
    }

    @Override
    public List<Question> loadByDifficulty(String difficulty) {
        try {
            Difficulty level = parseDifficulty(difficulty);
            return loadAllQuestions().stream()
                    .filter(q -> q.difficulty() == level)
                    .toList();
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid difficulty level: {}", difficulty);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Question> loadShuffleQuestions(String difficulty, int amount) {
        if (amount < 0 ) {
            throw new IllegalArgumentException("Amount can not be negative");
        }

        List<Question> filtered = loadByDifficulty(difficulty);
        if (filtered.isEmpty() || amount == 0) {
            return Collections.emptyList();
        }

        List<Question> shuffled = new ArrayList<>(filtered);
        Collections.shuffle(shuffled);
        int actualAmount = Math.min(amount, shuffled.size());
        return new ArrayList<>(shuffled.subList(0, actualAmount));
    }

    public void clearCache() {
        cache = null;
        LOGGER.debug("Cache cleared");
    }

    private Difficulty parseDifficulty(String level) {
        return Difficulty.valueOf(level.trim().toUpperCase());
    }
}