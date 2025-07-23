package com.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {

	Category findByCategoryName(String category);

	Optional<Category> findByIdAndIsDeletedFalse(Long category_id);

	List<Category> findByIsDeletedFalse();

}
