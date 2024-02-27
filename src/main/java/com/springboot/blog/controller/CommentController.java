package com.springboot.blog.controller;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@Tag(name = "Comment Resource Operations")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create Comment REST API", description = "Create comment for a post")
    @ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
    @PostMapping("/posts/{postId}/comments/create")
    public ResponseEntity<Comment> createComment(@PathVariable long postId,
                                                 @Valid @RequestBody Comment comment){
        return new ResponseEntity(commentService.createComment(postId, comment), HttpStatus.CREATED);
    }

    @Operation(summary = "Get Comments By Post Id REST API", description = "Get comments for a post by id")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable long postId){
        return commentService.getCommentsByPostId(postId).stream().toList();
    }

    @Operation(summary = "Get Comment By Id REST API", description = "Get Comment by Id for a post REST API")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById (@PathVariable long postId,
                                                   @PathVariable long commentId){
        Comment comment = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @Operation(summary = "Update Comment REST API", description = "Update Comment REST API")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @PutMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> updateComment (@PathVariable long postId,
                                                   @Valid @RequestBody Comment comment){
        Comment updatedComment = commentService.updateComment(postId, comment.getId(), comment);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @Operation(summary = "Delete Comment REST API", description = "Delete Comment by Id REST API")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment (@PathVariable long postId, @PathVariable long commentId){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity(String.format("Comment with id = %s has been deleted successfully from post with id = %s", commentId, postId), HttpStatus.OK);
    }
}
