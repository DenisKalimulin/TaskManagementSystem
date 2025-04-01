package ru.taskmanager.taskmanagementsystem.service;

import ru.taskmanager.taskmanagementsystem.DTO.userDTO.LoginRequestDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.LoginResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserRegistrationDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(UserRegistrationDTO userRegistrationDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}