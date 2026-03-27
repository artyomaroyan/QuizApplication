package org.pure.java.project.service;

import org.pure.java.project.model.entity.Question;
import org.pure.java.project.ui.UserInputHandler;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 16.01.26
 * Time: 00:01:59
 */
public class QuizServiceImpl implements QuizService {
    private final UserInputHandler inputHandler;

    public QuizServiceImpl(UserInputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void startExam(Set<Question> questions) {
        int score = 0;

        for (Question q : questions) {
            IO.println("\n" + q.question());

            for (int i = 0; i < q.answers().size(); i++) {
                IO.println(i + ": " + q.answers().get(i));
            }

            int answer = inputHandler.readAnswer();

            if (answer == q.correctIndex()) {
                IO.println("Correct!");
                score++;
            } else {
                IO.println("Wrong! Correct answer is: " + q.correctIndex());
            }
        }
        IO.println("\nFinal score: " + score + "/" + questions.size());
    }
}