package com.misztal.elevatorsystem.controller;

import com.misztal.elevatorsystem.model.Elevator;
import com.misztal.elevatorsystem.model.PickUpRequest;
import com.misztal.elevatorsystem.service.ElevatorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Math.min;

@RestController
@RequestMapping("/elevator")
@CrossOrigin("http://localhost:3000")
public class ElevatorController {

    private final ElevatorService elevatorService;

    public ElevatorController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @PostMapping("/add")
    public String add(@RequestBody Elevator elevator){
        elevatorService.saveElevator(elevator);
        return "New elevator added";
    }

    @PostMapping("/initialize")
    public String initialize(@RequestBody Integer numberOfElevators){
        int maxNumber = min(numberOfElevators, 16);
        for (int i = 0; i < maxNumber; i++){
            elevatorService.saveElevator(new Elevator());
        }
        return String.format("New %d elevators initialized", numberOfElevators);
    }

    @GetMapping("/getAll")
    public List<Elevator> getAllElevators(){
        return elevatorService.getAllElevators();
    }

    @PutMapping("/pickUp")
    public String pickUp(@RequestBody PickUpRequest request) {
        elevatorService.pickup(request.getFloor(), request.getDirection());
        return String.format("Elevator is coming to %d floor", request.getFloor());
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable int id, @RequestBody Elevator elevator) {
        elevatorService.update(id, elevator.getCurrent(), elevator.getTargets());
        return String.format("Elevator %d changed position to %d floor and targets to ", id, elevator.getCurrent())
                + elevator.getTargets().toString();
    }

    @PutMapping("/updateCurrent/{id}")
    public String updateCurrent(@PathVariable int id, @RequestBody int current) {
        elevatorService.updateCurrent(id, current);
        return String.format("Elevator %d changed position to %d floor", id, current);
    }

    @PutMapping("/updateTargets/{id}")
    public String updateTargets(@PathVariable int id, @RequestBody List<Integer> targets) {
        elevatorService.updateTargets(id, targets);
        return String.format("Elevator %d changed position to", id) + targets.toString();
    }

    @PutMapping("/step")
    public String step() {
        elevatorService.step();
        return "Every elevator made simulation step";
    }

    @PutMapping("/addTarget/{id}")
    public String addTarget(@PathVariable int id, @RequestBody int target) {
        elevatorService.addTarget(id, target);
        return String.format("Target %d added for elevator %d", target, id);
    }

    @GetMapping("/status")
    public String status() {
        return elevatorService.status().toString();
    }

    @DeleteMapping("/reset")
    public String reset() {
        elevatorService.reset();
        return "All elevators removed";
    }
}
