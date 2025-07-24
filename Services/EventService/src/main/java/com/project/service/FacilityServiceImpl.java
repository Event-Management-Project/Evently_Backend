package com.project.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.dao.EventDao;
import com.project.dao.FacilityDao;
import com.project.dto.ApiResponse;
import com.project.dto.FacilityDTO;
import com.project.entities.Events;
import com.project.entities.Facility;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class FacilityServiceImpl implements FacilityService {
	
	private final ModelMapper modelMapper;
	private final FacilityDao facilityDao;
	private final EventDao eventDao;
	
	@Override
	public ApiResponse addFacility(FacilityDTO newFacility) {
		
		Facility facility = modelMapper.map(newFacility, Facility.class);
		System.out.println("Mapped Facility Name: " + facility.getFacilityName());
		
		facilityDao.save(facility);
		
		return new ApiResponse("New Facility Added");
	}
	
	

	@Override
	public List<FacilityDTO> getFacility() {
		List<Facility> facilityList = facilityDao.findByIsDeletedFalse();
		List<FacilityDTO> facilityResponseList = new ArrayList<>();
		FacilityDTO facilityResponse = null;
		
		for(Facility facility: facilityList) {
			facilityResponse = modelMapper.map(facility, FacilityDTO.class);
			
			facilityResponseList.add(facilityResponse);
		}

		return facilityResponseList;
	}

	@Override
	public List<FacilityDTO> getFacilityOfEvent(Long evt_id) {
		Events event = eventDao.findByIdAndIsDeletedFalse(evt_id).orElseThrow();
		
		List<Facility> facilityList = event.getFacilities();
		List<FacilityDTO> facilityResponseList = new ArrayList<>();
		FacilityDTO facilityResponse = null;

		for(Facility facility: facilityList) {
			facilityResponse = modelMapper.map(facility, FacilityDTO.class);
			
			facilityResponseList.add(facilityResponse);
		}

		return facilityResponseList;
	}



	@Override
	public ApiResponse addFacilityToEvent(List<Long> facility_id, Long evt_id) {
		Events event = eventDao.findByIdAndIsDeletedFalse(evt_id).orElseThrow();
		List<Facility> facilityList = facilityDao.findAllByIdInAndIsDeletedFalse(facility_id);
		
		event.getFacilities().addAll(facilityList);
		eventDao.save(event);
		
		return new ApiResponse("Facilities Added Successfully");
	}	
}
