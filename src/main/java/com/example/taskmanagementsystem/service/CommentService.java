package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.exception.UnauthorizedAccessException;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final TaskService taskService;

    public Comment createComment(Comment comment, Long userId) {
        Task task = taskService.getTaskById(comment.getTask().getId());
        if (task.getAuthor().getId().equals(userId) || task.getAssignee().getId().equals(userId)) {
            comment.setCreatedAt(LocalDateTime.now());
            return commentRepository.save(comment);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to add a comment to this task.");
        }
    }

    public Page<Comment> getCommentsByTaskId(Integer taskId, Pageable pageable) {
        return commentRepository.findByTaskId(taskId, pageable);
    }
}