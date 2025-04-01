package ru.taskmanager.taskmanagementsystem.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserUpdateDTO;
import ru.taskmanager.taskmanagementsystem.configSecurity.SecurityUtils;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UserAlreadyExistsException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UserNotFoundException;
import ru.taskmanager.taskmanagementsystem.mappers.user.UserMapper;
import ru.taskmanager.taskmanagementsystem.models.User;
import ru.taskmanager.taskmanagementsystem.repository.UserRepository;
import ru.taskmanager.taskmanagementsystem.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Transactional
    @Override
    public UserResponseDTO updateUser(UserUpdateDTO userUpdateDTO) {
        String userEmail = SecurityUtils.getCurrentEmail();
        logger.info("Обновление профиля пользователя");

        User user = findUserByEmail(userEmail);

        // Преобразование логина и email в нижний регистр перед обновлением
        if (userUpdateDTO.getLogin() != null) {
            userUpdateDTO.setLogin(userUpdateDTO.getLogin().toLowerCase());
        }
        if (userUpdateDTO.getEmail() != null) {
            userUpdateDTO.setEmail(userUpdateDTO.getEmail().toLowerCase());
        }

        checkUserUniqueness(userUpdateDTO.getLogin(), userUpdateDTO.getEmail());

        // Редактирование данных пользователя из DTO-класса
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getLogin() != null) {
            user.setLogin(userUpdateDTO.getLogin());
        }
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        User savedUser = userRepository.save(user);
        logger.info("Профиль пользователя обновлен");

        return userMapper.toUserResponseDTO(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDTO getCurrentUserProfile() {
        String userEmail = SecurityUtils.getCurrentEmail();
        logger.info("Получение профиля текущего пользователя");
        User user = findUserByEmail(userEmail);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    @Override
    public void deleteCurrentUser() {
        String userEmail = SecurityUtils.getCurrentEmail();
        logger.info("Удаление пользователя");

        User user = findUserByEmail(userEmail);
        userRepository.delete(user);

        SecurityContextHolder.clearContext();

        logger.info("Пользователь удален");
    }


    /**
     * Получение пользователя по логину.
     *
     * @param email электронная почта пользователя.
     * @return найденный пользователь.
     * @throws UserNotFoundException если пользователь с таким email не найден.
     */
    private User findUserByEmail(String email) {
        if (email == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> {
                    logger.warn("Пользователь не найден");
                    return new UserNotFoundException("Пользователь с таким email " + email + " не найден");
                });
    }

    /**
     * Проверка уникальности логина и email.
     * Если логин или email уже заняты, выбрасывает исключение.
     *
     * @param login логин пользователя.
     * @param email email пользователя.
     * @throws UserAlreadyExistsException если логин или email уже заняты.
     */
    private void checkUserUniqueness(String login, String email) {
        if (login != null && userRepository.findByLogin(login.toLowerCase()).isPresent()) {
            logger.warn("Попытка регистрации с уже существующим логином");
            throw new UserAlreadyExistsException("Логин уже используется");
        }
        if (email != null && userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            logger.warn("Попытка регистрации с уже существующим email");
            throw new UserAlreadyExistsException("Email уже используется");
        }
    }
}