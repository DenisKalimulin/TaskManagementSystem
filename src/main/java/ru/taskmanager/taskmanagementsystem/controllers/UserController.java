package ru.taskmanager.taskmanagementsystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserUpdateDTO;
import ru.taskmanager.taskmanagementsystem.service.UserService;


@Tag(name = "Пользователи", description = "Операции с текущим пользователем")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Обновить текущего пользователя")
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {

        UserResponseDTO updatedUser = userService.updateUser(userUpdateDTO);
        logger.info("Профиль пользователя был успешно обновлен");

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Получить текущего пользователя")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        logger.info("Запрос на получение профиля");
        UserResponseDTO userResponseDTO = userService.getCurrentUserProfile();
        return ResponseEntity.ok(userResponseDTO);
    }


    @Operation(summary = "Удалить текущего пользователя")
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser() {
        userService.deleteCurrentUser();
        logger.info("Пользователь был успешно удален из системы");
        return ResponseEntity.ok("Профиль удален");
    }
}