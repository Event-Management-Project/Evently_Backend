package com.project.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.project.config.JwtUtil;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.dao.OrganiserDao;
import com.project.dto.ApiResponse;
import com.project.dto.OrganiserCreateDto;
import com.project.dto.ChangePasswordDto;
import com.project.dto.JwtRequest;
import com.project.dto.JwtResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrganiserServiceImpl implements OrganiserService {
	private final Cloudinary cloudinary;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
	private final OrganiserDao organiserDao;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public OrganiserDto saveOrganiser(OrganiserCreateDto organiserCreateDto) {
	    String url = null;

	    try {
	        @SuppressWarnings("unchecked")
	        Map<String, Object> data = this.cloudinary.uploader().upload(
	                organiserCreateDto.getImage().getBytes(),
	                ObjectUtils.emptyMap()
	        );
	        url = (String) data.get("url");
	    } catch (IOException e) {
	        throw new RuntimeException("File not been able to upload");
	    }

	    // Map from DTO to entity
	    Organiser organiser = modelMapper.map(organiserCreateDto, Organiser.class);

	    // Hash the password before saving
	    organiser.setPassword(passwordEncoder.encode(organiserCreateDto.getPassword()));

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
	    public JwtResponse loginOrganiser(JwtRequest loginDto) {
	        // Step 1: Authenticate
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
	        );

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String token = jwtUtil.createToken(authentication);

	        // Step 2: Fetch Organiser
	        Organiser organiser = organiserDao.findByEmail(loginDto.getEmail())
	                .orElseThrow(() -> new ResourseNotFound("Organiser not found with email: " + loginDto.getEmail()));

	        // Step 3: Map to DTO
	        OrganiserDto organiserDto = modelMapper.map(organiser, OrganiserDto.class);
            organiserDto.setOrgId(organiser.getId());
	        // Step 4: Build Response
	        return new JwtResponse(token, loginDto.getEmail(), "ROLE_ORGANISER", null, organiserDto);
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

	@Override
	public ApiResponse changePassword(Long id, ChangePasswordDto dto) {
	    Organiser organiser = organiserDao.findById(id)
	        .orElseThrow(() -> new ResourseNotFound("Organiser not found"));

	    //  Check if current password matches (encoded check)
	    if (!passwordEncoder.matches(dto.getCurrentPassword(), organiser.getPassword())) {
	        throw new ChangePasswordException("Current password is incorrect");
	    }

	    //  Ensure new and confirm match
	    if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
	        throw new ChangePasswordException("New password and confirm password do not match");
	    }

	    //  Ensure new password is not same as current
	    if (passwordEncoder.matches(dto.getNewPassword(), organiser.getPassword())) {
	        throw new ChangePasswordException("New password cannot be the same as the current password");
	    }

	    // Encode the new password before saving
	    organiser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	    organiserDao.save(organiser);

	    return new ApiResponse("Password updated successfully for organiser ID: " + organiser.getId());
	}


	@Override
	public ApiResponse deleteOrganiser(Long id) {
		Organiser organiser = organiserDao.findById(id)
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
