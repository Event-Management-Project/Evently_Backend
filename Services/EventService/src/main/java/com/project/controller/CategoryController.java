package com.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.dto.CategoryDTO;
import com.project.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;
	
	@PostMapping
	@Operation(description = "Add New Category ")
	public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryCreateDto){
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(categoryService.createCategory(categoryCreateDto));
	}
	
	@GetMapping
	@Operation(description = " Get Categories ")
	public ResponseEntity<?> getCategory(){
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(categoryService.getCategory());
	}
	
	@GetMapping("/SpecificEvent/{evt_id}")
	@Operation(description = "Get Event of Specified Category ")
	public ResponseEntity<?> getCategoryOfEvent(@PathVariable Long evt_id){
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(categoryService.getCategoryOfEvent(evt_id));
	}
	
	@DeleteMapping("{category_id}")
	@Operation(description = "Delete Category by Category Id ")
	public ResponseEntity<?> deleteCategory(@PathVariable Long category_id){
		return ResponseEntity.status(HttpStatus.CREATED)
		.body(categoryService.deleteCategory(category_id));
	}
}
