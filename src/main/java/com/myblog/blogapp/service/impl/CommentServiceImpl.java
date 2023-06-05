package com.myblog.blogapp.service.impl;

import com.myblog.blogapp.entities.Comment;
import com.myblog.blogapp.entities.Post;
import com.myblog.blogapp.exception.ResourceNotFoundException;
import com.myblog.blogapp.payload.CommentDto;
import com.myblog.blogapp.repository.CommentRepository;
import com.myblog.blogapp.repository.PostRepository;
import com.myblog.blogapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service

public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper mapper;

    //this is constructor
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId , CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));
        Comment comment = maptoComment(commentDto); // here we call maptoComment method & with the help of comment
                                                    // Entity object reference variable
        comment.setPost(post); //tells the for this post id save the comment
        Comment newComment = commentRepository.save(comment);
           return  maptoDto(newComment);// here maptoDto method takes newComment and return commentDto Object



    }


    @Override
    public List<CommentDto> getCommentByPostId(long postID) {
        List<Comment> comments = commentRepository.findByPostId(postID);
        return comments.stream().map(comment->maptoDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        // here check postId is present or not
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));
        // here check comment id is present or not
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException
                ("comment", "id", id));

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updatedComment = commentRepository.save(comment);

        return maptoDto(updatedComment);
    }

    @Override
    public ResponseEntity<String> deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException
                ("comment", "id", commentId));


          commentRepository.deleteById(commentId);

          return  new ResponseEntity<>("comment is deleted !" , HttpStatus.OK);

    }

    // Comment is Entity Class
    Comment maptoComment(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment=new Comment();   // here we create Comment class Object
//        comment.setName(commentDto.getName());   //here we set the value of commentDto object in Comment Entity;
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment; // so maptoComment method return Comment Entity Object

    }

    CommentDto maptoDto(Comment comment){
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        // here we create CommentDto object
//        CommentDto commentDto=new CommentDto();
//        // here with the help of object reference variable of commentDto class set the value of comment class to
//        // commentDto class
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }
}
