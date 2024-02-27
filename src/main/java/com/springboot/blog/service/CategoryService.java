package com.springboot.blog.service;

import com.springboot.blog.entity.Category;

import java.util.List;

public interface CategoryService {

    Category addCategory(Category category);

    Category getCategory(Long categoryId);

    List<Category> getAllCategories();

    Category updateCategory(Category category);

    void deleteCategory(Long categoryId);
}
