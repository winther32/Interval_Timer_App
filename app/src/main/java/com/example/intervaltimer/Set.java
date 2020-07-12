package com.example.intervaltimer;

import java.util.ArrayList;

// Iterable groupings of timer objects
public class Set extends TimeUnit {

    // Also has name UUID, iterations, min and sec
    private ArrayList<Timer> setList = new ArrayList<>();

    //////////////////// Constructors ////////////////////

    // Create a Set from 1 timer
    public Set(Timer t1) {
        setList.add(t1);
    }
//
//    // add a timer onto an existing Set
//    public Set(Set oldSet, Timer t1) {
//        oldSet.setList.add(t1);
//    }

    /////////////////// Methods ///////////////////////

    // Return number of timers in Set
    public int size() { return setList.size(); }

    public Timer get(int index) {
        return setList.get(index);
    }

    // Add a timer to the setList
    public void add(Timer timer){
        setList.add(timer);
        int totSec = Seconds + timer.Seconds;
        Seconds = totSec % 60;
        Minutes += (totSec / 60) + timer.Minutes;
    }

    // Type method for adapter
    @Override
    public int getType() {
        return TYPE_SET;
    }
}
