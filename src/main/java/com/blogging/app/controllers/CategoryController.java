package com.blogging.app.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogging.app.payloads.ApiResponse;
import com.blogging.app.payloads.CategoryDto;
import com.blogging.app.payloads.CategoryResponseDto;
import com.blogging.app.services.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category APIs")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/createCategory")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto createdCategory = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createdCategory, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateCategory/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable("id") int categoryId ) {
		CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
	}

	@GetMapping("/getCategory/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") int categoryId ) {
		CategoryDto category = this.categoryService.getCategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(category, HttpStatus.OK);
	}
	
	@GetMapping("/getAllCategories")
	public ResponseEntity<CategoryResponseDto> getAllCategory(@RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize") int pageSize,@RequestParam(name="sortBy") String sortBy, @RequestParam(name="sortDir") String sortDir){
		CategoryResponseDto categories = this.categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<CategoryResponseDto>(categories, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteCategory/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") int categoryId){
		this.categoryService.deleteCategory(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category Deleted succesfully..", true), HttpStatus.OK);
	}
}
