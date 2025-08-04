package com.project.service;

import com.project.dto.ApiResponse;
import com.project.dto.ChangePasswordDto;
import com.project.dto.CustomerDto;
import com.project.dto.CustomerLoginDto;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;
import com.project.dto.OrganiserUpdateDto;
import com.project.entities.Customer;
import com.project.entities.Organiser;

import java.util.List;
import java.util.Optional;

public interface OrganiserService {
    ApiResponse saveOrganiser(OrganiserDto organiser);
    ApiResponse validateOrganiser(OrganiserLoginDto organiserDetails);
      List<Organiser> getAllOrganisers();
      OrganiserDto getOrganiserById(Long org_id);
      ApiResponse updateProfile(Long id, OrganiserUpdateDto organiserDto);
    ApiResponse changePassword(Long id, ChangePasswordDto passwordDto);
    ApiResponse deleteOrganiser(String org_company_name);
     List<Organiser> filterOrganiserContainaningAddress(String address);
	List<Organiser> filterOrganiserContainaningCmpName(String company_name);
     
//     public List<Organiser> filterOrganiserContainaningCmpName(String company_name);


}
