package com.misztal.elevatorsystem.modelTests;

import com.misztal.elevatorsystem.model.Elevator;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorTests {

    @Test
    public void givenElevator_whenFindTimeDistance_thenReturnTime1() {
        Elevator elevator = new Elevator();
        elevator.setCurrent(6);
        elevator.setTargets(Arrays.asList(3, 4, 8, 10, 9, 1, 5));
        int actualTime = elevator.findTimeDistance(5, -1);
        int expectedTime = 12;
        assertEquals(expectedTime, actualTime);
    }

    @Test
    public void givenElevator_whenFindTimeDistance_thenReturnTime2() {
        Elevator elevator = new Elevator();
        elevator.setCurrent(4);
        elevator.setTargets(Collections.singletonList(6));
        int actualTime = elevator.findTimeDistance(5, -1);
        int expectedTime = 4;
        assertEquals(expectedTime, actualTime);
    }
}
