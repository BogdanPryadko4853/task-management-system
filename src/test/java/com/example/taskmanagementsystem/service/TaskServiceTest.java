package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.exception.ResourceNotFoundException;
import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User author;
    private User assignee;

    @BeforeEach
    public void setUp() {
        author = new User(1L, "author@example.com", "password", "USER");
        assignee = new User(2L, "assignee@example.com", "password", "USER");
        task = new Task(1, "Test Task", "Description", Status.PENDING, Priority.MEDIUM, author, assignee, Collections.emptyList());
    }

    @Test
    public void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals(task.getTitle(), createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testGetTaskById() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(task.getId());

        assertNotNull(foundTask);
        assertEquals(task.getTitle(), foundTask.getTitle());
    }

    @Test
    public void testGetTaskById_NotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(task.getId());
        });
    }

    @Test
    public void testGetAllTasks() {
        Page<Task> tasksPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(tasksPage);

        Page<Task> result = taskService.getAllTasks(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    public void testUpdateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(task);

        assertNotNull(updatedTask);
        assertEquals(task.getTitle(), updatedTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        when(taskRepository.existsById(task.getId())).thenReturn(true);

        taskService.deleteTask(task.getId());

        verify(taskRepository, times(1)).deleteById(task.getId());
    }

    @Test
    public void testDeleteTask_NotFound() {
        when(taskRepository.existsById(task.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(task.getId());
        });

        verify(taskRepository, never()).deleteById(task.getId());
    }

    @Test
    public void testGetTasksByAuthorId() {
        Page<Task> tasksPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findByAuthorId(eq(author.getId()), any(Pageable.class))).thenReturn(tasksPage);

        Page<Task> result = taskService.getTasksByAuthorId(author.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    public void testGetTasksByAssigneeId() {
        Page<Task> tasksPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findByAssigneeId(eq(assignee.getId()), any(Pageable.class))).thenReturn(tasksPage);

        Page<Task> result = taskService.getTasksByAssigneeId(assignee.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    public void testIsTaskAssignee_True() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        boolean isAssignee = taskService.isTaskAssignee(task.getId(), assignee.getId());

        assertTrue(isAssignee);
    }

    @Test
    public void testIsTaskAssignee_False() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        boolean isAssignee = taskService.isTaskAssignee(task.getId(), author.getId());

        assertFalse(isAssignee);
    }

    @Test
    public void testIsTaskAssignee_TaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        boolean isAssignee = taskService.isTaskAssignee(task.getId(), assignee.getId());

        assertFalse(isAssignee);
    }
}