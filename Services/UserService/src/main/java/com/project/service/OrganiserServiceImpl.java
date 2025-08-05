package com.project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.dao.OrganiserDao;
import com.project.dto.ApiResponse;
import com.project.dto.OrganiserCreateDto;
import com.project.dto.ChangePasswordDto;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;
import com.project.dto.OrganiserUpdateDto;
import com.project.entities.Organiser;
import com.project.exceptions.ChangePasswordException;
import com.project.exceptions.ResourseNotFound;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrganiserServiceImpl implements OrganiserService {
	private final Cloudinary cloudinary;

	private final OrganiserDao organiserDao;
	private final ModelMapper modelMapper;

	@Override
	public OrganiserDto saveOrganiser(OrganiserCreateDto organiserCreateDto) {
	    String url = null;

	    try {
	        @SuppressWarnings("unchecked")
	        Map<String, Object> data = this.cloudinary.uploader().upload(organiserCreateDto.getImage().getBytes(),
	                ObjectUtils.emptyMap());
	        url = (String) data.get("url");
	    } catch (IOException e) {
	        throw new RuntimeException("File not been able to upload");
	    }

	    // Map directly from OrganiserCreateDto to Organiser to include password
	    Organiser organiser = modelMapper.map(organiserCreateDto, Organiser.class);
	    organiser.setImageURL(url);

	    organiser = organiserDao.save(organiser); // persist and get generated ID

	    // Prepare response DTO
	    OrganiserDto organiserDto = new OrganiserDto();
	    organiserDto.setOrgId(organiser.getId());
	    organiserDto.setOrganiserCompanyName(organiser.getOrganiserCompanyName());
	    organiserDto.setAddress(organiser.getAddress());
	    organiserDto.setEmail(organiser.getEmail());
	    organiserDto.setPhoneNumber(organiser.getPhoneNumber());

	    return organiserDto;
	}

	@Override
	public OrganiserDto validateOrganiser(OrganiserLoginDto organiserLoginDto) {
		// get email from database
		Organiser organiser = organiserDao.findByEmail(organiserLoginDto.getEmail())
				.orElseThrow(() -> new ResourseNotFound("Organiser Not Found"));

		OrganiserDto organiserDto = modelMapper.map(organiser, OrganiserDto.class);
		organiserDto.setOrgId(organiser.getId());

		if (!organiser.getPassword().equals(organiserLoginDto.getPassword())) {
			throw new ResourseNotFound("In valid password");
		}
		return organiserDto;
	}


	@Override
	public List<Organiser> getAllOrganisers() {
		return organiserDao.findAll();
	}

	@Override
	public Optional<Organiser> getOrganiserById(Long org_id) {

		return organiserDao.findById(org_id);
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
