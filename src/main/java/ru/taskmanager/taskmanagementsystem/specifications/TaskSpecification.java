package ru.taskmanager.taskmanagementsystem.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.taskmanager.taskmanagementsystem.enums.TaskPriority;
import ru.taskmanager.taskmanagementsystem.enums.TaskStatus;
import ru.taskmanager.taskmanagementsystem.models.Task;
import ru.taskmanager.taskmanagementsystem.models.User;

public class TaskSpecification {

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasAuthor(User author) {
        return (root, query, cb) -> author == null ? null : cb.equal(root.get("author"), author);
    }

    public static Specification<Task> hasExecutor(User executor) {
        return (root, query, cb) -> executor == null ? null : cb.equal(root.get("executor"), executor);
    }
}