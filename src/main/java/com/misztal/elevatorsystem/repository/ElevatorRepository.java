package com.misztal.elevatorsystem.repository;

import com.misztal.elevatorsystem.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ElevatorRepository extends JpaRepository<Elevator, Integer> {

}
