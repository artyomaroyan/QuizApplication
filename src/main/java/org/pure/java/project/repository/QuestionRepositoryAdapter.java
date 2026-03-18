package org.pure.java.project.repository;

import org.pure.java.project.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 15.03.26
 * Time: 00:43:45
 */
public class QuestionRepositoryAdapter implements QuestionRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionRepositoryAdapter.class);
    private final ObjectMapper objectMapper;
    private final File questionFile;

    public QuestionRepositoryAdapter(ObjectMapper objectMapper, String filePath) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.questionFile = new File(Objects.requireNonNull(filePath));
        ensureDirectoryExists();
    }

    @Override
    public List<Question> findAll() {
        if (!isFileReadable()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(questionFile, new TypeReference<>() {});
        } catch (Exception ex) {
            LOGGER.error("Failed to read questions from file", ex);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Question> findById(Long id) {
        return findAll().stream()
                .filter(q -> q.id().equals(id))
                .findFirst();
    }

    @Override
    public Question save(Question question) {
        List<Question> questions = findAll();

        Optional<Question> existing = questions.stream()
                .filter(q -> q.id().equals(question.id()))
                .findFirst();

        if (existing.isPresent()) {
            int index = questions.indexOf(existing.get());
            questions.set(index, question);
        } else {
            questions.add(question);
        }
        writeAll(questions);
        return question;
    }

    @Override
    public void savAll(List<Question> questions) {
        writeAll(new ArrayList<>(questions));
    }

    private void ensureDirectoryExists() {
        File parentDir = questionFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                LOGGER.info("Created directory: {}", parentDir.getAbsolutePath());
            }
        }
    }

    private void writeAll(List<Question> questions) {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(questionFile, questions);
        LOGGER.debug("Saved {} questions to file", questions.size());
    }

    private boolean isFileReadable() {
        if (!questionFile.exists()) {
            LOGGER.debug("Question file does not exist yet: {}", questionFile.getAbsolutePath());
            return false;
        }
        return true;
    }
}