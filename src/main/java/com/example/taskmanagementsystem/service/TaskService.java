package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.exception.ResourceNotFoundException;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public Page<Task> getTasksByAuthorId(Long authorId, Pageable pageable) {
        return taskRepository.findByAuthorId(authorId, pageable);
    }

    public Page<Task> getTasksByAssigneeId(Long assigneeId, Pageable pageable) {
        return taskRepository.findByAssigneeId(assigneeId, pageable);
    }

    public boolean isTaskAssignee(Integer taskId, Long userId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.isPresent() && task.get().getAssignee().getId().equals(userId);
    }
}