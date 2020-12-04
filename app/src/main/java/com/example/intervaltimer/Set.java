package com.example.intervaltimer;

import java.util.ArrayList;

// Iterable groupings of timer objects
public class Set extends TimeUnit {

    // Also has name, UUID, iterations, min and sec
    ArrayList<WorkoutItem> setList = new ArrayList<>();

    public Set() {
        Name = "Set";
    }

    public Set(String name) {
        Name = name;
    }

    // Return number of timers in Set
    public int size() { return setList.size(); }

    // Return Timer obj by index
    public Timer get(int index) {
        return setList.get(index).getTimer();
    }

    // Add a timer to the setList
    public void add(WorkoutItem timer){
        setList.add(timer);
        timer.getTimer().parentName = Name; // Set the timer's parent as this Set with its ID.
        // Update the rep Min and sec total.
        int totSec = Seconds + timer.getTimer().Seconds;
        Seconds = totSec % 60;
        Minutes += (totSec / 60) + timer.getTimer().Minutes;
    }

    // Return if set is empty
    public boolean empty() {
        return setList.isEmpty();
    }

    // Return time of 1 full set rep in Seconds
    public int getRepTime() {
        int totSec = 0;
        for (int i = 0; i < setList.size(); i++) {
            totSec += setList.get(i).getTimer().Seconds + (setList.get(i).getTimer().Minutes * 60);
        }
        return totSec;
    }

    // Return total time of set in seconds
    public int getTotalTime() {
        int totSec = getRepTime() * Iterations;
        return totSec;
    }

    // Type method for drag adapter
    @Override
    public int getType() {
        return TYPE_SET;
    }
}
