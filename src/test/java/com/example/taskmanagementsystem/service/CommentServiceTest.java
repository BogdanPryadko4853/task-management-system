package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.exception.UnauthorizedAccessException;
import com.example.taskmanagementsystem.model.*;
import com.example.taskmanagementsystem.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private Task task;
    private User author;
    private User assignee;

    @BeforeEach
    public void setUp() {
        author = new User(1L, "author@example.com", "password", "USER");
        assignee = new User(2L, "assignee@example.com", "password", "USER");
        task = new Task(1, "Test Task", "Description", Status.PENDING, Priority.MEDIUM, author, assignee, Collections.emptyList());
        comment = new Comment(1L, "Test Comment", LocalDateTime.now(), task, author);
    }

    @Test
    public void testCreateComment_Author() {
        when(taskService.getTaskById(task.getId())).thenReturn(task);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentService.createComment(comment, author.getId());

        assertNotNull(createdComment);
        assertEquals(comment.getText(), createdComment.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testCreateComment_Assignee() {
        when(taskService.getTaskById(task.getId())).thenReturn(task);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentService.createComment(comment, assignee.getId());

        assertNotNull(createdComment);
        assertEquals(comment.getText(), createdComment.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testCreateComment_Unauthorized() {
        User unauthorizedUser = new User(3L, "unauthorized@example.com", "password", "USER");

        when(taskService.getTaskById(task.getId())).thenReturn(task);

        assertThrows(UnauthorizedAccessException.class, () -> {
            commentService.createComment(comment, unauthorizedUser.getId());
        });

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    public void testGetCommentsByTaskId() {
        Page<Comment> commentsPage = new PageImpl<>(Collections.singletonList(comment));
        when(commentRepository.findByTaskId(eq(task.getId()), any(Pageable.class))).thenReturn(commentsPage);

        Page<Comment> result = commentService.getCommentsByTaskId(task.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(comment.getText(), result.getContent().get(0).getText());
    }
}