package com.example.intervaltimer;

import java.util.ArrayList;
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
        int totSec = 0;
        // Get all times from all timers in workout
        for (int i = 0; i  < masterList.size(); i++) {
            totSec += masterList.get(i).getTotalTime();
        }
        return totSec;
    }

    // Add timer to end of list
    public void add(WorkoutItem item) {
        masterList.add(item);
    }

    // Remove timer at given index or given obj.
    public void remove(WorkoutItem item) { masterList.remove(item);}
    public  void remove(int position) { masterList.remove(position); }


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
