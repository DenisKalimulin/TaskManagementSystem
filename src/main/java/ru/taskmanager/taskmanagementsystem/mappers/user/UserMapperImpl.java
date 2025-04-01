package ru.taskmanager.taskmanagementsystem.mappers.user;

import org.springframework.stereotype.Component;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserRegistrationDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .build();
    }

    @Override
    public User toUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRegistrationDTO == null) {
            return null;
        }

        return User.builder()
                .login(userRegistrationDTO.getLogin())
                .email(userRegistrationDTO.getEmail())
                .password(userRegistrationDTO.getPassword())
                .build();
    }

    @Override
    public List<UserResponseDTO> toListUserResponseDTO(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        return users.stream()
                .map(this::toUserResponseDTO)
                .collect(Collectors.toList());
    }
}