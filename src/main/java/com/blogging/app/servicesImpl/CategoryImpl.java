package com.blogging.app.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogging.app.entities.Category;
import com.blogging.app.exception.ResourceNotFoundException;
import com.blogging.app.payloads.CategoryDto;
import com.blogging.app.payloads.CategoryResponseDto;
import com.blogging.app.repositories.CategoryRepo;
import com.blogging.app.services.CategoryService;

@Service
public class CategoryImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category savedCategory = this.categoryRepo.save(category);
		return this.modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "id", categoryId));
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		Category  updatedCategory =  this.categoryRepo.save(category);
		return this.modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public CategoryResponseDto getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir ) {
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable =  PageRequest.of( pageNo, pageSize, sort);
		Page<Category> categories = this.categoryRepo.findAll(pageable);
		List<CategoryDto> categoryDto = categories.stream().map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
		CategoryResponseDto categoryResDto = new CategoryResponseDto();
		categoryResDto.setCategories(categoryDto);
		categoryResDto.setPageNo(categories.getNumber());
		categoryResDto.setPageSize(categories.getSize());
		categoryResDto.setTotalElement(categories.getTotalElements());
		categoryResDto.setTotalPages(categories.getTotalPages());
		categoryResDto.setLastPage(categories.isLast());
		return categoryResDto;
	}

	@Override
	public CategoryDto getCategoryById(int categoryId) {
		Category category =  this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
		return categoryDto;
	}

	@Override
	public void deleteCategory(int categoryId) {
		Category category =  this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		this.categoryRepo.delete(category);
	}

}
