package com.project.dto;

import com.project.entities.GenderEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCreateDto {
    @NotNull
    private String customerName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private String password;

    @NotNull
    private GenderEnum gender;
}