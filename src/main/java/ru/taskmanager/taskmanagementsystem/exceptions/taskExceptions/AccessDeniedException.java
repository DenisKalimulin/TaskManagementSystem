package ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}