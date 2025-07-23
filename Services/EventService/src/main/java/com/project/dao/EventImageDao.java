package com.project.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.EventImage;
import com.project.entities.Events;

public interface EventImageDao extends JpaRepository<EventImage, Long> {

	List<EventImage> findByEvent(Events events);

	EventImage findByEventAndIsPrimaryTrue(Events events);

}
