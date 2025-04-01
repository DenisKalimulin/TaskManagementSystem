package ru.taskmanager.taskmanagementsystem.mappers.user;

import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserRegistrationDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.models.User;

import java.util.List;

public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);

    User toUser(UserRegistrationDTO userRegistrationDTO);

    List<UserResponseDTO> toListUserResponseDTO(List<User> users);
}