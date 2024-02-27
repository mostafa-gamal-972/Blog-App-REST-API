package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.PostResponseDto;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Post createPost(Post post) {

        Long categoryId = post.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Post newPost = postRepository.save(post);
        newPost.setCategory(category);
        return newPost;
    }

    @Override
    public PostResponseDto getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Create a sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create a pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Get page object for all posts in db
        Page<Post> posts = postRepository.findAll(pageable);

        // Get content of pages for all posts
        List<Post> listOfPosts = posts.getContent();

        // Set the response object
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setContent(listOfPosts);
        postResponseDto.setPageNo(pageNo);
        postResponseDto.setPageSize(pageSize);
        postResponseDto.setTotalElements(posts.getTotalElements());
        postResponseDto.setTotalPages(posts.getTotalPages());
        postResponseDto.setLast(posts.isLast());

        return postResponseDto;
    }

    @Override
    public Post getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));
        return post;
    }

    @Override
    public Post updatePost(Post post, long id) {
        // get post by id from db
        Post oldPost = postRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));

        Long categoryId = post.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        oldPost.setTitle(post.getTitle());
        oldPost.setContent(post.getContent());
        oldPost.setDescription(post.getDescription());
        oldPost.setCategory(category);

        Post updatedPost = postRepository.save(oldPost);

        return updatedPost;
    }

    @Override
    public void deletePostById(long id) {
        // get post by id from db
        Post post = postRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Override
    public List<Post> getPostsByCategory (long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts;
     }


}
