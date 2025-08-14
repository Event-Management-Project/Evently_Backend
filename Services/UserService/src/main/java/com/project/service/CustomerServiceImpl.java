package com.project.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.project.config.JwtUtil;
import com.project.dao.CustomerDao;
import com.project.dto.ApiResponse;
import com.project.dto.ChangePasswordDto;
import com.project.dto.CustomerCreateDto;
import com.project.dto.CustomerDto;
import com.project.dto.JwtRequest;
import com.project.dto.JwtResponse;
import com.project.entities.Customer;
import com.project.entities.UserRole;
import com.project.exceptions.ChangePasswordException;
import com.project.exceptions.ResourseNotFound;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDao customerDao;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private JwtUtil jwtUtil;
  

	@Override
	public CustomerDto saveCustomer(CustomerCreateDto customerCreateDto) {

	    // Map DTO → Entity
	    Customer customer = modelMapper.map(customerCreateDto, Customer.class);

	    // Hash password & set defaults
	    customer.setPassword(passwordEncoder.encode(customerCreateDto.getPassword()));
	    customer.setDeleted(false);
	    customer.setRole(UserRole.ROLE_CUSTOMER);

	    // Save entity
	    customerDao.save(customer);//This forces Hibernate to hit the DB immediately and populate the generated ID.
	    System.out.println("Saved customer ID: " + customer.getId());
	    
	    // Map Entity → Response DTO
	    CustomerDto dto = modelMapper.map(customer, CustomerDto.class);
	    dto.setCstId(customer.getId());
	    return dto;
	}


	 @Override
	    public JwtResponse loginCustomer(JwtRequest loginDto) {
	        // Step 1: Authenticate user
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
	        );

	        // Step 2: Set context
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // Step 3: Generate JWT token
	        String token = jwtUtil.createToken(authentication);

	        // Step 4: Fetch Customer entity
	        Customer customer = customerDao.findByEmail(loginDto.getEmail())
	                .orElseThrow(() -> new ResourseNotFound("Customer not found with email: " + loginDto.getEmail()));

	        // Step 5: Map to DTO
	        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
	        customerDto.setCstId(customer.getId()); 
	        // Step 6: Return JWT Response
	        return new JwtResponse(token, loginDto.getEmail(), "ROLE_CUSTOMER", customerDto, null);
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
		customerDto.setCstId(existingCustomer.getId());

		existingCustomer.setUpdatedOn(LocalDateTime.now());

		customerDao.save(existingCustomer);

		return new ApiResponse("Customer details updated successfully for ID: " + existingCustomer.getId());
	}

	@Override
	public ApiResponse changePassword(Long id, ChangePasswordDto changePasswordto) {
		Customer customer = customerDao.findById(id).orElseThrow(() -> new ResourseNotFound("Organiser not found"));

		 // 1️ Verify current password
	    if (!passwordEncoder.matches(changePasswordto.getCurrentPassword(), customer.getPassword())) {
	        throw new ChangePasswordException("Current password is incorrect");
	    }

	    // 2️ Verify new password and confirm password match
	    if (!changePasswordto.getNewPassword().equals(changePasswordto.getConfirmPassword())) {
	        throw new ChangePasswordException("New password and confirm password do not match");
	    }

	    // 3️ Prevent same as old password
	    if (passwordEncoder.matches(changePasswordto.getNewPassword(), customer.getPassword())) {
	        throw new ChangePasswordException("New password cannot be the same as the current password");
	    }

	    // 4️ Encode new password before saving
	    customer.setPassword(passwordEncoder.encode(changePasswordto.getNewPassword()));
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


	@Override
	public Long getCustomerCount() {
		return customerDao.count();
	}
}
