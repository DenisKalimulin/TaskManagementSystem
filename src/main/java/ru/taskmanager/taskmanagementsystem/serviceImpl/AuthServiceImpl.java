package ru.taskmanager.taskmanagementsystem.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.LoginRequestDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.LoginResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserRegistrationDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.configSecurity.CustomUserDetails;
import ru.taskmanager.taskmanagementsystem.configSecurity.JwtUtils;
import ru.taskmanager.taskmanagementsystem.enums.RoleType;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.InvalidEmailOrPasswordException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UserAlreadyExistsException;
import ru.taskmanager.taskmanagementsystem.mappers.user.UserMapper;
import ru.taskmanager.taskmanagementsystem.models.Role;
import ru.taskmanager.taskmanagementsystem.models.User;
import ru.taskmanager.taskmanagementsystem.repository.RoleRepository;
import ru.taskmanager.taskmanagementsystem.repository.UserRepository;
import ru.taskmanager.taskmanagementsystem.service.AuthService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;


    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public UserResponseDTO register(UserRegistrationDTO userRegistrationDTO) {
        logger.info("Регистрация нового пользователя");
        checkUserUniqueness(userRegistrationDTO.getLogin(), userRegistrationDTO.getEmail());

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Роль USER_ROLE не найдена"));

        User user = User.builder()
                .login(userRegistrationDTO.getLogin().toLowerCase())
                .email(userRegistrationDTO.getEmail().toLowerCase())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);
        logger.info("Пользователь зарегистрирован");

        return userMapper.toUserResponseDTO(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        logger.info("Попытка входа пользователя");

        try {
            // Аутентификация по email и паролю
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail().toLowerCase(),
                            loginRequestDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            logger.warn("Ошибка аутентификации: неверный логин или пароль");
            throw new InvalidEmailOrPasswordException("Неверный логин или пароль");
        }

        // Получаем пользователя и оборачиваем в CustomUserDetails
        User user = userRepository.findByEmail(loginRequestDTO.getEmail().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtUtils.generateToken(userDetails);

        logger.info("Пользователь успешно вошел в систему");

        return LoginResponseDTO.builder()
                .message("Успешный вход")
                .token(token)
                .build();
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
