package com.myblog.blogapp.service;

import com.myblog.blogapp.payload.CommentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId ,CommentDto commentDto);

    List<CommentDto> getCommentByPostId(long postID);

    CommentDto updateComment(long postId, long id, CommentDto commentDto);


    ResponseEntity<String> deleteComment(long postId, long commentId);
}
