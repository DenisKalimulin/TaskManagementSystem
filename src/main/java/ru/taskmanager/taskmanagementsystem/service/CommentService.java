package ru.taskmanager.taskmanagementsystem.service;

import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO getCommentById(Long id);

    List<CommentResponseDTO> getCommentsByTask(Long taskId);

    CommentResponseDTO createCommentByExecutor(CommentCreateDTO commentCreateDTO);

}