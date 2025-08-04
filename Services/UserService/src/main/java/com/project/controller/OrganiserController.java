package com.project.controller;

import com.project.dto.ChangePasswordDto;
import com.project.dto.OrganiserDto;
import com.project.dto.OrganiserLoginDto;
import com.project.dto.OrganiserUpdateDto;
import com.project.service.OrganiserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organiser")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrganiserController {
//    @Autowired
    private final OrganiserService organiserService; 

    @PostMapping("/register")
    @Operation( description = "Adds a new organiser to the system with basic details ")
    ResponseEntity<?> organiserCustomer(@RequestBody OrganiserDto organiser){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(organiserService.saveOrganiser(organiser));
    }

    @PostMapping("/login")
    @Operation( description = "Login organiser")
    ResponseEntity<?> loginOrganiser(@RequestBody OrganiserLoginDto organiserLoginDto ){
        return ResponseEntity.status(HttpStatus.OK).
                body(organiserService.validateOrganiser(organiserLoginDto));
    }

    
    @GetMapping("/allorganisers")
    @Operation(description = " Get All Organisers ")
    ResponseEntity<?> getAllOrganisers(){
        return ResponseEntity.status(HttpStatus.OK).
                body(organiserService.getAllOrganisers());
    }
    
    
    @GetMapping("/organiser/{org_id}")
    @Operation(description = "Get Organiser by Organiser Id ")
    ResponseEntity<OrganiserDto> getOrganiserById(@PathVariable Long org_id){
        return ResponseEntity.status(HttpStatus.OK).
                body(organiserService.getOrganiserById(org_id));
    }
    
    
    @GetMapping("/organiser/location/{address}")
    @Operation(description = " Filter Organiser By its Locations ")
    ResponseEntity<?> filterOrganiserContainaningLocation(@PathVariable String address){
        return ResponseEntity.status(HttpStatus.OK).
                body(organiserService.filterOrganiserContainaningAddress(address));
    }
    
    
//    
//    @GetMapping("/{company_name}")
//    @Operation(description = " Filter Organiser By its Company Name ")
//    ResponseEntity<?> filterOrganiserContainaningCompanyName(@PathVariable String company_name){
//        return ResponseEntity.status(HttpStatus.OK).
//                body(organiserService.filterOrganiserContainaningCmpName(company_name));
//    }
//    
    
      
    

    @PutMapping("/update/{org_id}")
    @Operation(description = " Update Profile ")
    ResponseEntity<?> updateProfile(@PathVariable Long org_id, @RequestBody OrganiserUpdateDto organiserDto){
        return ResponseEntity.status(HttpStatus.OK).
                body(organiserService.updateProfile(org_id,organiserDto));
    }
    
    

    @PutMapping("/changePassword/{id}")
    @Operation(description = " Update Password ")
    ResponseEntity<?>  updatePassword(@PathVariable Long id,@RequestBody ChangePasswordDto passwordDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(organiserService.changePassword(id,
                		passwordDto));
    }
    
    
    // Soft Delete 
    @DeleteMapping("/{org_company_name}")
    @Operation(description = " Soft Delete ")
    ResponseEntity<?> removeCustomer(@PathVariable String org_company_name){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(organiserService.deleteOrganiser(org_company_name));
    }






}
