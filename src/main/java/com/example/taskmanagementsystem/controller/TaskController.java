package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.exception.UnauthorizedAccessException;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.security.CustomUserDetails;
import com.example.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@Tag(name = "Task Controller", description = "Operations pertaining to tasks in the task management system")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new task", responses = {
            @ApiResponse(responseCode = "200", description = "Task created successfully")
    })
    public ResponseEntity<Task> createTask(@RequestBody @Parameter(description = "Task object", required = true) Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully")
    })
    public ResponseEntity<Task> getTaskById(@PathVariable @Parameter(description = "Task ID", required = true) Integer id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all tasks", responses = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    })
    public ResponseEntity<Page<Task>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update task by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Task> updateTask(@PathVariable @Parameter(description = "Task ID", required = true) Integer id, @RequestBody @Parameter(description = "Task object", required = true) Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                taskService.isTaskAssignee(id, userId)) {
            task.setId(id);
            return ResponseEntity.ok(taskService.updateTask(task));
        } else {
            throw new UnauthorizedAccessException("You are not authorized to update this task.");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete task by ID", responses = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable @Parameter(description = "Task ID", required = true) Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/author/{authorId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get tasks by author ID", responses = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    })
    public ResponseEntity<Page<Task>> getTasksByAuthorId(@PathVariable @Parameter(description = "Author ID", required = true) Long authorId, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByAuthorId(authorId, pageable));
    }

    @GetMapping("/assignee/{assigneeId}")
    @Operation(summary = "Get tasks by assignee ID", responses = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Page<Task>> getTasksByAssigneeId(@PathVariable @Parameter(description = "Assignee ID", required = true) Long assigneeId, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        if (userId.equals(assigneeId)) {
            return ResponseEntity.ok(taskService.getTasksByAssigneeId(assigneeId, pageable));
        } else {
            throw new UnauthorizedAccessException("You are not authorized to view tasks assigned to this user.");
        }
    }
}