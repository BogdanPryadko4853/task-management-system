package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.security.CustomUserDetails;
import com.example.taskmanagementsystem.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comment Controller", description = "Operations pertaining to comments in the task management system")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a new comment", responses = {
            @ApiResponse(responseCode = "200", description = "Comment created successfully")
    })
    public ResponseEntity<Comment> createComment(@RequestBody @Parameter(description = "Comment object", required = true) Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return ResponseEntity.ok(commentService.createComment(comment, userId));
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get comments by task ID", responses = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    })
    public ResponseEntity<Page<Comment>> getCommentsByTaskId(@PathVariable @Parameter(description = "Task ID", required = true) Integer taskId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, pageable));
    }
}