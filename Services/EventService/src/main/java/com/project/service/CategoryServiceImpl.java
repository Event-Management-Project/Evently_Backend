package com.project.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dao.CategoryDao;
import com.project.dao.EventDao;
import com.project.dto.ApiResponse;
import com.project.dto.CategoryDTO;
import com.project.entities.Category;
import com.project.entities.Events;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final ModelMapper modelMapper;
	private final CategoryDao categoryDao;
	private final EventDao eventDao;
	
	@Override
	public ApiResponse createCategory(CategoryDTO categoryCreateDto) {
		Category category = modelMapper.map(categoryCreateDto, Category.class);
		
		categoryDao.save(category);
		
		return new ApiResponse("New Category Added");
	}

	@Override
	public List<CategoryDTO> getCategory() {
		List<Category> categoryList = categoryDao.findByIsDeletedFalse();
		List<CategoryDTO> categoryResponseList = new ArrayList<>();
		
		CategoryDTO categoryResponse = null;
		
		for(Category category: categoryList) {
			categoryResponse = modelMapper.map(category, CategoryDTO.class);
			
			categoryResponseList.add(categoryResponse);
		}
		
		return categoryResponseList;
	}

	@Override
	public CategoryDTO getCategoryOfEvent(Long evt_id) {
		Events event = eventDao.findByIdAndIsDeletedFalse(evt_id).orElseThrow();
		Category category = event.getCategory();
		
		CategoryDTO categoryResponse = modelMapper.map(category, CategoryDTO.class);
		
		return categoryResponse;
	}

	@Override
	public ApiResponse deleteCategory(Long category_id) {
		Category category = categoryDao.findByIdAndIsDeletedFalse(category_id).orElseThrow();
		category.setDeleted(true);
		
		categoryDao.save(category);

		return new ApiResponse("Category Deleted Successfully");
	}
}
