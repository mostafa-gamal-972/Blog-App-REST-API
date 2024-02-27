package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment createComment(long postId, Comment comment) {

        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new ResourceNotFoundException("Post", "id", postId));

        // Set post to comment entity
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);

        return newComment;
    }

    @Override
    public List<Comment> getCommentsByPostId(long postId) {
        // retrieve comments by post id
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments;
    }

    @Override
    public Comment getCommentById(long postId, long commentId) {
        // retrieve post by id
        Post post = getPostById(postId);

        // retrieve comment by id
        Comment comment = getCommentById(commentId);

        if (!checkIfCommentBelongsToPost(post, comment) ){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, AppConstants.COMMENT_DOES_NOT_BELONG_TO_POST_MSG);
        }

        return comment;
    }

    @Override
    public Comment updateComment(long postId, long commentId, Comment requestComment) {

        // retrieve post by id
        Post post =  getPostById(postId);

        // retrieve comment by id
        Comment oldComment = getCommentById(commentId);

        if (!checkIfCommentBelongsToPost(post, oldComment) ){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, AppConstants.COMMENT_DOES_NOT_BELONG_TO_POST_MSG);
        }

        // Update old comment
        oldComment.setName(requestComment.getName());
        oldComment.setEmail(requestComment.getEmail());
        oldComment.setBody(requestComment.getBody());

        Comment updatedComment = commentRepository.save(oldComment);

        return updatedComment;
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        // retrieve post by id
        Post post = getPostById(postId);

        // retrieve comment by id
        Comment comment = getCommentById(commentId);

        if (! comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, AppConstants.COMMENT_DOES_NOT_BELONG_TO_POST_MSG);
        }

        commentRepository.delete(comment);
    }

    private Comment getCommentById (long id) {
        return commentRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Comment", "id", id));
    }

    private Post getPostById (long id){
        return postRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));
    }

    private boolean checkIfCommentBelongsToPost (Post post , Comment comment){
        return comment.getPost().getId().equals(post.getId());
    }

}