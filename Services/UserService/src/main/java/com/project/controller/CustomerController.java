package com.project.controller;

import com.project.dto.CustomerDto;
import com.project.dto.CustomerLoginDto;
import com.project.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    ResponseEntity<?> getAllCustomers(){
    	return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }
    
    @PostMapping("/register")
    ResponseEntity<?> registerCustomer(@RequestBody CustomerDto customer){
        return ResponseEntity.status(HttpStatus.CREATED).
                body(customerService.saveCustomer(customer));
    }

    @PostMapping("/login")
    ResponseEntity<?> loginCustomer(@RequestBody CustomerLoginDto customerLoginDto ){
        return ResponseEntity.status(HttpStatus.OK).
                body(customerService.validateCustomer(customerLoginDto));
    }


    @PutMapping("/update/{customerId}")
    ResponseEntity<?> updateProfile(@PathVariable Long customerId, @RequestBody CustomerDto customerDto){
        return ResponseEntity.status(HttpStatus.OK).
                body(customerService.updateProfile(customerId,customerDto));
    }

    @PutMapping("/changePassword/{id}")
    ResponseEntity<?>  updatePassword(@PathVariable Long id,@RequestParam String password){
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerService.changePassword(id,
                password));
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
