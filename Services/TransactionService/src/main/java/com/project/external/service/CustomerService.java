package com.sunbeam.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sunbeam.external.entities.Customer;

@FeignClient(name = "UserService", url = "http://localhost:9091")
public interface CustomerService {
	@GetMapping("/customer/customers/{cstId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long cstId);
}