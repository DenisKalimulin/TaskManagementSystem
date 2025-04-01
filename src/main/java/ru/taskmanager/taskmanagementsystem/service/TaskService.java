package ru.taskmanager.taskmanagementsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.StatusDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskFilterDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskResponseDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO getTaskById(Long id);

    Page<TaskResponseDTO> getTasksByAuthor(Long userId, TaskFilterDTO filter, Pageable pageable);

    Page<TaskResponseDTO> getTasksByExecutor(Long userId, TaskFilterDTO filter, Pageable pageable);

    Page<TaskResponseDTO> getTasks(TaskFilterDTO filter, Pageable pageable);

    TaskResponseDTO updateStatus(StatusDTO status);
}