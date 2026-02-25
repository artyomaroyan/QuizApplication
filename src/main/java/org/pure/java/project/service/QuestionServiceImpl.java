package org.pure.java.project.service;

import org.pure.java.project.model.Difficulty;
import org.pure.java.project.model.Question;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

/**
 * Author: Artyom Aroyan
 * Date: 25.02.26
 * Time: 02:06:23
 */
public class QuestionServiceImpl implements QuestionService {
    private static final String JSON_PATH = "/Users/artyom_aroyan/Software/Java/IntelliJIDEA/Projects/Java/QuizApplication/src/main/resources/Question.json";

    @Override
    public String save() {

        Scanner scanner = new Scanner(System.in);
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(JSON_PATH);

        List<Question> questions;
        if (file.exists() && file.length() > 0) {
            questions = mapper.readValue(file, new TypeReference<>() {});
        } else {
            questions = new ArrayList<>();
        }

        IO.println("Enter question:");
        String inputQuestion = scanner.nextLine();

        IO.println("Enter answers (separated with spaces):");
        String answer = scanner.nextLine();
        List<String> answers = Arrays.stream(answer.split("\\s+"))
                .map(String::trim)
                .toList();

        IO.println("Enter correct answer (0-based):");
        int correctIndex = scanner.nextInt();
        scanner.nextLine();

        IO.println("Enter difficulty (LOW, MEDIUM, HIGH):");
        String difficulty = scanner.nextLine();

        Question newQuestion = new Question(
                1L,
                inputQuestion,
                answers,
                correctIndex,
                Difficulty.valueOf(difficulty.toUpperCase())
        );

        questions.add(newQuestion);

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, questions);

        return "Question added successfully!";
    }
}