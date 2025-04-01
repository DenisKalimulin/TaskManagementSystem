package ru.taskmanager.taskmanagementsystem.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.DTO.taskDTO.*;
import ru.taskmanager.taskmanagementsystem.configSecurity.SecurityUtils;
import ru.taskmanager.taskmanagementsystem.enums.RoleType;
import ru.taskmanager.taskmanagementsystem.enums.TaskPriority;
import ru.taskmanager.taskmanagementsystem.enums.TaskStatus;
import ru.taskmanager.taskmanagementsystem.exceptions.commentExceptions.CommentNotFoundException;
import ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions.AccessDeniedException;
import ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions.TaskNotFoundException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UnauthorizedAccessException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UserNotFoundException;
import ru.taskmanager.taskmanagementsystem.mappers.comment.CommentMapper;
import ru.taskmanager.taskmanagementsystem.mappers.task.TaskMapper;
import ru.taskmanager.taskmanagementsystem.models.Comment;
import ru.taskmanager.taskmanagementsystem.models.Task;
import ru.taskmanager.taskmanagementsystem.models.User;
import ru.taskmanager.taskmanagementsystem.repository.CommentRepository;
import ru.taskmanager.taskmanagementsystem.repository.TaskRepository;
import ru.taskmanager.taskmanagementsystem.repository.UserRepository;
import ru.taskmanager.taskmanagementsystem.service.AdminService;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskMapper taskMapper;

    private final static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Transactional
    @Override
    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO) {
        logger.info("Создание новой задачи");

        User author = getCurrentUser();

        checkAdmin(author);

        Task task = Task.builder()
                .header(taskCreateDTO.getHeader())
                .description(taskCreateDTO.getDescription())
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.valueOf(taskCreateDTO.getPriority().toUpperCase()))
                .author(author)
                .build();

        Task savedTask = taskRepository.save(task);

        logger.info("Задача с id {} успешно создана", savedTask.getId());

        return taskMapper.toTaskResponseDTO(savedTask);
    }

    @Transactional
    @Override
    public TaskResponseDTO updateTask(TaskUpdateDTO taskUpdateDTO) {
        logger.info("Попытка редактировать данные задачи");

        User user = getCurrentUser();
        Task task = getTaskByIdOrThrow(taskUpdateDTO.getId());

        checkAdmin(user);

        if (taskUpdateDTO.getHeader() != null) {
            task.setHeader(taskUpdateDTO.getHeader());
        }

        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }

        Task updated = taskRepository.save(task);
        logger.info("Задача с id {} успешно обновлена", updated.getId());

        return taskMapper.toTaskResponseDTO(updated);
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Попытка удалить задачу");

        User user = getCurrentUser();

        Task task = getTaskByIdOrThrow(id);

        checkAdmin(user);

        taskRepository.delete(task);
        logger.info("Задача с id {} удалена", id);
    }

    @Transactional
    @Override
    public TaskResponseDTO updateStatus(StatusDTO status) {
        logger.info("Обновление статуса задачи");

        User user = getCurrentUser();
        checkAdmin(user);

        Task task = getTaskByIdOrThrow(status.getTaskId());

        TaskStatus taskStatus = TaskStatus.valueOf(status.getStatus());
        task.setStatus(taskStatus);

        Task updated = taskRepository.save(task);

        logger.info("Статус задачи с id {} успешно изменен", status.getTaskId());

        return taskMapper.toTaskResponseDTO(updated);
    }

    @Transactional
    @Override
    public TaskResponseDTO updatePriority(PriorityDTO priority) {
        logger.info("Обновление приоритета задачи");

        User user = getCurrentUser();
        checkAdmin(user);

        Task task = getTaskByIdOrThrow(priority.getTaskId());

        TaskPriority taskPriority = TaskPriority.valueOf(priority.getPriority());
        task.setPriority(taskPriority);

        Task updated = taskRepository.save(task);

        logger.info("Приоритет задачи с id {} успешно изменен", priority.getTaskId());

        return taskMapper.toTaskResponseDTO(updated);
    }

    @Transactional
    @Override
    public void assignUserAsExecutor(Long taskId, Long userId) {
        logger.info("Попытка назначить пользователя с id {} исполнителем задачи с id {}", userId, taskId);

        User admin = getCurrentUser();

        User user = getUserById(userId);

        Task task = getTaskByIdOrThrow(taskId);

        checkAdmin(admin);

        task.setExecutor(user);
        Task updated = taskRepository.save(task);
        taskRepository.save(updated);

        logger.info("Пользователь с id {} теперь исполнитель задачи с id {}", userId, taskId);
    }

    @Transactional
    @Override
    public CommentResponseDTO createComment(CommentCreateDTO commentCreateDTO) {
        logger.info("Попытка оставить комментарий к задаче с id {}", commentCreateDTO.getTaskId());

        User user = getCurrentUser();

        Task task = getTaskByIdOrThrow(commentCreateDTO.getTaskId());

        checkAdmin(user);

        Comment comment = Comment.builder()
                .author(user)
                .text(commentCreateDTO.getText())
                .task(task)
                .build();

        Comment saved = commentRepository.save(comment);

        logger.info("Комментарий к задаче с id {} оставлен успешно", commentCreateDTO.getTaskId());

        return commentMapper.toCommentResponseDTO(saved);
    }

    @Transactional
    @Override
    public void deleteCommentById(Long commentId) {
        User user = getCurrentUser();

        Comment comment = findCommentById(commentId);

        checkAdmin(user);

        commentRepository.delete(comment);
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
     * Получение комментария по id
     *
     * @param commentId id комментария
     * @return комментарий
     * @throws CommentNotFoundException если комментарий с таким id не найден
     */
    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий с id " + commentId + " не найден"));
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
     * Проверка, является ли пользователь администратором
     *
     * @param user пользователь которого нужно проверить
     * @return true - если пользователь админ, false - если не является админом
     */
    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ROLE_ADMIN);
    }

    private void checkAdmin(User user) {
        if (!isAdmin(user)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }

}