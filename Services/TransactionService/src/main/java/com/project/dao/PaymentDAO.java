package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Payment;

public interface PaymentDAO extends JpaRepository<Payment,Long>{

}
