package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer,Long> {
	
    Optional<Customer> findById(Long id);
    Optional<Customer> findByEmail(String email);
    List<Customer> findByGender(String gender);
    Optional<Customer> findByCustomerName(String name);


}
