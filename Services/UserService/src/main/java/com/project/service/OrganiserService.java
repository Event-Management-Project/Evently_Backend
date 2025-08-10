package com.project.service;

import com.project.dto.ApiResponse;
import com.project.dto.OrganiserCreateDto;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;
import com.project.dto.ChangePasswordDto;
import com.project.dto.JwtRequest;
import com.project.dto.JwtResponse;
import com.project.dto.OrganiserUpdateDto;
import com.project.entities.Organiser;

import java.util.List;
import java.util.Optional;

public interface OrganiserService {
	OrganiserDto saveOrganiser(OrganiserCreateDto organiser);

//    OrganiserDto validateOrganiser(OrganiserLoginDto organiserDetails);
	
	JwtResponse loginOrganiser(JwtRequest loginDto);

	List<Organiser> getAllOrganisers();

	Optional<Organiser> getOrganiserById(Long org_id);

	ApiResponse updateProfile(Long id, OrganiserUpdateDto organiserDto);

	ApiResponse changePassword(Long id, ChangePasswordDto passwordDto);

	ApiResponse deleteOrganiser(Long id);

	List<Organiser> filterOrganiserContainaningAddress(String address);

	List<Organiser> filterOrganiserContainaningCmpName(String company_name);

//     public List<Organiser> filterOrganiserContainaningCmpName(String company_name);

}
