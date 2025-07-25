package com.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entities.Booking;

public interface BookingDAO extends JpaRepository<Booking, Long> {


	List<Booking> findByCstId(long cstId);

	List<Booking> findByEvtId(long eventId);

	Optional<Booking> findById(long bookingId);


	
}