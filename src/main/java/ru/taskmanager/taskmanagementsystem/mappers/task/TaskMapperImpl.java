package ru.taskmanager.taskmanagementsystem.mappers.task;


import org.springframework.stereotype.Component;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskResponseDTO;
import ru.taskmanager.taskmanagementsystem.enums.TaskPriority;
import ru.taskmanager.taskmanagementsystem.models.Task;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public TaskResponseDTO toTaskResponseDTO(Task task) {
        if (task == null) {
            return null;
        }

        return TaskResponseDTO.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .authorLogin(task.getAuthor() != null ? task.getAuthor().getLogin() : null)
                .build();
    }

    @Override
    public Task toTask(TaskCreateDTO taskCreateDTO) {
        if (taskCreateDTO == null) {
            return null;
        }

        return Task.builder()
                .header(taskCreateDTO.getHeader())
                .description(taskCreateDTO.getDescription())
                .priority(TaskPriority.valueOf(taskCreateDTO.getPriority()))
                .build();
    }

    @Override
    public List<TaskResponseDTO> toListTaskResponseDTO(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }

        return tasks.stream()
                .map(this::toTaskResponseDTO)
                .collect(Collectors.toList());
    }
}
