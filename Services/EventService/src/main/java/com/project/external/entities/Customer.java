package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	private String customerName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
}
