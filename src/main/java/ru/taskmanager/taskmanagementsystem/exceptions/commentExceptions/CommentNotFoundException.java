package ru.taskmanager.taskmanagementsystem.exceptions.commentExceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}