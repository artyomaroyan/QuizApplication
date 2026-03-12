package org.pure.java.project;

import org.pure.java.project.persistence.QuestionLoaderImpl;
import org.pure.java.project.persistence.QuestionLoaderService;
import org.pure.java.project.service.QuestionService;
import org.pure.java.project.service.QuestionServiceImpl;
import org.pure.java.project.ui.ConsoleQuiz;

/**
 * Author: Artyom Aroyan
 * Date: 15.01.26
 * Time: 23:43:53
 */
public class Main {
    static void main() {
        QuestionService questionService = new QuestionServiceImpl();
        QuestionLoaderService questionLoaderService = new QuestionLoaderImpl();

        ConsoleQuiz consoleQuiz = new ConsoleQuiz(questionService, questionLoaderService);
        consoleQuiz.view();
    }
}
