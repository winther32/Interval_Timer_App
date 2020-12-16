package com.example.intervaltimer;

import java.util.UUID;

import static com.example.intervaltimer.TimeUnit.TYPE_SET;
import static com.example.intervaltimer.TimeUnit.TYPE_TIMER;

// This is a wrapper class for Timers and Sets so they can both be in the same ArrayList
// A workoutItem cannot contain both a Timer and Set. But must contain exactly one timer or set
public class WorkoutItem {
    private UUID ID = UUID.randomUUID();
    private Timer timer;
    private Set set;
    private int active; // Boolean esk var to determine what is in the item, set or Timer

    // Basic Constructors
    public WorkoutItem(Timer T) {
        timer = T;
        set = null;
        active = TYPE_TIMER;
    }

    public WorkoutItem(Set S) {
        set = S;
        timer = null;
        active = TYPE_SET;
    }


    // Methods to change type (probably not needed)
//    public void setItem(Timer T) {
//        timer = T;
//        set = null;
//        active = TYPE_TIMER;
//    }
//
//    public void setItem(Set S) {
//        set = S;
//        timer = null;
//        active = TYPE_SET;
//    }

    // method to retrieve minutes from the held non-null item
    public int getMinutes(){
        if (active == TYPE_SET) {
            return set.getMinutes();
        } else if (active == TYPE_TIMER) {
            return timer.getMinutes();
        } else {
            return -1;
        }
    }

    // method to retrieve seconds from the held non-null item
    public int getSeconds() {
        if (active == TYPE_SET) {
            return set.getSeconds();
        } else if (active == TYPE_TIMER) {
            return timer.getSeconds();
        } else {
            return -1;
        }
    }

    // Method to get total time in sec of active. Takes iterations into account
    public int getTotalTime() {
        if (active == TYPE_TIMER) {
            return timer.getSeconds() + (timer.getMinutes() * 60); // potential to add timer iterations in here
        } else if (active == TYPE_SET) {
            return set.getTotalTime();
        } else {
            return -1;
        }
    }

    // Returns the type int of the WorkoutItem
    public int getType() {
        return active;
    }

    // Accessor methods (can return null)
    public Timer getTimer() {
        return timer;
    }

    // Returns the set value of the item (can return null)
    public Set getSet() {
        return set;
    }

    // Used to get the generics or null;
    public TimeUnit getActive() {
        if (active == TYPE_TIMER) {
            return timer;
        } else { // (active == TYPE_SET) {
            return set;
        }
    }

    public UUID getID() {
        return ID;
    }
}
