package com.example.intervaltimer;

import java.util.ArrayList;

// Outtermost obj in arch
// Made up of Sets and timers
public class Workout {
    // Set group;
    //ArrayList<TimeUnit> masterList; // Contains (S)sets and timers

    // For early dev only using timers. (S)sets not implemented
    protected ArrayList<Timer> timerList; // iterable list of only timers
    int position = 0;

    /////// Constructors ////////////
    public Workout() {
        timerList = new ArrayList<>();
    }

    //////// Methods ////////////////

    // Add timer to end of list
    public void add(Timer unit) {
        timerList.add(unit);
    }

    // Iterate the position in workout
    public void next() {
        // Check to ensure use of next() will not create out of range index
        if (position < (timerList.size()-1)) {
            position++;
        }
    }

    // Gets the timer at current position
    public Timer currentTimer() {
        // Good idea to have a check like below
        //assert (0 <= position && position < masterList.size());
        return timerList.get(position);
    }

    // Get a timer at a given pos
    public TimeUnit get(int index) {
        //TODO: assert that index in range of masterList
        return timerList.get(index);
    }

    // Tries to return the next timer and iterate position. If no timer next, returns null.
    public Timer move_and_get() {
        if (position + 1 >= timerList.size()) {
            return null;
        } else {
            return timerList.get(++position);
        }
    }

    // Resets the position of the workout to the start
    public void restart() {
        position = 0;
    }

    // Returns the size of the workout (ie number of timers)
    public int size() {
        return timerList.size();
    }
}
