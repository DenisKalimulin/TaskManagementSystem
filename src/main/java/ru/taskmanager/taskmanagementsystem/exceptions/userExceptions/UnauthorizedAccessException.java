package ru.taskmanager.taskmanagementsystem.exceptions.userExceptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}