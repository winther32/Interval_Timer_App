package com.example.intervaltimer;

import java.util.UUID;

// The parent class for holding times
// Timers, Sets, and super sets are all Time units
// All time unit objects are unique by random UUID
public class TimeUnit {
    int Iterations = 1;
    String Name;
    // Make sure that each unit is unique. Very small chance of collision
    long ID =  (UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

    // Basic constructor (prob should not be used ever)
//    public TimeUnit() {
//        Name = "Default";
//        Iterations = 1;
//    }
}
