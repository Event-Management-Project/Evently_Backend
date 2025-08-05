package com.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganiserUpdateDto {
    private String organiserCompanyName;
    private String phoneNumber;
    private String email;
    private String address;
}
