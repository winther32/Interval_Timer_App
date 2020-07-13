package com.example.intervaltimer;

import android.os.Debug;
import android.util.Log;

import java.util.UUID;

import static com.example.intervaltimer.TimeUnit.TYPE_SET;
import static com.example.intervaltimer.TimeUnit.TYPE_TIMER;

public class WorkoutItem {
    UUID ID = UUID.randomUUID();
    public Timer timer;
    public Set set;
    public int active;

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


    // Methods to change type
    public void setItem(Timer T) {
        timer = T;
        set = null;
        active = TYPE_TIMER;
    }

    public void setItem(Set S) {
        set = S;
        timer = null;
        active = TYPE_SET;
    }

    // method to retrieve minutes from the held non-null item
    public int getMin(){
        if (active == TYPE_SET) {
            return set.Minutes;
        } else if (active == TYPE_TIMER) {
            return timer.Minutes;
        } else {
            return -1;
        }
    }

    // method to retrieve seconds from the held non-null item
    public int getSec() {
        if (active == TYPE_SET) {
            return set.Seconds;
        } else if (active == TYPE_TIMER) {
            return timer.Seconds;
        } else {
            return -1;
        }
    }

    public int getType() {
        return active;
    }

    // Accessor methods (can return null)
    public Timer getTimer() {
        if (active == TYPE_TIMER) {
            return timer;
        } else { return null; }
    }

    public Set getSet() {
        if (active == TYPE_SET) {
            return set;
        } else { return null; }
    }

}
