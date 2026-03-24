package org.pure.java.project.config;

import org.pure.java.project.adapter.QuestionRepositoryAdapter;
import org.pure.java.project.model.validator.InputValidator;
import org.pure.java.project.repository.QuestionRepository;
import org.pure.java.project.service.QuestionLoaderService;
import org.pure.java.project.service.QuestionLoaderServiceImpl;
import org.pure.java.project.service.QuestionService;
import org.pure.java.project.service.QuestionServiceImpl;
import org.pure.java.project.ui.ConsoleUI;
import org.pure.java.project.ui.UserInputHandler;
import tools.jackson.databind.ObjectMapper;

/**
 * Author: Artyom Aroyan
 * Date: 25.03.26
 * Time: 01:39:31
 */
public record ApplicationContext(
        ConsoleUI consoleUI,
        QuestionService questionService,
        QuestionRepository questionRepository,
        QuestionLoaderService loaderService,
        InputValidator inputValidator) {

    public static ApplicationContext initialize(AppConfiguration configuration) {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionRepository repository = new QuestionRepositoryAdapter(objectMapper, configuration.getQuestionsFilePath());
        QuestionLoaderService loader = new QuestionLoaderServiceImpl(repository, configuration.isCacheEnabled());
        InputValidator validator = new InputValidator();
        QuestionService service = new QuestionServiceImpl(loader, repository, validator);
        ConsoleUI console = new ConsoleUI(service, loader, new UserInputHandler());

        return new ApplicationContext(console, service, repository, loader, validator);
    }
}