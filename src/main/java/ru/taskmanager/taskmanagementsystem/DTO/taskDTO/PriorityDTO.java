package ru.taskmanager.taskmanagementsystem.DTO.taskDTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriorityDTO {
    private Long taskId;

    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "Недопустимый приоритет задачи")
    private String priority;
}