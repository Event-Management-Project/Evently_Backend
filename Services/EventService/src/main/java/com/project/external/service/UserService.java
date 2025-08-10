package com.project.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.project.external.entities.Customer;
import com.project.external.entities.Organiser;

@FeignClient(name = "USERSERVICE")
public interface UserService {
	@GetMapping("/customer/customers/{cstId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long cstId );//,@RequestHeader("Authorization") String token);
	
	@GetMapping("organiser/organiser/{orgId}")
	public ResponseEntity<Organiser> getOrganiser(@PathVariable Long orgId); //,@RequestHeader("Authorization") String token); 
}
