package ru.taskmanager.taskmanagementsystem.DTO.taskDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateDTO {
    private Long id;

    @Size(min = 3, max = 255, message = "Заголовок должен содержать от 3 до 255 символов")
    private String header;

    private String description;
}