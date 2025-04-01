package ru.taskmanager.taskmanagementsystem.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentCreateDTO;
import ru.taskmanager.taskmanagementsystem.DTO.commentDTO.CommentResponseDTO;
import ru.taskmanager.taskmanagementsystem.configSecurity.SecurityUtils;
import ru.taskmanager.taskmanagementsystem.exceptions.commentExceptions.CommentNotFoundException;
import ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions.AccessDeniedException;
import ru.taskmanager.taskmanagementsystem.exceptions.taskExceptions.TaskNotFoundException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UnauthorizedAccessException;
import ru.taskmanager.taskmanagementsystem.exceptions.userExceptions.UserNotFoundException;
import ru.taskmanager.taskmanagementsystem.mappers.comment.CommentMapper;
import ru.taskmanager.taskmanagementsystem.models.Comment;
import ru.taskmanager.taskmanagementsystem.models.Task;
import ru.taskmanager.taskmanagementsystem.models.User;
import ru.taskmanager.taskmanagementsystem.repository.CommentRepository;
import ru.taskmanager.taskmanagementsystem.repository.TaskRepository;
import ru.taskmanager.taskmanagementsystem.repository.UserRepository;
import ru.taskmanager.taskmanagementsystem.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Transactional
    @Override
    public CommentResponseDTO getCommentById(Long id) {
        logger.info("Получение комментария по id {}", id);
        return commentMapper.toCommentResponseDTO(getCommentByIdOrThrow(id));
    }

    @Transactional
    @Override
    public List<CommentResponseDTO> getCommentsByTask(Long taskId) {
        logger.info("Получение комментариев к задаче с id {}", taskId);
        Task task = getTaskByIdOrThrow(taskId);

        List<Comment> comments = commentRepository.findCommentByTask(task);
        return commentMapper.toListCommentResponseDTO(comments);
    }

    @Transactional
    @Override
    public CommentResponseDTO createCommentByExecutor(CommentCreateDTO commentCreateDTO) {
        logger.info("Попытка оставить комментарий к задаче с id {}", commentCreateDTO.getTaskId());

        User user = getCurrentUser();

        Task task = getTaskByIdOrThrow(commentCreateDTO.getTaskId());

        boolean isExecutor = isExecutor(user, task);

        if (!isExecutor) {
            throw new AccessDeniedException("Недостаточно прав");
        }

        Comment comment = Comment.builder()
                .author(user)
                .text(commentCreateDTO.getText())
                .task(task)
                .build();

        Comment saved = commentRepository.save(comment);

        logger.info("Комментарий к задаче с id {} оставлен успешно", commentCreateDTO.getTaskId());

        return commentMapper.toCommentResponseDTO(saved);
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
     * Получение комментария по айди
     *
     * @param commentID id комментария
     * @return найденный комментарий
     */
    private Comment getCommentByIdOrThrow(Long commentID) {
        return commentRepository.findById(commentID)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий с id " + commentID + " не найден"));
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
}