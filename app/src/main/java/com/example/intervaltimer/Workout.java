package com.example.intervaltimer;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

// Outter-most obj. in workout abstraction.
// Made up of Sets and timers
public class Workout {
    // Lists left default access so other tools can manipulate (e.g. drag adapter)
    ArrayList<WorkoutItem> masterList; // Contains (S)sets and timers
    private String workoutName = "Workout"; // Default Name
    //String level = "1"; // difficulty level
    private UUID ID = UUID.randomUUID(); // Make sure that each workout is unique

    public Workout() {
        masterList = new ArrayList<>();
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

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

    // Get a WorkoutItem at a given pos
    public WorkoutItem get(int index) throws IndexOutOfBoundsException {
       if ((index >= masterList.size()) || (index < 0)) {
           Log.e("Debug", "Workout Error: Index requested out of range");
           throw new IndexOutOfBoundsException();
       } else {
           return masterList.get(index);
       }
    }

    // Returns the size of the workout (ie number of WorkoutItems)
    public int size() {
        return masterList.size();
    }

    // Boolean if workout is empty or not (masterList empty)
    public boolean empty() { return masterList.isEmpty();}


}
