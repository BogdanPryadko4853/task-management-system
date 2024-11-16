package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Integer taskId);
    Page<Comment> findByTaskId(Integer taskId, Pageable pageable);
}