package com.project.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Facility;

public interface FacilityDao extends JpaRepository<Facility, Long> {

	List<Facility> findByIsDeletedFalse();

	List<Facility> findAllByIdInAndIsDeletedFalse(List<Long> facility_id);

}
