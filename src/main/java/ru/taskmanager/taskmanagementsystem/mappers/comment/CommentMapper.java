package ru.taskmanager.taskmanagementsystem.mappers.comment;

import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.models.Comment;

import java.util.List;

public interface CommentMapper {
    CommentResponseDTO toCommentResponseDTO(Comment comment);

    Comment toComment(CommentCreateDTO commentCreateDTO);

    List<CommentResponseDTO> toListCommentResponseDTO(List<Comment> comments);
}
