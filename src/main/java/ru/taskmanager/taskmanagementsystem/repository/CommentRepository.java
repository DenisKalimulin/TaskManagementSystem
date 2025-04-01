package ru.taskmanager.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.taskmanager.taskmanagementsystem.models.Comment;
import ru.taskmanager.taskmanagementsystem.models.Task;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByTask(Task task);
}