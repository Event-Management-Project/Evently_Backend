package com.project.dto;

import com.project.entities.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String customerName;
    private GenderEnum gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
}
