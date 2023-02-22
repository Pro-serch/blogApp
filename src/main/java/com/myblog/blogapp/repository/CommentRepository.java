package com.myblog.blogapp.repository;

import com.myblog.blogapp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long > {
}
