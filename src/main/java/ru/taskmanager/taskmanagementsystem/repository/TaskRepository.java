package ru.taskmanager.taskmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.taskmanager.taskmanagementsystem.models.Task;
import ru.taskmanager.taskmanagementsystem.models.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findTaskByAuthor(User user);

    List<Task> findTaskByExecutor(User executor);

    Page<Task> findByAuthor(User author, Pageable pageable);

    Page<Task> findByExecutor(User executor, Pageable pageable);
}