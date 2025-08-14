package com.project.controller;

import com.project.dto.ChangePasswordDto;
import com.project.dto.CustomerCreateDto;
import com.project.dto.CustomerDto;
import com.project.service.CustomerService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;

    @GetMapping
    ResponseEntity<?> getAllCustomers(){
    	return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }
    
    @PostMapping("/register")
    ResponseEntity<CustomerDto> registerCustomer(@RequestBody CustomerCreateDto customerdto){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(customerService.saveCustomer(customerdto));
    }



    @PutMapping("/update/{customerId}")
    ResponseEntity<?> updateProfile(@PathVariable Long customerId, @RequestBody CustomerDto customerDto){
        return ResponseEntity.status(HttpStatus.OK).
                body(customerService.updateProfile(customerId,customerDto));
    }

    @PutMapping("/changePassword/{id}")
    ResponseEntity<?>  updatePassword(@PathVariable Long id,@RequestBody ChangePasswordDto dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerService.changePassword(id,
                dto));
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> removeCustomer(@PathVariable Long id){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(customerService.deleteCustomer(id));
    }

    @PreAuthorize("hasAnyRole('ORGANISER', 'CUSTOMER')")
    // Fetch Customer By ID
    @GetMapping("/customers/{cstId}")
    ResponseEntity<?> eventAttendees(@PathVariable Long cstId){
        return ResponseEntity.status(HttpStatus.OK).
                body(customerService.getCustomerById(cstId));
    }
    
    @GetMapping("/count-customer")
    ResponseEntity<Long> getCustomerCount(){
    	return ResponseEntity.ok(customerService.getCustomerCount());
    }

}
