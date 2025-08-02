package com.project.external.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.external.entities.Customer;

@FeignClient(name = "UserService", url = "http://localhost:9091", configuration = FeignClientConfiguration.class)
public interface CustomerService {
	@GetMapping("/customer/customers/{cstId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long cstId);
}
