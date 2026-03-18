package org.pure.java.project;

import org.pure.java.project.model.InputValidator;
import org.pure.java.project.repository.QuestionRepository;
import org.pure.java.project.repository.QuestionRepositoryAdapter;
import org.pure.java.project.service.QuestionLoaderServiceImpl;
import org.pure.java.project.service.QuestionLoaderService;
import org.pure.java.project.service.QuestionService;
import org.pure.java.project.service.QuestionServiceImpl;
import org.pure.java.project.ui.ConsoleUI;
import org.pure.java.project.ui.UserInputHandler;
import tools.jackson.databind.ObjectMapper;

/**
 * Author: Artyom Aroyan
 * Date: 15.03.26
 * Time: 00:42:24
 */
public class ApplicationContext {
    private final ConsoleUI consoleUI;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final QuestionLoaderService loaderService;
    private final InputValidator inputValidator;

    public ApplicationContext(Builder builder) {
        this.questionRepository = builder.questionRepository;
        this.loaderService = builder.loaderService;
        this.questionService = builder.questionService;
        this.inputValidator = builder.inputValidator;
        this.consoleUI = builder.consoleUI;
    }

    public static ApplicationContext initialize(AppConfiguration configuration) {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionRepository repository = new QuestionRepositoryAdapter(objectMapper, configuration.getQuestionsFilePath());
        QuestionLoaderService loader = new QuestionLoaderServiceImpl(repository, configuration.isCacheEnabled());
        InputValidator validator = new InputValidator();
        QuestionService service = new QuestionServiceImpl(loader, repository, validator);
        ConsoleUI console = new ConsoleUI(service, loader, new UserInputHandler());

        return new Builder()
                .questionRepository(repository)
                .loaderService(loader)
                .questionService(service)
                .inputValidator(validator)
                .consoleUI(console)
                .build();

    }

    public ConsoleUI getConsoleUI() {
        return consoleUI;
    }

    public static class Builder {
        private ConsoleUI consoleUI;
        private QuestionService questionService;
        private QuestionRepository questionRepository;
        private QuestionLoaderService loaderService;
        private InputValidator inputValidator;

        public Builder consoleUI(ConsoleUI consoleUI) {
            this.consoleUI = consoleUI;
            return this;
        }

        public Builder questionService(QuestionService questionService) {
            this.questionService = questionService;
            return this;
        }

        public Builder questionRepository(QuestionRepository questionRepository) {
            this.questionRepository = questionRepository;
            return this;
        }

        public Builder loaderService(QuestionLoaderService loaderService) {
            this.loaderService = loaderService;
            return this;
        }

        public Builder inputValidator(InputValidator inputValidator) {
            this.inputValidator = inputValidator;
            return this;
        }

        public ApplicationContext build() {
            return new ApplicationContext(this);
        }
    }
}