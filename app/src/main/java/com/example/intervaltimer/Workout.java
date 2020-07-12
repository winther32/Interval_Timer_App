package com.example.intervaltimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

// Outtermost obj in arch
// Made up of Sets and timers
public class Workout {
    // Set group;
    ArrayList<TimeUnit> masterList; // Contains (S)sets and timers

    // For early dev only using timers. (S)sets not implemented
    protected ArrayList<Timer> timerList; // iterable list of only timers

    int position = 0;
    String workoutName = "Workout"; // Default Name
    //String level = "1"; // difficulty level
    UUID ID = UUID.randomUUID(); // Make sure that each workout is unique

    /////// Constructors ////////////
    public Workout() {
        timerList = new ArrayList<>();
    }

    //////// Methods ////////////////

    // Returns the total run time of workout in SECONDS.
    public int getTotalTime() {
        int countMin = 0;
        int countSec = 0;
        // Get all times from all timers in workout
        for (int i = 0; i  < timerList.size(); i++) {
            countMin += timerList.get(i).Minutes;
            countSec += timerList.get(i).Seconds;
        }
        countSec += (countMin * 60); // Convert to sec
        return countSec;
    }

    // Add timer to end of list
    public void add(Timer unit) {
        timerList.add(unit);
    }

    // Remove timer at given index
    public void remove(int position) { timerList.remove(position);}

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
    public Timer get(int index) {
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

    // Boolean if workout is empty or not (timerList empty)
    public boolean empty() { return timerList.isEmpty();}

    // Return an ArrayList of tail of the timerList (ie without first timer)
    public ArrayList<Timer> initNextTimers() {
        ArrayList<Timer> nextTimers = new ArrayList<>();
        int timeLength = timerList.size();
        for (int i = 1; i < timeLength; i++) {
            nextTimers.add(timerList.get(i));
        }
        return nextTimers;
    }
}
