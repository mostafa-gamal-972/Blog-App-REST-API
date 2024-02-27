package com.springboot.blog.controller;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostResponseDto;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Resource Operations")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create Post REST API", description = "Create Post REST API is used to save post into database")
    @ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Post> createPost (@Valid @RequestBody Post post){
        return new ResponseEntity(postService.createPost(post), HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Posts Post REST API", description = "Get all posts that exist in database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping
    public ResponseEntity<PostResponseDto> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @Operation(summary = "Get Post REST API", description = "Get Post REST API is used to retrieve a single posts by id from database if exists")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable long id){
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update Post REST API", description = "Update Post REST API is used to update posts that already exist in database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost (@Valid @RequestBody Post post, @PathVariable long id){
        return new ResponseEntity<>(postService.updatePost(post, id), HttpStatus.OK);
    }

    @Operation(summary = "Delete Post REST API", description = "Delete Post REST API is used to delete posts that exist in database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById (@PathVariable long id){
        postService.deletePostById(id);
        return new ResponseEntity(String.format("Post with id = %s has been deleted successfully", id), HttpStatus.OK);
    }

    @Operation(summary = "Get Posts By Category Id REST API", description = "Get Posts By Category Id REST API is used to get all posts that belong to a particular category from database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Post>> getPostsByCategory (@PathVariable Long categoryId){
         List<Post> posts =postService.getPostsByCategory(categoryId);
         return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
