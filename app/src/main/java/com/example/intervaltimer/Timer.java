package com.example.intervaltimer;

// Basic Timer class
public class Timer extends TimeUnit {

    // Name and iterations inherited
    int Minutes, Seconds;

    // Vars for set management
    String parentSet = null; // Name of set if in one else null
    int myIter, TotalIters; // Iteration of set timer is in and total set iterations.

    // Timer obj constructor
    public Timer(String name, int minutes, int seconds) {
        Name = name;
        Minutes = minutes;
        Seconds = seconds;
        // Default iterations = 1
    }

    // Overload constructor when non-default(!=1) iteration given
    public Timer(String name, int minutes, int seconds, int iterations) {
        Name = name;
        Minutes = minutes;
        Seconds = seconds;
        Iterations = iterations;
    }
}
