package com.misztal.elevatorsystem.model;

import org.javatuples.Triplet;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *  Class representing elevator entity.
 */
@Entity
public class Elevator {
    /**
     * Automatically generated, provides identification of elevator object which means no two elevators can have the same id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Number specificating on which floor the elevator currently is.
     */
    private int current;
    /**
     * List of floor numbers to which the elevator is approaching.
     */
    @ElementCollection
    private List<Integer> targets;


    /**
     * Creates new elevator object situated on ground floor with no targets.
     */
    public Elevator() {
        this.current = 0;
        this.targets = new ArrayList<>();
    }

    /**
     * Creates deepcopy of elevator passed as parameter.
     * @param elevator the elevator to be copied.
     */
    public Elevator(Elevator elevator) {
        this.id = elevator.id;
        this.current = elevator.current;
        this.targets = new ArrayList<>();
        this.targets.addAll(elevator.targets);
    }

    /**
     * Returns elevator's identification number.
     * @return elevator's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets elevator's id to given.
     * @param id identification number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns number of floor on which elevator currently is.
     * @return number of current floor.
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Sets elevator's position to given.
     * @param current number of floor to position elevator.
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     * Returns list of elevator's targets.
     * @return list of elevator's targets.
     */
    public List<Integer> getTargets() {
        return targets;
    }

    /**
     * Sets targets based on target list passed as parameter and automatically arranges them in best fitted order.
     * @param targets list of targets to which elevator should arrive.
     */
    public void setTargets(List<Integer> targets) {
        this.targets = new ArrayList<>();
        this.targets.addAll(targets);
        this.reorganiseTargets();
    }

    /**
     * Adds new destination to elevator's list of targets then reorganises the target list.
     * @param target floor number to which the elevator should arrive.
     */
    public void addTarget(int target){
        this.targets.add(target);
        this.reorganiseTargets();
    }

    /**
     * Sets elevator's targets in most optimal way.
     * Firstly duplicate targets are removed. Then: <br>
     * If current floor is below all targets then the targets are sorted ascending. <br>
     * If current floor is above all targets then the targets are sorted descending. <br>
     * If some targets are above the current floor and some are below then:<br>
     * &emsp;initial direction is set based on distance from current floor to the highest target and the lowest target.<br>
     * &emsp;If highest target is closer then the initial direction is up,
     * &emsp;otherwise the initial direction is down.<br>
     * &emsp;All targets above current floor are sorted ascending and all targets below are sorted descending.<br>
     * &emsp;If the initial direction is up then all targets above the current floor will be executed before all below,<br>
     * &emsp;otherwise all lower targets will be executed first.
     */
    public void reorganiseTargets(){
        if (!this.targets.isEmpty()) {
            this.targets = new ArrayList<>(new HashSet<>(this.targets));

            int maxTarget = Collections.max(this.targets);
            if (maxTarget < this.current) {
                this.targets.sort(Collections.reverseOrder());
                return;
            }

            int minTarget = Collections.min(this.targets);
            if (minTarget > this.current) {
                Collections.sort(this.targets);
                return;
            }

            Collections.sort(this.targets);
            int splitIndex = this.findSplitIndex();
            List<Integer> up = this.targets.subList(splitIndex, this.targets.size());
            List<Integer> down = this.targets.subList(0, splitIndex);
            Collections.reverse(down);

            if (Math.abs(this.current - maxTarget) < Math.abs(this.current - minTarget)){
                this.targets = up;
                this.targets.addAll(down);
            } else {
                this.targets = down;
                this.targets.addAll(up);
            }
        }
    }

    /**
     * Finds index of split point in sorted target list for reorganiseTargets() method
     * @return index
     */
    private int findSplitIndex() {
        for (int i = 0; i < this.targets.size(); i++) {
            if (this.targets.get(i) > this.current) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds how much time would take the elevator to arrive at given floor considering that direction in which elevator is going cannot be opposite to given direction.
     * @param floor number of floor to which elevator should arrive.
     * @param direction the direction in which elevator would go after arriving.
     * @return number of time units to elevator arrival.
     */
    public int findTimeDistance(int floor, int direction) {
        Elevator elevator = new Elevator(this);
        int time = 0;
        while (elevator.direction() != direction || (floor - elevator.current) * direction < 0 ) {
            if (elevator.targets.isEmpty()) {
                break;
            }
            elevator.step();
            time += 1;
        }
        time += Math.abs(elevator.current - floor);
        return time;
    }

    /**
     * Returns direction in which the elevator proceeds.
     * @return if there's no targets or elevator is currently opening doors: 0<br>
     * if elevator's next destination is up: 1<br>
     * if elevator's next destination is down: -1
     */
    private int direction() {
        int direction = 0;
        if (this.targets.size() >= 1) { //najbliższy ruch jest zaplanowany
            if (this.current > this.targets.get(0)) { //najbliższy ruch jest w dół
                direction = -1;
            }
            else if (this.current < this.targets.get(0)) { //najbliższy ruch jest w górę
                direction = 1;
            }
        }
        return direction;
    }

    /**
     * Executes one step of simulation.
     */
    public void step(){
        int direction = 0;
        if (!this.targets.isEmpty()) {
            if (this.current < this.targets.get(0)) {
                direction = 1;
            } else if (this.current > this.targets.get(0)) {
                direction = -1;
            }
            this.current += direction;
            if (direction == 0){
                this.targets.remove(0);
            }
        }
    }

    /**
     * Returns current state of the elevator.
     * @return state of elevator in a form of tuple, where first element is elevator id, second is current floor, and third is list of it's targets.
     */
    public Triplet<Integer, Integer, List<Integer>> toTriplet() {
        return new Triplet<>(this.id,
                             this.current,
                             this.targets);
    }
}
