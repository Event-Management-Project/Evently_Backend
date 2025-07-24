package com.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.FacilityDTO;
import com.project.service.FacilityService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/facility")
@AllArgsConstructor
public class FacilityController {
	private final FacilityService facilityService;
	
	@PostMapping
    @Operation(description = " Add Facility ")
	public ResponseEntity<?> addFacility(@RequestBody FacilityDTO newFacility){
		System.out.println(newFacility.getFacilityName());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(facilityService.addFacility(newFacility));
	}
	
	@PostMapping("/ToEvent/{evt_id}")
	@Operation(description = " Add Facility to Event ")
	public ResponseEntity<?> addfacilityToEvent(@RequestBody List<Long> facility_id, @PathVariable Long evt_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(facilityService.addFacilityToEvent(facility_id, evt_id));
	}
	
	@GetMapping
	@Operation(description = " Get Facility")
	public ResponseEntity<?> getFacility(){
		return ResponseEntity.status(HttpStatus.CREATED)
		.body(facilityService.getFacility());
	}
	
	@GetMapping("/SpecificEvent/{evt_id}")
	@Operation(description = " Get Facility of Event ")
	public ResponseEntity<?> getFacilityOfEvent(@PathVariable Long evt_id){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(facilityService.getFacilityOfEvent(evt_id));
	}
}
