package org.pure.java.project.service;

import org.pure.java.project.model.Difficulty;
import org.pure.java.project.model.Question;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Artyom Aroyan
 * Date: 16.01.26
 * Time: 00:00:54
 */
public class QuestionLoaderImpl implements QuestionLoaderService {
    private static final Logger LOGGER = Logger.getLogger(QuestionLoaderImpl.class.getName());
    private static final String JSON_PATH = "/Users/artyom_aroyan/Software/Java/IntelliJIDEA/Projects/Java/QuizApplication/src/main/resources/Question.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final File QUESTION_FILE = new File(JSON_PATH);

    private List<Question> cache;

    @Override
    public List<Question> loadAllQuestions() {
        return readQuestion();
    }

    @Override
    public List<Question> loadByDifficulty(String level) {
            Difficulty difficulty = Difficulty.valueOf(level.trim().toUpperCase());
            return readQuestion().stream()
                    .filter(q -> q.difficulty() == difficulty)
                    .toList();
    }

    @Override
    public List<Question> loadShuffleQuestions(String level, int amount) {
        List<Question> filtered = loadByDifficulty(level);
        if (filtered.isEmpty()) return Collections.emptyList();
        List<Question> shuffled = new ArrayList<>(filtered);
        Collections.shuffle(shuffled);
        return shuffled.stream()
                .limit(amount)
                .toList();
    }

    private List<Question> readQuestion() {
        if (cache != null) {
            return cache;
        }

        if (!isFileReadable()) {
            LOGGER.warning("Question file not found or empty.");
            cache = List.of();
            return cache;
        }

        try {
            cache = List.of(OBJECT_MAPPER.readValue(QUESTION_FILE, new TypeReference<>() {}));
            return cache;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to read questions: {}", ex.getMessage());
            cache = List.of();
            return cache;
        }
    }

    private boolean isFileReadable() {
        if (!QUESTION_FILE.exists()) {
            LOGGER.log(Level.WARNING, "Question file does not exist: {}", QUESTION_FILE.getAbsolutePath());
            return false;
        }

        if (!QUESTION_FILE.canRead()) {
            LOGGER.log(Level.WARNING, "Question file exists but cannot be read: {}", QUESTION_FILE.getAbsolutePath());
            return false;
        }

        if (QUESTION_FILE.length() == 0) {
            LOGGER.log(Level.WARNING, "Question file is empty: {}", QUESTION_FILE.getAbsolutePath());
            return false;
        }
        return true;
    }
}