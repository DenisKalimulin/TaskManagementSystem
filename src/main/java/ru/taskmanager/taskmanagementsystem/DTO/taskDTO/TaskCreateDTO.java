package ru.taskmanager.taskmanagementsystem.DTO.taskDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCreateDTO {
    @NotBlank(message = "Заголовок задачи не может быть пустым")
    @Size(min = 3, max = 255, message = "Заголовок должен содержать от 3 до 255 символов")
    private String header;

    @NotBlank(message = "Описание задачи не может быть пустым")
    private String description;

    @NotBlank(message = "Приоритет задачи обязателен")
    private String priority;

    @NotBlank(message = "Логин автора задачи обязателен")
    private String authorLogin;
}