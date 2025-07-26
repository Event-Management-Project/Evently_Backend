package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Ticket;

public interface TicketDAO extends JpaRepository<Ticket,Long>{

}

