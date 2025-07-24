package com.project.service;

import java.util.List;

import com.project.dto.ApiResponse;
import com.project.dto.FacilityDTO;

public interface FacilityService {

	ApiResponse addFacility(FacilityDTO newFacility);

	List<FacilityDTO> getFacility();

	List<FacilityDTO> getFacilityOfEvent(Long evt_id);

	ApiResponse addFacilityToEvent(List<Long> facility_id, Long evt_id);
}
