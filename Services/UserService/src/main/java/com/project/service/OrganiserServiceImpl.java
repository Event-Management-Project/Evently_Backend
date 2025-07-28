package com.project.service;

import com.project.dao.OrganiserDao;
import com.project.dto.ApiResponse;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;

import com.project.entities.Organiser;
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
public class OrganiserServiceImpl implements OrganiserService{


    private final OrganiserDao organiserDao; // final - immutable
    ModelMapper modelMapper;

    @Override
    public ApiResponse saveOrganiser(OrganiserDto organiser) {
    	Organiser organiser1 = modelMapper.map(organiser,Organiser.class);
    	organiserDao.save(organiser1);
        return new ApiResponse("organiser created "+ organiser1.getId());
    }

    @Override
    public ApiResponse validateOrganiser(OrganiserLoginDto organiserLoginDto) {
    	// get email from database
    	Organiser organiser= organiserDao
                 .findByEmail(organiserLoginDto.getEmail())
                 .orElseThrow(() ->
                 new ResourseNotFound("Organiser Not Found"));
    	
      // get email from dto 
        if(!organiser.getPassword().equals(organiserLoginDto.getPassword()))
        {
            throw new ResourseNotFound("In valid password");
        }
        return new ApiResponse("Login successful for organiser "+ organiser.getId());
    }

    
    @Override
    public List<Organiser> getAllOrganisers() {
        return organiserDao.findAll();
    }
    
    @Override
	public Optional<Organiser> getOrganiserById(Long org_id){
    	
    	return organiserDao.findById(org_id); //.orElseGet(()->new ConfigDataResourceNotFoundException("Not Found  "));
    }
    
    

    @Override
    public ApiResponse updateProfile(Long id , OrganiserDto organiserDto) {
        Organiser organiser = organiserDao.findById(id).
                orElseThrow(()->new ResourseNotFound("organiser not found"));
   
         // Mapping : dto --> entity
         modelMapper.map(organiserDto, organiser);
  
//        updatedOrganiser.setUpdatedOn(organiser.getUpdatedOn());
        organiserDao.save(organiser );

        return new ApiResponse("Organiser details updated successfully "+ organiser.getId());
    }

    @Override
    public ApiResponse changePassword(String email, String password) {
    	Organiser organiser=organiserDao.findByEmail(email)
               .orElseThrow(()->new ResourseNotFound("Organiser Not Found"));
    	
    	organiser.setPassword(password);
    	organiserDao.save(organiser);
       return new ApiResponse("Password updated successfully for " + organiser.getId());
    }

    
    
   
    @Override
    public ApiResponse deleteOrganiser(String org_company_name){
        Organiser organiser= organiserDao
        		.findByOrganiserCompanyName(org_company_name)
                .orElseThrow(()->new ResourseNotFound("Organiser Not Found"));
        // soft delete
        organiser.setDeleted(true);
        organiserDao.save(organiser);
        return new ApiResponse("Organiser deleted successfully "+ organiser.getId());
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
