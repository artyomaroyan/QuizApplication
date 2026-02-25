package org.pure.java.project;

import org.pure.java.project.model.Question;
import org.pure.java.project.persistence.QuestionLoaderImpl;
import org.pure.java.project.persistence.QuestionLoaderService;
import org.pure.java.project.service.QuestionService;
import org.pure.java.project.service.QuestionServiceImpl;

import java.util.List;
import java.util.Scanner;

/**
 * Author: Artyom Aroyan
 * Date: 15.01.26
 * Time: 23:43:53
 */
public class Main {
    static void main() {

        Scanner scanner = new Scanner(System.in);
        IO.println("Choose your service - (Add, Read, Exam):");
        String input = scanner.nextLine();

        QuestionService questionService = new QuestionServiceImpl();
        QuestionLoaderService loaderService = new QuestionLoaderImpl();

        switch (input.trim().toUpperCase()) {
            case "ADD" -> questionService.save();
            case "READ" -> {
                IO.println("""
                        Choose read mode:
                        1 - Read all questions:
                        2 - Filter by difficulty
                        3 - Shuffle by amount and difficulty
                        """);

                String readOption = scanner.nextLine().trim();

                switch (readOption) {
                    case "1" -> {
                        List<Question> questions = loaderService.loadAllQuestions();
                        questions.forEach(IO::println);
                    }

                    case "2" -> {
                        IO.println("Enter difficulty (LOW, MEDIUM, HIGH):");
                        String difficulty = scanner.nextLine();
                        List<Question> questions = loaderService.loadByDifficulty(difficulty);
                        questions.forEach(IO::println);
                    }
                    case "3" -> {
                        IO.println("Enter difficulty (LOW, MEDIUM, HIGH):");
                        String difficulty = scanner.nextLine();

                        IO.println("Enter number of questions:");
                        int amount = Integer.parseInt(scanner.nextLine());

                        List<Question> questions = loaderService.loadShuffleQuestions(amount, difficulty);
                        questions.forEach(IO::println);
                    }
                    default -> IO.println("Unknown read option:");
                }
            }

            case "EXAM" -> IO.println("coming soon!");
            default -> IO.println("Unknown command:");
        }
    }
}
