package com.project.service;

import java.util.List;

import com.project.dto.ApiResponse;
import com.project.dto.CategoryDTO;

public interface CategoryService {
	public ApiResponse createCategory(CategoryDTO categoryCreateDto);

	public List<CategoryDTO> getCategory();

	public CategoryDTO getCategoryOfEvent(Long evt_id);

	public ApiResponse deleteCategory(Long category_id);
}
