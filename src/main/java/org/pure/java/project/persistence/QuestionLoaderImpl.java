package org.pure.java.project.persistence;

import org.pure.java.project.model.Difficulty;
import org.pure.java.project.model.Question;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 16.01.26
 * Time: 00:00:54
 */
public class QuestionLoaderImpl implements QuestionLoaderService {
    private static final String JSON_PATH = "/Users/artyom_aroyan/Software/Java/IntelliJIDEA/Projects/Java/QuizApplication/src/main/resources/Question.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File file = new File(JSON_PATH);

    @Override
    public List<Question> loadAllQuestions() {
        if (!file.exists() || file.length() == 0) {
            return List.of();
        }
        return mapper.readValue(file, new TypeReference<>() {});
    }

    @Override
    public List<Question> loadByDifficulty(String level) {
        try {
            if (!file.exists() || file.length() == 0) {
                return List.of();
            }

            List<Question> questions = mapper.readValue(file, new TypeReference<>() {});

            Difficulty difficulty = Difficulty.valueOf(level.trim().toUpperCase());

            return questions.stream()
                    .filter(q -> q.difficulty() == difficulty)
                    .toList();

        } catch (IllegalArgumentException ex) {
            IO.println("Failed to load questions:" + ex.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Question> loadShuffleQuestions(int amount, String level) {
        return List.of();
    }
}