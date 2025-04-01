package ru.taskmanager.taskmanagementsystem.DTO.commentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private String authorLogin;
    private String text;
    private LocalDateTime createdAt;
    private Long taskId;
}