package org.pure.java.project;

import org.pure.java.project.model.Difficulty;
import org.pure.java.project.model.Question;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 15.01.26
 * Time: 23:43:53
 */
public class Main {
    private static final String PATH = "/Users/artyom_aroyan/Software/Java/IntelliJIDEA/Projects/Java/QuizApplication/src/main/resources/Questions.txt";

    static void main() {
        try (Scanner scanner = new Scanner(System.in)) {

            IO.println("Enter question");
            String question = scanner.nextLine();

            IO.println("Enter answers (separated with spaces)");
            String answer = scanner.nextLine();
            List<String> answers = Arrays.stream(answer.split("\\s+"))
                    .map(String::trim)
                    .toList();

            IO.println("Enter correct answer (0-based)");
            int correctIndex = scanner.nextInt();
            scanner.nextLine();

            IO.println("Enter difficulty (LOW, MEDIUM, HIGH)");
            String difficulty = scanner.nextLine();

            Question newQuestion = new Question(
                    UUID.randomUUID(),
                    question,
                    answers,
                    correctIndex,
                    Difficulty.valueOf(difficulty.toUpperCase())
            );

            try (FileWriter fileWriter = new FileWriter(PATH, true)) {
                fileWriter.write(newQuestion.toString());
                fileWriter.write(System.lineSeparator());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
