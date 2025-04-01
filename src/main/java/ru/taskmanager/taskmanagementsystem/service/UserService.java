package ru.taskmanager.taskmanagementsystem.service;

import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserUpdateDTO;

public interface UserService {
    UserResponseDTO updateUser(UserUpdateDTO userUpdateDTO);

    UserResponseDTO getCurrentUserProfile();

    void deleteCurrentUser();
}