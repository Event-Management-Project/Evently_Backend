package com.project.controller;

import com.project.config.JwtUtil;
import com.project.dao.CustomerDao;
import com.project.dao.OrganiserDao;
import com.project.dto.CustomerCreateDto;
import com.project.dto.CustomerDto;
import com.project.dto.JwtRequest;
import com.project.dto.JwtResponse;
import com.project.dto.OrganiserCreateDto;
import com.project.dto.OrganiserDto;
import com.project.entities.Customer;
import com.project.entities.Organiser;
import com.project.entities.UserRole;
import com.project.exceptions.ResourseNotFound;
import com.project.service.CustomerService;
import com.project.service.OrganiserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")

@RequestMapping("/users")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    OrganiserService organiserService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrganiserDao organiserDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
	ModelMapper modelMapper;
    
  

    // ---------- LOGIN ----------
    @PostMapping("/login/customer")
    public ResponseEntity<?> customerLogin(@RequestBody JwtRequest loginDto) {
        try {
            JwtResponse response = customerService.loginCustomer(loginDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    
    // ========================= ORGANISER LOGIN =========================
    @PostMapping("/login/organiser")
    public ResponseEntity<?> organiserLogin(@RequestBody JwtRequest loginDto) {
        try {
            JwtResponse response = organiserService.loginOrganiser(loginDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
    
    // ---------- REGISTER CUSTOMER ----------
    @PostMapping("/register/customer")
    @Transactional
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerCreateDto dto) {
    	
        if (customerDao.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered as Customer");
        }
        // check if role is already registered
        if (organiserDao.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered for another role");
        }
    
        return ResponseEntity.status(HttpStatus.CREATED).
                body(customerService.saveCustomer(dto));
    }

    
 // ---------- REGISTER ORGANISER ----------
    @PostMapping(value = "/register/organiser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Adds a new organiser to the system with basic details")
    @Transactional
    public ResponseEntity<?> registerOrganiser(@ModelAttribute OrganiserCreateDto organiserCreateDto) {

        // Check if email is already registered
        if (organiserDao.findByEmail(organiserCreateDto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already registered as Organiser");
        }
        // check if role is already registered
        if (customerDao.findByEmail(organiserCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered for another role");
        }
                
        // Save organiser using the service method (handles hashing & image upload)
        OrganiserDto savedOrganiser = organiserService.saveOrganiser(organiserCreateDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedOrganiser);
    }

}
