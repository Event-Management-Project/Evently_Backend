package com.project.dao;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.entities.Category;
import com.project.entities.Events;

public interface EventDao extends JpaRepository<Events, Long> {
		List<Events> findByStartDateTimeAfterAndIsDeletedFalse(LocalDateTime currentDate);

		@Query("SELECT e FROM Events e WHERE e.startDateTime < :currentDate AND e.isDeleted = false")
	    List<Events> findByStartDateTimeBeforeAndIsDeletedFalse(@Param("currentDate") LocalDateTime currentDate);

		List<Events> findByOrganiserIdAndIsDeletedFalse(Long organiser_id);

		List<Events> findByOrganiserIdAndStartDateTimeAfterAndIsDeletedFalse(Long organiser_id, LocalDateTime currentDate);

		List<Events> findByOrganiserIdAndStartDateTimeBeforeAndIsDeletedFalse(Long organiser_id, LocalDateTime currentDate);

		List<Events> findByOrganiserIdAndIsDeletedFalseOrderByStartDateTimeAsc(Long organiser_id);

		List<Events> findByOrganiserIdAndLocationAndIsDeletedFalse(Long organiser_id, String location);

		List<Events> findByOrganiserIdAndCategoryAndIsDeletedFalse(Long organiser_id, Optional<Category> category);

		List<Events> findByIdAndOrganiserIdAndIsDeletedFalse(Long evt_id, Long organiser_id);

		Optional<Events> findByIdAndStartDateTimeAfter(Long evt_id, LocalDateTime currentDate);

		Optional<Events> findByIdAndIsDeletedFalse(Long evt_id);
	}
