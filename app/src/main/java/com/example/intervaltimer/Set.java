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

    public Timer get(int index) {
        return setList.get(index).getTimer();
    }

    // Add a timer to the setList
    public void add(WorkoutItem timer){
        setList.add(timer);
        int totSec = Seconds + timer.getTimer().Seconds;
        Seconds = totSec % 60;
        Minutes += (totSec / 60) + timer.getTimer().Minutes;
    }

    public int getTotalTime() {
        int totSec = Seconds * Iterations;
        totSec += Minutes * 60;
        return totSec;
    }

    // Type method for adapter
    @Override
    public int getType() {
        return TYPE_SET;
    }
}
