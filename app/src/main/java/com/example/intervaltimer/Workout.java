package com.example.intervaltimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

// Outtermost obj in arch
// Made up of Sets and timers
public class Workout {
    // Set group;
    ArrayList<WorkoutItem> masterList; // Contains (S)sets and timers

    // For early dev only using timers. (S)sets not implemented
    //protected ArrayList<Timer> timerList; // iterable list of only timers

//    int position = 0; // Moved to Run Workout activity.

    String workoutName = "Workout"; // Default Name
    //String level = "1"; // difficulty level
    UUID ID = UUID.randomUUID(); // Make sure that each workout is unique

    /////// Constructors ////////////
    public Workout() {
        masterList = new ArrayList<>();
    }

    //////// Methods ////////////////

    // Returns the total run time of workout in SECONDS.
    public int getTotalTime() {
        int countMin = 0;
        int countSec = 0;
        // Get all times from all timers in workout
        for (int i = 0; i  < masterList.size(); i++) {
            countMin += masterList.get(i).getMin();
            countSec += masterList.get(i).getSec();
        }
        countSec += (countMin * 60); // Convert to sec
        return countSec;
    }

    // Add timer to end of list
    public void add(WorkoutItem item) {
        masterList.add(item);
    }

    // Remove timer at given index
    public void remove(int position) { masterList.remove(position);}

    ////// All following commented methods functionality is moving to Run activity

//    // Iterate the position in workout
//    public void next() {
//        // Check to ensure use of next() will not create out of range index
//        if (position < (timerList.size()-1)) {
//            position++;
//        }
//    }
//    // Gets the timer at current position
//    public Timer currentTimer() {
//        // Good idea to have a check like below
//        //assert (0 <= position && position < masterList.size());
//        return timerList.get(position);
//    }
//
//    // Tries to return the next timer and iterate position. If no timer next, returns null.
//    public Timer move_and_get() {
//        if (position + 1 >= timerList.size()) {
//            return null;
//        } else {
//            return timerList.get(++position);
//        }
//    }
//
//    // Resets the position of the workout to the start
//    public void restart() {
//        position = 0;
//    }

    // Get a TimeUnit at a given pos
    public WorkoutItem get(int index) {
        //TODO: assert that index in range of masterList
        return masterList.get(index);
    }

    // Returns the size of the workout (ie number of TimeUnits)
    public int size() {
        return masterList.size();
    }

    // Boolean if workout is empty or not (timerList empty)
    public boolean empty() { return masterList.isEmpty();}


}
