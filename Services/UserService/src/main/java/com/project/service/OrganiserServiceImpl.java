package com.project.service;

import com.project.dao.OrganiserDao;
import com.project.dto.ApiResponse;
import com.project.dto.ChangePasswordDto;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;
import com.project.dto.OrganiserUpdateDto;
import com.project.entities.Organiser;
import com.project.exceptions.ChangePasswordException;
import com.project.exceptions.ResourseNotFound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrganiserServiceImpl implements OrganiserService {

	private final OrganiserDao organiserDao;
	ModelMapper modelMapper;

	@Override
	public ApiResponse saveOrganiser(OrganiserDto organiser) {
		Organiser organiser1 = modelMapper.map(organiser, Organiser.class);
		organiserDao.save(organiser1);
		return new ApiResponse("organiser created " + organiser1.getId());
	}

	@Override
	public ApiResponse validateOrganiser(OrganiserLoginDto organiserLoginDto) {
		Organiser organiser = organiserDao.findByEmail(organiserLoginDto.getEmail())
				.orElseThrow(() -> new ResourseNotFound("Organiser Not Found"));

		if (!organiser.getPassword().equals(organiserLoginDto.getPassword())) {
			throw new ResourseNotFound("In valid password");
		}
		return new ApiResponse("Login successful for organiser ", organiser);
	}

	@Override
	public List<Organiser> getAllOrganisers() {
		return organiserDao.findAll();
	}

	@Override
	public OrganiserDto getOrganiserById(Long org_id) {
		Organiser organiser = organiserDao.findById(org_id).orElseThrow();
		
		OrganiserDto organiserDto = modelMapper.map(organiser, OrganiserDto.class);

		return organiserDto;
	}

	@Override
	public ApiResponse updateProfile(Long id, OrganiserUpdateDto organiserDto) {
		Organiser organiser = organiserDao.findById(id).orElseThrow(() -> new ResourseNotFound("organiser not found"));

		// Mapping : dto --> entity
		modelMapper.map(organiserDto, organiser);

//        updatedOrganiser.setUpdatedOn(organiser.getUpdatedOn());
		organiserDao.save(organiser);

		return new ApiResponse("Organiser details updated successfully " + organiser.getId());
	}

	@Override
    public ApiResponse changePassword(Long id, ChangePasswordDto dto) {
        Organiser organiser = organiserDao.findById(id)
            .orElseThrow(() -> new ResourseNotFound("Organiser not found"));

        if (!organiser.getPassword().equals(dto.getCurrentPassword())) {
            throw new ChangePasswordException("Current password is incorrect");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ChangePasswordException("New password and confirm password do not match");
        }

        if (dto.getCurrentPassword().equals(dto.getNewPassword())) {
            throw new ChangePasswordException("New password cannot be the same as the current password");
        }

        organiser.setPassword(dto.getNewPassword());
        organiserDao.save(organiser);

        return new ApiResponse("Password updated successfully for organiser ID: " + organiser.getId());
    }

	@Override
	public ApiResponse deleteOrganiser(String org_company_name) {
		Organiser organiser = organiserDao.findByOrganiserCompanyName(org_company_name)
				.orElseThrow(() -> new ResourseNotFound("Organiser Not Found"));
		// soft delete
		organiser.setDeleted(true);
		organiserDao.save(organiser);
		return new ApiResponse("Organiser deleted successfully " + organiser.getId());
	}

//	@Override
//	public List<Organiser> filterOrganiserContainaningAddress(String address) {
//		
//		 List<Organiser> organiserList = organiserDao.filterOrganiserContainaningLocation(address);
//		
//		return organiserList;
//	}

	@Override
	public List<Organiser> filterOrganiserContainaningCmpName(String company_name) {

		return filterOrganiserContainaningCmpName(company_name);
	}

	@Override
	public List<Organiser> filterOrganiserContainaningAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}
}
