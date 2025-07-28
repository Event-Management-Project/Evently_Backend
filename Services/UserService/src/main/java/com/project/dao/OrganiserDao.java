
package com.project.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.entities.Customer;
import com.project.entities.Organiser;

@Repository
public interface OrganiserDao extends JpaRepository<Organiser,Long>{
	
	
     //Get Organiser profile
	  Optional<Organiser> findById(Long id);
	  Optional<Organiser> findByEmail(String email);
	  Optional<Organiser> findByOrganiserCompanyName(String organiserCompanyName);
	  
	  
	//Get Organisers
	 List<Organiser> findAll();
	 
	//Organiser-filter-location
	 @Query("SELECT o FROM Organiser o WHERE LOWER(o.address) LIKE LOWER(CONCAT('%', :address, '%'))")
	    List<Organiser> filterOrganiserContainaningLocation(String address);
	
	
//	//Organiser-filter-name
//	 @Query("SELECT o FROM Organiser o WHERE LOWER(o.company_name) LIKE LOWER(CONCAT('%', :company_name, '%'))")
//	    List<Organiser> filterOrganiserContainaningCompanyName(String company_name);
//	
//	 
	 
	 //Delete Profile
	//Update Profile
	//Password Change
	

}
