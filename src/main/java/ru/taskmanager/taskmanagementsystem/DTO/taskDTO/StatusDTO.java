package ru.taskmanager.taskmanagementsystem.DTO.taskDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusDTO {
    private Long taskId;

    @NotBlank(message = "Статус не может быть пустым")
    @Pattern(regexp = "PENDING|IN_PROGRESS|DONE", message = "Недопустимый статус задачи")
    private String status;
}