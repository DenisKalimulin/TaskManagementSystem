package ru.taskmanager.taskmanagementsystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.LoginRequestDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.LoginResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserRegistrationDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.service.AuthService;

@Tag(name = "Аутентификация", description = "Регистрация и вход пользователя")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        UserResponseDTO response = authService.register(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Вход пользователя в систему")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }
}