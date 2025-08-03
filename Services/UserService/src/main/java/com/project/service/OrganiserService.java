package com.project.service;

import com.project.dto.ApiResponse;
import com.project.dto.OrganiserCreateDto;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;
import com.project.entities.Organiser;

import java.util.List;
import java.util.Optional;

public interface OrganiserService {
	ApiResponse saveOrganiser(OrganiserCreateDto organiser);

	ApiResponse validateOrganiser(OrganiserLoginDto organiserDetails);

	List<Organiser> getAllOrganisers();

	Optional<Organiser> getOrganiserById(Long org_id);

	ApiResponse updateProfile(Long id, OrganiserDto organiserDto);

	ApiResponse changePassword(String email, String password);

	ApiResponse deleteOrganiser(String org_company_name);

	List<Organiser> filterOrganiserContainaningAddress(String address);

	List<Organiser> filterOrganiserContainaningCmpName(String company_name);

//     public List<Organiser> filterOrganiserContainaningCmpName(String company_name);

}
