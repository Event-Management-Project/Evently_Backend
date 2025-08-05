package com.project.service;

import com.project.dto.ApiResponse;
import com.project.dto.ChangePasswordDto;
import com.project.dto.CustomerCreateDto;
import com.project.dto.CustomerDto;
import com.project.dto.CustomerLoginDto;
import com.project.entities.Customer;

import java.util.List;

public interface CustomerService {
	CustomerDto saveCustomer(CustomerCreateDto customer);
	CustomerDto validateCustomer(CustomerLoginDto customerDetails);
    List<Customer> getAllCustomerForEvent(Long id);
    ApiResponse updateProfile(Long id, CustomerDto customerDto);
    ApiResponse changePassword(Long id, ChangePasswordDto changePasswordDto);
    ApiResponse deleteCustomer(Long id);
	List<Customer> getAllCustomers();
	CustomerDto getCustomerById(Long cstId);
}
