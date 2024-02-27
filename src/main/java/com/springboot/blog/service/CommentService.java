package com.springboot.blog.service;

import com.springboot.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment (long postId, Comment  comment);

    List<Comment> getCommentsByPostId (long postId);

    Comment getCommentById (long postId, long commentId);

    Comment updateComment (long postId, long commentId, Comment comment);

    void deleteComment (long postId, long commentId);
}
