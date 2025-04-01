package ru.taskmanager.taskmanagementsystem.mappers.comment;

import org.springframework.stereotype.Component;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.models.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponseDTO toCommentResponseDTO(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponseDTO.builder()
                .authorLogin(comment.getAuthor() != null ? comment.getAuthor().getLogin() : null)
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .taskId(comment.getTask() != null ? comment.getTask().getId() : null)
                .build();
    }

    @Override
    public Comment toComment(CommentCreateDTO commentCreateDTO) {
        if (commentCreateDTO == null) {
            return null;
        }

        return Comment.builder()
                .text(commentCreateDTO.getText())
                .build();
    }

    @Override
    public List<CommentResponseDTO> toListCommentResponseDTO(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return List.of();
        }

        return comments.stream()
                .map(this::toCommentResponseDTO)
                .collect(Collectors.toList());
    }
}
