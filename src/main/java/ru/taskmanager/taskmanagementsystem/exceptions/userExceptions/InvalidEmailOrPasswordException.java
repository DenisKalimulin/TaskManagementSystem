package ru.taskmanager.taskmanagementsystem.exceptions.userExceptions;

public class InvalidEmailOrPasswordException extends RuntimeException {
    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }
}