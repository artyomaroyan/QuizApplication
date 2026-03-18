package org.pure.java.project.ui;

import org.pure.java.project.model.enums.MainMenuOption;
import org.pure.java.project.model.Question;
import org.pure.java.project.model.dto.QuestionInputDto;
import org.pure.java.project.model.result.QuestionSaveResult;
import org.pure.java.project.service.QuestionLoaderService;
import org.pure.java.project.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:36:57
 */
public class ConsoleUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleUI.class);
    private final QuestionService questionService;
    private final QuestionLoaderService loaderService;
    private final UserInputHandler inputHandler;

    private boolean running = true;

    public ConsoleUI(QuestionService questionService, QuestionLoaderService loaderService, UserInputHandler inputHandler) {
        this.questionService = Objects.requireNonNull(questionService);
        this.loaderService = Objects.requireNonNull(loaderService);
        this.inputHandler = Objects.requireNonNull(inputHandler);
    }

    public void start() {
        printWelcome();

        while (running) {
            try {
                printMainMenu();
                String choice = inputHandler.readLine();
                handleMainChoice(choice);
            } catch (Exception ex) {
                LOGGER.error("Unexpected error", ex);
                IO.println("An error occurred: " + ex.getMessage());
            }
        }
        inputHandler.close();
        printGoodBye();
    }

    private void handleMainChoice(String choice) {
        try {
            MainMenuOption option = MainMenuOption.fromString(choice);

            switch (option) {
                case ADD -> handleAddQuestion();
                case READ -> handleReadQuestions();
                case EXAM -> handleExam();
                case EXIT -> running = false;
            }
        } catch (IllegalArgumentException ex) {
            IO.println("Invalid option, Please try again.");
        }
    }

    private void handleAddQuestion() {
        IO.println("\n--- Add New Question ---");

        try {
            String question = inputHandler.readLine("Enter question:");
            List<String> answers = inputHandler.readAnswers("Enter answers (separate with spaces or commas):");
            int correctIndex = inputHandler.readInt("Enter correct answer index (0-based):");
            String difficulty = inputHandler.readLine("Enter difficulty (EASY, MEDIUM, HARD):");
            QuestionInputDto inputDto = new QuestionInputDto(question, answers, correctIndex, difficulty);
            QuestionSaveResult result = questionService.save(inputDto);

            if (result.isSuccess()) {
                IO.println("Success: " + result.getMessage());
            } else {
                IO.println("Failed to add question:");
                result.getErrors().forEach(error -> IO.println(" - " + error));
            }

        } catch (Exception ex) {
            IO.println("Error adding question: " + ex.getMessage());
        }
    }

    private void handleReadQuestions() {
        IO.println("\n--- Read Questions ---");
        printReadMenu();

        String choice = inputHandler.readLine();

        try {
            switch (choice) {
                case "1" -> displayQuestions(loaderService.loadAllQuestions());
                case "2" -> handleReadByDifficulty();
                case "3" -> handleReadShuffled();
                default -> IO.println("Invalid option");
            }
        } catch (Exception ex) {
            IO.println("Error reading questions: " + ex.getMessage());
        }
    }

    private void displayQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            IO.println("No questions found.");
            return;
        }

        IO.println("\nFound " + questions.size() + " question(s):");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            IO.println("\n" + (i + 1) + ", " + formatQuestions(question));
        }
    }

    private String formatQuestions(Question question) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ID: ")
                .append(question.id())
                .append("] ");
        sb.append(question.question())
                .append(" (Difficulty: ")
                .append(question.difficulty())
                .append(")\n");

        List<String> answers = question.answers();
        for (int i = 0; i < answers.size(); i++) {
            sb.append("   ")
                    .append(i)
                    .append(": ")
                    .append(answers.get(i));
            if (i == question.correctIndex()) {
                sb.append("OK");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void handleReadByDifficulty() {
        String difficulty = inputHandler.readLine("Enter difficulty (EASY, MEDIUM, HARD):");
        displayQuestions(loaderService.loadByDifficulty(difficulty));
    }

    private void handleReadShuffled() {
        String difficulty = inputHandler.readLine("Enter difficulty (EASY, MEDIUM, HARD):");
        int amount = inputHandler.readInt("Enter number of questions:");
        displayQuestions(loaderService.loadShuffleQuestions(difficulty, amount));
    }

    private void handleExam() {
        IO.println("\n---Exam Mode (Coming Soon!) ---");
    }

    private void printWelcome() {
        IO.println("\n" + "=".repeat(50));
        IO.println("Welcome to Quiz Application");
        IO.println("=".repeat(50));
    }

    private void printMainMenu() {
        IO.println("\n" + "-".repeat(30));
        IO.println("Main Menu:");
        IO.println("Add - Add new question");
        IO.println("Read - View questions");
        IO.println("Exam - Take an exam");
        IO.println("Exit - Exit application");
        IO.println("\nChoose option: ");
    }

    private void printReadMenu() {
        IO.println("Read options:");
        IO.println("1 - Show all questions");
        IO.println("2 - Filter bu difficulty");
        IO.println("3 - Shuffle by difficulty & amount");
        IO.println("Choose: ");
    }

    private void printGoodBye() {
        IO.println("\nThank you for using Quiz Application. Goodbye!");
    }
}