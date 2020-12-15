package com.example.intervaltimer;

import android.util.Log;

import java.util.UUID;

// Basic Timer class. Holds the name, id, time, and parent set name if exists.
// Note: This class must be put into a WorkoutItem in order to be put into a workout.
public class Timer extends TimeUnit {
    // Name, iterations, UUID, Min, and Sec inherited

    // Vars for set management
    private String parentName = null; // Name of set if in one else null
    // Used to hold number of iterations the parent has for running timer list
    private int parentIterations, parentTimerCount;

    // Timer obj constructor
    public Timer(String name, int minutes, int seconds) {
        if ((minutes <= 0) && (seconds <= 0)) {
            //Log.e("Timer", "Attempted to create timer with invalid time value"); // Log not mocked
            throw new IllegalArgumentException("Min and sec value <= 0");
        }
        Name = name;
        Minutes = minutes;
        Seconds = seconds;
        // Default iterations = 1
    }

    // Overload constructor when non-default(!=1) iteration given
    public Timer(String name, int minutes, int seconds, int iterations) {
        Name = name;
        Minutes = minutes;
        Seconds = seconds;
        Iterations = iterations;
    }

    public int getParentIterations() {
        return parentIterations;
    }

    public void setParentIterations(int parentIterations) {
        this.parentIterations = parentIterations;
    }

    public int getParentTimerCount() {
        return parentTimerCount;
    }

    public void setParentTimerCount(int parentTimerCount) {
        this.parentTimerCount = parentTimerCount;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    // Return type for adapter
    @Override
    public int getType() {
        return TYPE_TIMER;
    }
}
