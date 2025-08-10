package com.project.entities;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Customer extends BaseEntity{

    @Column(name = "customer_name")
    @NotNull
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    @NotNull
    private GenderEnum gender;
    
    @Transient // Used only in Java, not saved in DB
    private UserRole role;


}
