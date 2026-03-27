# QuizApplication

A Java-based console application for creating, managing, and taking quizzes. Designed for extensibility and ease of use, 
it supports question management, quiz sessions, and persistent storage of questions in JSON format.

## Features
- Console-based interactive UI
- Add, view, and manage quiz questions
- Take quizzes with questions loaded from a JSON file
- Questions categorized by difficulty
- Modular service-based architecture
- Configurable question source and caching
- Logging and error handling

## Project Structure
- `src/main/java/org/pure/java/project/` — Main application source code
  - `Main.java` — Application entry point
  - `ui/ConsoleUI.java` — Console user interface logic
  - `service/` — Core services (QuizService, QuestionService, QuestionLoaderService)
  - `model/` — Data transfer objects, entities, enums, and result types
  - `repository/` — Data access layer
  - `config/` — Application configuration and context
- `src/main/resources/`
  - `application.properties` — Configuration file
  - `Question.json` — Sample questions in JSON format
- `build.gradle` — Gradle build configuration

## Getting Started
### Prerequisites
- Java 17 or higher
- Gradle (or use the provided `gradlew` wrapper)

### Build and Run
1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd QuizApplication
   ```
2. **Build the project:**
   ```sh
   ./gradlew build
   ```
3. **Run the application:**
   ```sh
   ./gradlew run
   ```
   Or run the `Main` class from your IDE.

### Configuration
- Edit `src/main/resources/application.properties` to set the path to your questions file and enable/disable caching:
  ```properties
  questions.file.path=src/main/resources/Question.json
  question.cache.enable=true
  ```

## Usage
- On startup, the application presents a main menu in the console.
- You can add new questions, start a quiz, or view/manage existing questions.
- Questions are loaded from the configured JSON file and can be filtered by difficulty.

## Example Question Format
```json
{
  "id": 1,
  "question": "What is 1 + 1?",
  "answers": ["1", "2", "3", "4"],
  "correctIndex": 1,
  "difficulty": "EASY"
}
```

## Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to your branch (`git push origin feature/your-feature`)
5. Open a pull request

### Coding Guidelines
- Follow standard Java code conventions
- Write clear commit messages
- Add tests for new features where possible

## License
This project is licensed under the MIT License.

## Author
Artyom Aroyan

---
Feel free to open issues or submit pull requests for improvements!

