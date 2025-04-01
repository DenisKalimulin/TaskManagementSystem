package ru.taskmanager.taskmanagementsystem.serviceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.userDTO.UserUpdateDTO;
import ru.taskmanager.taskmanagementsystem.configSecurity.CustomUserDetails;
import ru.taskmanager.taskmanagementsystem.enums.RoleType;
import ru.taskmanager.taskmanagementsystem.mappers.user.UserMapper;
import ru.taskmanager.taskmanagementsystem.models.Role;
import ru.taskmanager.taskmanagementsystem.models.User;
import ru.taskmanager.taskmanagementsystem.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUpSecurityContext() {
        CustomUserDetails userDetails = new CustomUserDetails(
                User.builder()
                        .id(1L)
                        .email("user@test.com")
                        .login("user")
                        .password("password")
                        .roles(Set.of(new Role(1L, RoleType.ROLE_USER)))
                        .build()
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserProfile_shouldReturnProfile_whenUserExists() {
        User user = User.builder()
                .id(1L)
                .email("user@test.com")
                .login("user")
                .password("password")
                .roles(Set.of(new Role(1L, RoleType.ROLE_USER)))
                .build();

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .email("user@test.com")
                .login("user")
                .build();

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(user))
                .thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getCurrentUserProfile();

        Assertions.assertEquals("user@test.com", result.getEmail());
        Assertions.assertEquals("user", result.getLogin());
    }

    @Test
    void deleteCurrentUser_shouldDeleteUser_andClearSecurityContext() {
        User user = User.builder()
                .id(1L)
                .email("user@test.com")
                .login("user")
                .password("password")
                .roles(Set.of(new Role(1L, RoleType.ROLE_USER)))
                .build();


        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        userService.deleteCurrentUser();

        verify(userRepository).delete(user);
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void updateUser_ShouldUpdateSuccessfully() {
        User existingUser = User.builder()
                .id(1L)
                .email("user@test.com")
                .login("user")
                .password("encodedOldPass")
                .build();

        UserUpdateDTO dto = UserUpdateDTO.builder()
                .email("newuser@test.com")
                .login("newuser")
                .password("newpass")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .email("newuser@test.com")
                .login("newuser")
                .password("encodedNewPass")
                .build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByLogin("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDTO responseDTO = new UserResponseDTO();
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(responseDTO);

        UserResponseDTO result = userService.updateUser(dto);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserResponseDTO(any(User.class));
    }
}

