package ru.taskmanager.taskmanagementsystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.*;
import ru.taskmanager.taskmanagementsystem.service.AdminService;

@Tag(name = "Админ-панель", description = "Управление задачами и комментариями администраторами")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Создать задачу")
    @PostMapping("/task/create")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        TaskResponseDTO taskResponseDTO = adminService.createTask(taskCreateDTO);
        return ResponseEntity.ok(taskResponseDTO);
    }


    @Operation(summary = "Обновить задачу")
    @PutMapping("/task/update")
    public ResponseEntity<TaskResponseDTO> updateTask(@RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskResponseDTO taskResponseDTO = adminService.updateTask(taskUpdateDTO);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Удалить задачу по ID")
    @DeleteMapping("/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        adminService.deleteTask(id);
        return ResponseEntity.ok("Задача успешно удалена");
    }

    @Operation(summary = "Обновить статус задачи")
    @PatchMapping("/task/update-status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@RequestBody StatusDTO status) {
        TaskResponseDTO taskResponseDTO = adminService.updateStatus(status);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Обновить приоритет задачи")
    @PatchMapping("/task/update-priority")
    public ResponseEntity<TaskResponseDTO> updatePriority(@RequestBody PriorityDTO priority) {
        TaskResponseDTO taskResponseDTO = adminService.updatePriority(priority);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Назначить пользователя исполнителем задачи")
    @PatchMapping("/task/executor/{userId}/{taskId}")
    public ResponseEntity<String> assignUserAsExecutor(@PathVariable Long userId,
                                                       @PathVariable Long taskId) {
        adminService.assignUserAsExecutor(taskId, userId);
        return ResponseEntity.ok("Назначение пользователя на роль исполнителя успешно");
    }

    @Operation(summary = "Оставить комментарий к задаче")
    @PostMapping("/comment/create")
    public ResponseEntity<CommentResponseDTO> createCommentByAdmin(@RequestBody CommentCreateDTO commentCreateDTO) {
        CommentResponseDTO commentResponseDTO = adminService.createComment(commentCreateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @Operation(summary = "Удалить комментарий по ID")
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        adminService.deleteCommentById(id);
        return ResponseEntity.ok("Комментарий успешно удален");
    }
}