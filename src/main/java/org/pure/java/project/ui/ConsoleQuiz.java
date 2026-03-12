package org.pure.java.project.ui;

import org.pure.java.project.model.Question;
import org.pure.java.project.persistence.QuestionLoaderService;
import org.pure.java.project.service.QuestionService;

import java.util.List;
import java.util.Scanner;

/**
 * Author: Artyom Aroyan
 * Date: 16.01.26
 * Time: 00:02:36
 */
public class ConsoleQuiz {
    private final QuestionService questionService;
    private final QuestionLoaderService questionLoaderService;

    private static final Scanner SCANNER = new Scanner(System.in);

    public ConsoleQuiz(QuestionService questionService, QuestionLoaderService questionLoaderService) {
        this.questionService = questionService;
        this.questionLoaderService = questionLoaderService;
    }

    public void view() {
        IO.println("Choose your service - (Add, Read, Exam):");
        String input = SCANNER.nextLine();

        try {
            ServiceType serviceType = ServiceType.valueOf(input.trim().toUpperCase());

            switch (serviceType) {
                case ADD -> questionService.save();
                case READ -> {
                    IO.println("""
                            Choose read mode:
                            1 - Read all questions:
                            2 - Filter by difficulty
                            3 - Shuffle by amount and difficulty
                            """);
                    String readOption = SCANNER.nextLine().trim();

                    switch (readOption) {
                        case "1" -> {
                            List<Question> questions = questionLoaderService.loadAllQuestions();
                            questions.forEach(IO::println);
                        }

                        case "2" -> {
                            IO.println("Enter difficulty (LOW, MEDIUM, HIGH):");
                            String difficulty = SCANNER.nextLine();
                            List<Question> questions = questionLoaderService.loadByDifficulty(difficulty);
                            questions.forEach(IO::println);
                        }

                        case "3" -> {
                            IO.println("Enter difficulty (LOW, MEDIUM, HIGH):");
                            String difficulty = SCANNER.nextLine();
                            IO.println("Enter number of questions:");
                            int amount = Integer.parseInt(SCANNER.nextLine());
                            List<Question> questions = questionLoaderService.loadShuffleQuestions(amount, difficulty);
                            questions.forEach(IO::println);
                        }
                        default -> IO.println("Unknown read option:");
                    }
                }

                case EXAM -> IO.println("Coming soon!");
                default -> IO.println("Unknown command:");
            }
        } catch (IllegalArgumentException ex) {
            IO.println("Unknown command:" + ex.getMessage());
        }
    }
}

enum ServiceType {
    ADD,
    READ,
    EXAM
}