package com.example.intervaltimer;

import android.util.Log;

import java.util.ArrayList;

// Iterable groupings of timer objects
public class Set extends TimeUnit {
    // Also has name, UUID, iterations, min and sec
    // List of timers in this set (needs to be accessed by the drag adapter to manipulate)
    ArrayList<WorkoutItem> setList = new ArrayList<>();


    // Constructors
    public Set() {
        Name = "Set";
    }

    public Set(String name) {
        Name = name;
    }

    // Getter for setList
    public ArrayList<WorkoutItem> getSetList() {
        return setList;
    }

    // Return number of timers in Set
    public int size() { return setList.size(); }

    // Return Timer obj by index
    public Timer get(int index) {
        return setList.get(index).getTimer();
    }

    // Add a timer to the setList (for manual adding through code, i.e. DebugTestWorkout)
    public void add(WorkoutItem timer){
        // Verify got a timer in item
        if (timer.getType() != TYPE_TIMER) {
            Log.e("Set", "Attempted to add non-Timer workoutItem to setList");
            return;
        }
        setList.add(timer);
        timer.getTimer().setParentName(Name); // Set the timer's parent as this Set with its ID.
        // Update the rep Min and sec total.
        int totSec = Seconds + timer.getSeconds();
        Seconds = totSec % 60;
        Minutes += (totSec / 60) + timer.getMinutes();
    }

    // Return if set is empty
    public boolean empty() {
        return setList.isEmpty();
    }

    // Return time of 1 full set rep in Seconds
    public int getRepTime() {
        int totSec = 0;
        for (int i = 0; i < setList.size(); i++) {
            totSec += setList.get(i).getSeconds() + (setList.get(i).getMinutes() * 60);
        }
        return totSec;
    }

    // Return total time of set in seconds
    public int getTotalTime() {
        return getRepTime() * Iterations;
    }

    // Type method for drag adapter. From TimeUnit Class
    @Override
    public int getType() {
        return TYPE_SET;
    }
}
