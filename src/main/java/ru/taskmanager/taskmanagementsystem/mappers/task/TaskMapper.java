package ru.taskmanager.taskmanagementsystem.mappers.task;

import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskResponseDTO;
import ru.taskmanager.taskmanagementsystem.models.Task;

import java.util.List;

public interface TaskMapper {
    TaskResponseDTO toTaskResponseDTO(Task task);

    Task toTask(TaskCreateDTO taskCreateDTO);

    List<TaskResponseDTO> toListTaskResponseDTO(List<Task> tasks);
}