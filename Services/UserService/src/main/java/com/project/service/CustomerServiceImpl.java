package com.project.service;

import com.project.dao.CustomerDao;
import com.project.dto.ApiResponse;
import com.project.dto.ChangePasswordDto;
import com.project.dto.CustomerDto;
import com.project.dto.CustomerLoginDto;
import com.project.entities.Customer;
import com.project.entities.Organiser;
import com.project.exceptions.ChangePasswordException;
import com.project.exceptions.ResourseNotFound;

import jakarta.ws.rs.BadRequestException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDao customerDao;
	@Autowired
	ModelMapper modelMapper;

	@Override
	public ApiResponse saveCustomer(CustomerDto customer) {
		Customer customer1 = modelMapper.map(customer, Customer.class);
		customerDao.save(customer1);
		return new ApiResponse("Customer created "  ,customer);
	}

	@Override
	public ApiResponse validateCustomer(CustomerLoginDto customerLoginDto) {
		Customer customer = customerDao.findByEmail(customerLoginDto.getEmail()).orElseThrow(
				() -> new ResourseNotFound("Customer not found with email: " + customerLoginDto.getEmail()));

		if (!customer.getPassword().equals(customerLoginDto.getPassword())) {
			throw new ResourseNotFound("Invalid password");
		}

		return new ApiResponse("Login successful for customer: ", customer);
	}

	@Override
	public List<Customer> getAllCustomerForEvent(Long id) {
		return customerDao.findAll().stream().filter(customer -> !customer.isDeleted()).toList();
	}

	@Override
	public ApiResponse updateProfile(Long id, CustomerDto customerDto) {
		Customer existingCustomer = customerDao.findById(id)
				.orElseThrow(() -> new ResourseNotFound("Customer not found with id " + id));

		modelMapper.map(customerDto, existingCustomer);

		existingCustomer.setUpdatedOn(LocalDateTime.now());

		customerDao.save(existingCustomer);

		return new ApiResponse("Customer details updated successfully for ID: " + existingCustomer.getId());
	}

	@Override
	public ApiResponse changePassword(Long id, ChangePasswordDto changePasswordto) {
		Customer customer = customerDao.findById(id).orElseThrow(() -> new ResourseNotFound("Organiser not found"));

		if (!customer.getPassword().equals(changePasswordto.getCurrentPassword())) {
			throw new ChangePasswordException("Current password is incorrect");
		}

		if (!changePasswordto.getNewPassword().equals(changePasswordto.getConfirmPassword())) {
			throw new ChangePasswordException("New password and confirm password do not match");
		}

		if (changePasswordto.getCurrentPassword().equals(changePasswordto.getNewPassword())) {
			throw new ChangePasswordException("New password cannot be the same as the current password");
		}

		customer.setPassword(changePasswordto.getNewPassword());
		customerDao.save(customer);

		return new ApiResponse("Password updated successfully for organiser ID: " + customer.getId());
	}

	@Override
	public ApiResponse deleteCustomer(Long id) {
		Customer customer = customerDao.findById(id).orElseThrow(() -> new ResourseNotFound("Customer Not Found"));
		customer.setDeleted(true);
		customerDao.save(customer);
		return new ApiResponse("Customer deleted successfully " + customer.getId());
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerDao.findAll();
	}
	
	@Override
	public CustomerDto getCustomerById(Long cstId) {
		Customer customer = customerDao.findById(cstId).orElseThrow();
		
		CustomerDto customerResp = modelMapper.map(customer, CustomerDto.class); 
		
		return customerResp;
	}
}
