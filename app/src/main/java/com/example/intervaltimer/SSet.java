package com.example.intervaltimer;

import java.util.ArrayList;

// Currently not implemented.
// Class one level higher that set class. Is an iterable ArrayList that contains both sets and timers
public class SSet extends TimeUnit {
    ArrayList<Set> ssList = new ArrayList<>();

    // Create a Super Set from 1 set
    public SSet(Set s1) {
        ssList.add(s1);
    }

    // ADD a set onto an existing super set
    public SSet(SSet ss1, Set s1) {
        ss1.ssList.add(s1);
    }

    // REMOVE a set from super set
    public SSet removeTimer(SSet ss1, Set s1) {
        ss1.ssList.remove(s1);
        return ss1;
    }

    // CHANGE POS of a set in a super set
    public SSet changeTimer(SSet ss1, Set s1, int newPos) {
        ss1.ssList.remove(s1);
        ss1.ssList.add(newPos, s1);
        return ss1;
    }

    @Override
    public int getType() {
        return TYPE_SUPER_SET;
    }
}
