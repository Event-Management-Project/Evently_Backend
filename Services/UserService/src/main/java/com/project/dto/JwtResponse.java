package com.project.dto;

import com.project.entities.Customer;
import com.project.entities.Organiser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private String jwtToken;
    private String email;
    private String role; // optional, can be used to show user role (CUSTOMER/ORGANISER)
    private CustomerDto customer;     // Nullable, sent only if role is CUSTOMER
    private OrganiserDto organiser;   // Nullable, sent only if role is ORGANISER

}
