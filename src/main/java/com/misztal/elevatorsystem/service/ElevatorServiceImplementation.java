package com.misztal.elevatorsystem.service;

import com.misztal.elevatorsystem.model.Elevator;
import com.misztal.elevatorsystem.repository.ElevatorRepository;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElevatorServiceImplementation implements ElevatorService{

    private final ElevatorRepository elevatorRepository;

    public ElevatorServiceImplementation(ElevatorRepository elevatorRepository) {
        this.elevatorRepository = elevatorRepository;
    }

    @Override
    public void saveElevator(Elevator elevator) {
        elevatorRepository.save(elevator);
    }

    @Override
    public List<Elevator> getAllElevators() {
        return elevatorRepository.findAll();
    }

    @Override
    public void pickup(int floor, int direction) {
        Elevator bestElevator = findBestElevator(floor, direction);
        elevatorRepository.findById(bestElevator.getId())
                .map(elevator -> {
                    elevator.addTarget(floor);
                    return elevatorRepository.save(elevator);
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void update(int id, int current, List<Integer> targets) {
        elevatorRepository.findById(id)
                .map(elevator -> {
                    elevator.setCurrent(current);
                    elevator.setTargets(targets);
                    return elevatorRepository.save(elevator);
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateCurrent(int id, int current) {
        elevatorRepository.findById(id)
                .map(elevator -> {
                    elevator.setCurrent(current);
                    return elevatorRepository.save(elevator);
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateTargets(int id, List<Integer> targets) {
        elevatorRepository.findById(id)
                .map(elevator -> {
                    elevator.setTargets(targets);
                    return elevatorRepository.save(elevator);
                }).orElseThrow(RuntimeException::new);

    }

    @Override
    public void step() {
        List<Elevator> allElevators = elevatorRepository.findAll();
        for (Elevator elevator:
            allElevators) {
            elevatorRepository.findById(elevator.getId())
                    .map(el -> {
                        el.step();
                        return elevatorRepository.save(el);
                    }).orElseThrow(RuntimeException::new);
        }
    }

    @Override
    public List<Triplet<Integer, Integer, List<Integer>>> status() {
        List<Elevator> allElevators = elevatorRepository.findAll();
        List<Triplet<Integer, Integer, List<Integer>>> result = new ArrayList<>();
        for (Elevator elevator:
             allElevators) {
            result.add(elevator.toTriplet());
        }
        return result;
    }

    @Override
    public void addTarget(int id, int target) {
        elevatorRepository.findById(id)
                .map(elevator -> {
                    elevator.addTarget(target);
                    return elevatorRepository.save(elevator);
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void reset() {
        elevatorRepository.deleteAll();
    }

    private Elevator findBestElevator(int floor, int direction) {
        List<Elevator> allElevators = elevatorRepository.findAll();
        int index = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < allElevators.size(); i++) {
            int pickUpTime = allElevators.get(i).findTimeDistance(floor, direction);
            if (pickUpTime < min) {
                min = pickUpTime;
                index = i;
            }
        }
        return allElevators.get(index);
    }
}
