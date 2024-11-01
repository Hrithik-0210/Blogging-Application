package com.blogging.app.services;

import java.util.List;

import com.blogging.app.payloads.CategoryDto;
import com.blogging.app.payloads.CategoryResponseDto;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	
	CategoryResponseDto getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);
	
	CategoryDto getCategoryById(int categoryId);
	
	void deleteCategory(int categoryId);

}
