package com.project.controller;

import com.project.dto.OrganiserCreateDto;
import com.project.dto.OrganiserDto;
import com.project.dto.ChangePasswordDto;
import com.project.dto.OrganiserLoginDto;
import com.project.dto.OrganiserUpdateDto;
import com.project.entities.Organiser;
import com.project.service.OrganiserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organiser")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrganiserController {
//    @Autowired
    private final OrganiserService organiserService; 

    @PostMapping(value = "/organiser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation( description = "Adds a new organiser to the system with basic details ")
    ResponseEntity<OrganiserDto> organiserCustomer(@ModelAttribute OrganiserCreateDto organiser){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(organiserService.saveOrganiser(organiser));
    }

    @PostMapping("/login")
    @Operation( description = "Login organiser")
    ResponseEntity<OrganiserDto> loginOrganiser(@RequestBody OrganiserLoginDto organiserLoginDto ){
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
    ResponseEntity<Optional<Organiser>> getOrganiserById(@PathVariable Long org_id){
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
