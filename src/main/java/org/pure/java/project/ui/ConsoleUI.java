package org.pure.java.project.ui;

import org.pure.java.project.model.dto.QuestionInputDto;
import org.pure.java.project.model.entity.Question;
import org.pure.java.project.model.enums.MainMenuOption;
import org.pure.java.project.model.result.QuestionSaveResult;
import org.pure.java.project.service.QuestionLoaderService;
import org.pure.java.project.service.QuestionService;
import org.pure.java.project.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:36:57
 */
public class ConsoleUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleUI.class);
    private final QuizService quizService;
    private final QuestionService questionService;
    private final QuestionLoaderService loaderService;
    private final UserInputHandler userInputHandler;

    private boolean running = true;

    public ConsoleUI(QuizService quizService, QuestionService questionService, QuestionLoaderService loaderService, UserInputHandler userInputHandler) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.loaderService = loaderService;
        this.userInputHandler = userInputHandler;
    }

    public void start() {
        printWelcome();

        while (running) {
            try {
                printMainMenu();
                String choice = userInputHandler.readLine();
                handleMainChoice(choice);
            } catch (Exception ex) {
                LOGGER.error("Unexpected error", ex);
                IO.println("An error occurred: " + ex.getMessage());
            }
        }
        userInputHandler.close();
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
            String question = userInputHandler.readLine("Enter question:");
            List<String> answers = userInputHandler.readAnswers("Enter answers (separate with spaces or commas):");
            int correctIndex = userInputHandler.readInt("Enter correct answer index (0-based):");
            String difficulty = userInputHandler.readLine("Enter difficulty (EASY, MEDIUM, HIGH):");
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

        String choice = userInputHandler.readLine();

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
                .append("]");
        sb.append("\n[Question: ")
                .append(question.question())
                .append("]")
                .append(" (Difficulty: ")
                .append(question.difficulty())
                .append(")\n");

        List<String> answers = question.answers();
        for (int i = 0; i < answers.size(); i++) {
            sb.append("   ")
                    .append(i)
                    .append(": ")
                    .append(answers.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    private void handleReadByDifficulty() {
        String difficulty = userInputHandler.readLine("Enter difficulty (EASY, MEDIUM, HIGH):");
        displayQuestions(loaderService.loadByDifficulty(difficulty));
    }

    private void handleReadShuffled() {
        String difficulty = userInputHandler.readLine("Enter difficulty (EASY, MEDIUM, HIGH):");
        int amount = userInputHandler.readInt("Enter number of questions:");
        displayQuestions(loaderService.loadShuffleQuestions(difficulty, amount));
    }

    private void handleExam() {
        String difficulty = userInputHandler.readLine("Enter difficulty (EASY, MEDIUM, HIGH):");
        int amount = userInputHandler.readInt("Enter number of questions:");
        Set<Question> questions = new HashSet<>(loaderService.loadShuffleQuestions(difficulty, amount));
        quizService.startExam(questions);
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