package ru.taskmanager.taskmanagementsystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.StatusDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskFilterDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskResponseDTO;
import ru.taskmanager.taskmanagementsystem.enums.TaskPriority;
import ru.taskmanager.taskmanagementsystem.enums.TaskStatus;
import ru.taskmanager.taskmanagementsystem.service.TaskService;

@Tag(name = "Задачи", description = "Операции с задачами")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Получить задачу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @Operation(summary = "Получить все задачи с фильтрацией и пагинацией")
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        TaskFilterDTO filter = new TaskFilterDTO(status, priority);
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskResponseDTO> tasks = taskService.getTasks(filter, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Получить задачи по автору с фильтрацией и пагинацией")
    @GetMapping("/author/{id}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByAuthor(
            @PathVariable Long id,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        TaskFilterDTO filter = new TaskFilterDTO(status, priority);
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskResponseDTO> tasks = taskService.getTasksByAuthor(id, filter, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Получить задачи по исполнителю с фильтрацией и пагинацией")
    @GetMapping("/executor/{id}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByExecutor(
            @PathVariable Long id,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        TaskFilterDTO filter = new TaskFilterDTO(status, priority);
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskResponseDTO> tasks = taskService.getTasksByExecutor(id, filter, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Обновить статус задачи")
    @PatchMapping("/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@Valid @RequestBody StatusDTO status) {
        TaskResponseDTO taskResponseDTO = taskService.updateStatus(status);
        return ResponseEntity.ok(taskResponseDTO);
    }
}