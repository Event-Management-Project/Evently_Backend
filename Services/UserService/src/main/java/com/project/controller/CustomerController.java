package com.project.controller;

import com.project.dto.ChangePasswordDto;
import com.project.dto.CustomerCreateDto;
import com.project.dto.CustomerDto;
import com.project.dto.CustomerLoginDto;
import com.project.service.CustomerService;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<CustomerDto> registerCustomer(@RequestBody CustomerCreateDto customer){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(customerService.saveCustomer(customer));
    }

    @PostMapping("/login")
    ResponseEntity<CustomerDto> loginCustomer(@RequestBody CustomerLoginDto customerLoginDto ){
        return ResponseEntity.status(HttpStatus.OK).
                body(customerService.validateCustomer(customerLoginDto));
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

    // Fetch Customer By ID
    @GetMapping("/customers/{cstId}")
    ResponseEntity<?> eventAttendees(@PathVariable Long cstId){
        return ResponseEntity.status(HttpStatus.OK).
                body(customerService.getCustomerById(cstId));
    }
    


}
