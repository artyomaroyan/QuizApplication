package org.pure.java.project.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:38:55
 */
public class UserInputHandler implements AutoCloseable {
    private final Scanner scanner;
    private boolean closed = false;

    public UserInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine() {
        chackedClosed();
        return scanner.nextLine();
    }

    public String readLine(String prompt) {
        IO.println(prompt + " ");
        return readLine();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                String input = readLine(prompt);
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                IO.println("Please enter valid number.!");
            }
        }
    }

    public List<String> readAnswers(String prompt) {
        String input = readLine(prompt);
        return Arrays.stream(input.split("[,\\s]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public int readAnswer() {
        while (true) {
            try {
                return  Integer.parseInt(readLine());
            } catch (NumberFormatException ex) {
                IO.println("Enter valid number");
            }
        }
    }

    @Override
    public void close() {
        if (!closed) {
            scanner.close();
            closed = true;
        }
    }

    private void chackedClosed() {
        if (closed) {
            throw new IllegalStateException("Input handler is closed.!");
        }
    }
}