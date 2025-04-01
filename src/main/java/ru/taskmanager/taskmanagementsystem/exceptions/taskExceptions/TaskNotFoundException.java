package ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}