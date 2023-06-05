package com.myblog.blogapp.controller;

import com.myblog.blogapp.entities.Comment;
import com.myblog.blogapp.payload.CommentDto;
import com.myblog.blogapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // handler method
    @PostMapping("posts/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("postId") long postId, @RequestBody CommentDto commentDto){
        CommentDto dto = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }



    @GetMapping("posts/{postId}/comment")
    public List<CommentDto> getAllCommentsByPostId(@PathVariable ("postId") long postId) {
        List<CommentDto> dto = commentService.getCommentByPostId(postId);
        return dto;
    }

    @PutMapping("/posts/{postId}/comment/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") long postId,
                                                     @PathVariable("id") long id,
                                                    @RequestBody CommentDto commentDto
                                                    ){
        CommentDto dto = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @DeleteMapping("/posts/{postId}/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") long postId,@PathVariable("id") long commentId
                                                    ){
                  commentService.deleteComment(postId, commentId);
                  return null;
    }

}
