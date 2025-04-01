package ru.taskmanager.taskmanagementsystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.service.CommentService;

import java.util.List;

@Tag(name = "Комментарии", description = "Управление комментариями к задачам")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Получить комментарий по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long id) {
        CommentResponseDTO commentResponseDTO = commentService.getCommentById(id);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @Operation(summary = "Получить все комментарии к задаче по её ID")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTask(@PathVariable Long taskId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByTask(taskId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Создать комментарий (только для исполнителя задачи)")
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createCommentByExecutor(@Valid @RequestBody CommentCreateDTO commentCreateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createCommentByExecutor(commentCreateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }
}