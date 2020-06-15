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

    //Constructors
    public Workout() {
        timerList = new ArrayList<>();
    }

    //Methods
    public void add(Timer unit) {
        timerList.add(unit);
    }

    public void next() {
        // Check to ensure use of next() will not create out of range index
        if (position < (timerList.size()-1)) {
            position++;
        }
    }

    public Timer currentTimer() {
        // Good idea to have a check like below
        //assert (0 <= position && position < masterList.size());
        return timerList.get(position);
    }

    public TimeUnit get(int index) {
        //TODO: assert that index in range of masterList
        return timerList.get(index);
    }
}
