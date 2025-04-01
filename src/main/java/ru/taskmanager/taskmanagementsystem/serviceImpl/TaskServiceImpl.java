package ru.taskmanager.taskmanagementsystem.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.StatusDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskFilterDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.TaskResponseDTO;
import ru.taskmanager.taskmanagementsystem.configSecurity.SecurityUtils;
import ru.taskmanager.taskmanagementsystem.enums.TaskStatus;
import ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions.AccessDeniedException;
import ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions.TaskNotFoundException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UnauthorizedAccessException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UserNotFoundException;
import ru.taskmanager.taskmanagementsystem.mappers.task.TaskMapper;
import ru.taskmanager.taskmanagementsystem.models.Task;
import ru.taskmanager.taskmanagementsystem.models.User;
import ru.taskmanager.taskmanagementsystem.repository.TaskRepository;
import ru.taskmanager.taskmanagementsystem.repository.UserRepository;
import ru.taskmanager.taskmanagementsystem.service.TaskService;
import ru.taskmanager.taskmanagementsystem.specifications.TaskSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Transactional
    @Override
    public TaskResponseDTO getTaskById(Long id) {
        logger.info("Получение задачи по id");

        Task task = getTaskByIdOrThrow(id);

        return taskMapper.toTaskResponseDTO(task);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByAuthor(Long userId, TaskFilterDTO filter, Pageable pageable) {
        User user = getUserById(userId);
        Specification<Task> spec = Specification
                .where(TaskSpecification.hasAuthor(user))
                .and(TaskSpecification.hasStatus(filter.getStatus()))
                .and(TaskSpecification.hasPriority(filter.getPriority()));

        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::toTaskResponseDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByExecutor(Long userId, TaskFilterDTO filter, Pageable pageable) {
        User user = getUserById(userId);
        Specification<Task> spec = Specification
                .where(TaskSpecification.hasExecutor(user))
                .and(TaskSpecification.hasStatus(filter.getStatus()))
                .and(TaskSpecification.hasPriority(filter.getPriority()));

        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::toTaskResponseDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasks(TaskFilterDTO filter, Pageable pageable) {
        Specification<Task> spec = Specification
                .where(TaskSpecification.hasStatus(filter.getStatus()))
                .and(TaskSpecification.hasPriority(filter.getPriority()));

        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::toTaskResponseDTO);
    }

//    @Transactional
//    @Override
//    public List<TaskResponseDTO> getTasksByAuthor(Long userId) {
//        User user = getUserById(userId);
//
//        List<Task> tasks = taskRepository.findTaskByAuthor(user);
//
//        return taskMapper.toListTaskResponseDTO(tasks);
//    }
//
//    @Transactional
//    @Override
//    public List<TaskResponseDTO> getTasksByExecutor(Long userId) {
//        User user = getUserById(userId);
//
//        List<Task> tasks = taskRepository.findTaskByExecutor(user);
//
//        return taskMapper.toListTaskResponseDTO(tasks);
//    }
//
//    @Transactional
//    @Override
//    public List<TaskResponseDTO> getTasks() {
//        logger.info("Получение всех задач");
//        List<Task> tasks = taskRepository.findAll();
//        return taskMapper.toListTaskResponseDTO(tasks);
//    }

    @Transactional
    @Override
    public TaskResponseDTO updateStatus(StatusDTO status) {
        logger.info("Обновление статуса задачи");

        User user = getCurrentUser();

        Task task = getTaskByIdOrThrow(status.getTaskId());

        if (!isExecutor(user, task)) {
            throw new AccessDeniedException("У вас нет прав для данного действия");
        }

        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.getStatus());
            task.setStatus(taskStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение статуса: " + status);
        }

        return taskMapper.toTaskResponseDTO(task);
    }

    /**
     * Получение задачи по айди
     *
     * @param taskId id задачи
     * @return найденная задача
     */
    private Task getTaskByIdOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id " + taskId + " не найдена"));
    }

    /**
     * Получение текущего пользователя из JWT-токена
     *
     * @return найденный пользователь.
     * @throws UserNotFoundException если пользователь с таким email не найден.
     */
    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentEmail();
        if (email == null) {
            throw new UnauthorizedAccessException("Не удалось определить пользователя");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
    }

    /**
     * Получение пользователя по id
     *
     * @param id id пользователя
     * @return пользователь
     * @throws UserNotFoundException если пользователь с таким id не найден
     */
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    /**
     * Проверка, является ли пользователь исполнителем задачи
     *
     * @param user пользователь, которого нужно проверить
     * @param task задача
     * @return true - если пользователь исполнитель, false - если не является исполнителем
     */
    private boolean isExecutor(User user, Task task) {
        return user.equals(task.getExecutor());
    }
}