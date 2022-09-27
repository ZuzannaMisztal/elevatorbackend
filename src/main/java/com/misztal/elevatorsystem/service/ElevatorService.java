package com.misztal.elevatorsystem.service;

import com.misztal.elevatorsystem.model.Elevator;
import org.javatuples.Triplet;
import java.util.List;

public interface ElevatorService {
    void saveElevator(Elevator elevator);
    List<Elevator> getAllElevators();
    void pickup(int floor, int direction);
    void update(int id, int current, List<Integer> targets);
    void updateCurrent(int id, int current);
    void updateTargets(int id, List<Integer> targets);
    void step();
    List<Triplet<Integer, Integer, List<Integer>>> status();
    void addTarget(int id, int target);
    void reset();
}
