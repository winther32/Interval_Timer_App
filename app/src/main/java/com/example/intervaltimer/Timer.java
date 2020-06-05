package com.example.intervaltimer;

// Basic Timer class
public class Timer {
    // Has a  duration, name as innards
    // Needs display as well ?
    int Minutes, Seconds;
    String Name;

    // Timer obj constructor
    public Timer(String name, int minutes, int seconds) {
        Name = name;
        Minutes = minutes;
        Seconds = seconds;
    }
}
