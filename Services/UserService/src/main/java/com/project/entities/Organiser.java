package com.project.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Organiser extends BaseEntity{
	
	
    @Column(name = "company_name",unique = true)
    private String organiserCompanyName;

    @Column(name = "image_url")
    @NotNull
    private String imageURL;

//    @Transient
//    List<Events> events;
}
