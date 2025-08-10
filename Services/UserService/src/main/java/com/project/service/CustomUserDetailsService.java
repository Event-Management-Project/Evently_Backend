package com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.dao.CustomerDao;
import com.project.dao.OrganiserDao;
import com.project.entities.CustomUserDetails;
import com.project.entities.Customer;
import com.project.entities.Organiser;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerDao customerRepo;

    @Autowired
    private OrganiserDao organiserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
        // Try finding in Customer table first
        Optional<Customer> optionalCustomer = customerRepo.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            return new CustomUserDetails(optionalCustomer.get());
        }

        // If not found in Customer, try Organiser
        Optional<Organiser> optionalOrganiser = organiserRepo.findByEmail(email);
        if (optionalOrganiser.isPresent()) {
            return new CustomUserDetails(optionalOrganiser.get());
        }

        // If not found in either, throw
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

