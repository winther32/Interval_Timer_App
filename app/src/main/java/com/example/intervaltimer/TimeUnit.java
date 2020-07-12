package com.example.intervaltimer;

import java.util.UUID;

// The parent class for holding times
// Timers, Sets, and super sets are all Time units
// All time unit objects are unique by random UUID
public abstract class TimeUnit {
    int Iterations = 1;
    String Name;
    // Make sure that each unit is unique. Very small chance of collision
    UUID ID =  UUID.randomUUID();
    int Minutes, Seconds;

    public static final int TYPE_TIMER = 0;
    public static final int TYPE_SET = 1;
    public static final int TYPE_SUPER_SET = 2;

    abstract public int getType();
}
