package com.project.entities;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	    
	    private String email;
	    private String password;
	    private UserRole role;
	    private boolean isActive;

	    // Constructor for Customer
	    public CustomUserDetails(Customer customer) {
	        this.email = customer.getEmail();
	        this.password = customer.getPassword();
	        this.role = UserRole.ROLE_CUSTOMER;
	        this.isActive = !customer.isDeleted(); // Assuming deleted = inactive
	    }

	    // Constructor for Organiser
	    public CustomUserDetails(Organiser organiser) {
	        this.email = organiser.getEmail();
	        this.password = organiser.getPassword();
	        this.role = UserRole.ROLE_ORGANISER;
	        this.isActive = !organiser.isDeleted(); // Assuming deleted = inactive
	    }
    
	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return List.of(new SimpleGrantedAuthority(role.name()));
	    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can customize based on your entity
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Customize if you store lock status in UserEntity
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Customize if you track password expiry
    }

    @Override
    public boolean isEnabled() {
        return isActive;  //  isActive
    }
}
