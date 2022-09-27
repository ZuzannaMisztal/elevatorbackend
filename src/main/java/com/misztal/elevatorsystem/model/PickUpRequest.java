package com.misztal.elevatorsystem.model;


/**
 * Auxiliary class used to request pick up from selected floor up or down.
 */
public class PickUpRequest {

    /**
     * Requested floor.
     */
    private final int floor;
    /**
     * Requested direction: 1 for up and -1 for down.
     */
    private final int direction;

    public PickUpRequest(int floor, int direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public int getDirection() {
        return direction;
    }
}
