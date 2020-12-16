package com.example.intervaltimer;

import android.util.Log;

import java.util.UUID;

// The abstract parent time class for sets and timers
// Timers, Sets, and super sets are all Time units
// All time unit objects are unique by random UUID
public abstract class TimeUnit {
    protected int Iterations = 1;
    protected String Name;
    // Make sure that each unit is unique
    protected UUID ID =  UUID.randomUUID();
    protected int Minutes, Seconds;

    // Define distinguishing types for subclasses.
    public static final int TYPE_TIMER = 0;
    public static final int TYPE_SET = 1;
    public static final int TYPE_SUPER_SET = 2;

    abstract public int getType();

    // Generic getters/setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getMinutes() {
        return Minutes;
    }

    // Do nothing if given value is negative
    public void setMinutes(int minutes) {
        if (minutes >=0) {
            Minutes = minutes;
        } else {
            throw new IllegalArgumentException("Minutes given < 0");
        }
    }

    public int getSeconds() {
        return Seconds;
    }

    // Negative value protections (do nothing)
    public void setSeconds(int seconds) {
        if (seconds >= 0) {
            if (seconds < 60) {
                Seconds = seconds;
            } else {
                throw new IllegalArgumentException("Seconds given > 59");
            }
        } else {
            throw new IllegalArgumentException("Seconds given < 0");
        }
    }

    public UUID getID() {
        return ID;
    }

    public int getIterations() {
        return Iterations;
    }

    public void setIterations(int iterations) {
        Iterations = iterations;
    }
}
