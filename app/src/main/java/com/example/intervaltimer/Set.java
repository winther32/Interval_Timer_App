package com.example.intervaltimer;

import java.util.ArrayList;

// Iterable groupings of timer objects
public class Set extends TimeUnit {
    ArrayList<Timer> setList = new ArrayList<>();

    // Create a Set from 1 timer
    public Set(Timer t1) {
        setList.add(t1);
    }

    // add a timer onto an existing Set
    public Set(Set oldSet, Timer t1) {
        oldSet.setList.add(t1);
    }

    // Remove a timer from set
    public Set removeTimer(Set s1, Timer t1) {
        s1.setList.remove(t1);
        return s1;
    }

    // Change the position of a timer in a set
    public Set changeTimer(Set s1, Timer t1, int newPos) {
        s1.setList.remove(t1);
        s1.setList.add(newPos, t1);
        return s1;
    }

}
