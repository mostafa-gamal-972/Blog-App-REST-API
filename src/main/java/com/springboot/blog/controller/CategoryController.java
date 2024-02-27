package com.springboot.blog.controller;

import com.springboot.blog.entity.Category;
import com.springboot.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Resource Operations")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Add Category REST API", description = "Add Category REST API is used to save category into database")
    @ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Category> addCategory (@RequestBody Category category){
        Category savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Category By Id REST API", description = "Get Category By Id REST API is used to get a category by Id from database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping("{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId){
        Category category = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "Get All Categories REST API", description = "Get All Categories REST API is used to get all categories stored in database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Update Category REST API", description = "Update Category REST API is used to update category that already exist in database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Category> updateCategory (@RequestBody Category category){
        Category updatedCategory = categoryService.updateCategory(category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @Operation(summary = "Delete Category REST API", description = "Delete Category REST API is used to delete a category from database (if exists)")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"{categoryId}"})
    public ResponseEntity<String> updateCategory (@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(String.format("Category with id = %s has been deleted successfully!", categoryId), HttpStatus.OK);
    }

}
