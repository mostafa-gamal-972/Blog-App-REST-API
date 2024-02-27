package com.springboot.blog.service;

import com.springboot.blog.payload.PostResponseDto;
import com.springboot.blog.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Post post);

    PostResponseDto getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    Post getPostById(long id);

    Post updatePost(Post post, long id);

    void deletePostById(long id);

    List<Post> getPostsByCategory(long categoryId);

}
