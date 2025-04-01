package ru.taskmanager.taskmanagementsystem.service;

import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.*;

public interface AdminService {
    TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO);

    TaskResponseDTO updateTask(TaskUpdateDTO taskUpdateDTO);

    void deleteTask(Long id);

    TaskResponseDTO updateStatus(StatusDTO status);

    TaskResponseDTO updatePriority(PriorityDTO priority);

    void assignUserAsExecutor(Long taskId, Long userId);

    CommentResponseDTO createComment(CommentCreateDTO commentCreateDTO);

    void deleteCommentById(Long commentId);
}