package com.example.intervaltimer;

public class DebugTestWorkout {
    // Basic Test Workout obj.
    Timer t1 = new Timer("1", 0, 1);
    Timer t2 = new Timer("2", 0, 2);
    Timer t3 = new Timer("3", 0, 3);
    Timer t4 = new Timer("4", 0, 4);
    Timer t5 = new Timer("5", 0, 5);
    Timer t6 = new Timer("6", 0, 6);
    Timer t7 = new Timer("7", 0, 7);
    Timer t8 = new Timer("8", 0, 8);
    Timer t9 = new Timer("9", 0, 9);
    Timer t10 = new Timer("10", 0, 10);
    Workout testWorkout;

    public DebugTestWorkout(){
        testWorkout = new Workout();
        testWorkout.add(t1);
        testWorkout.add(t2);
        testWorkout.add(t3);
        testWorkout.add(t4);
        testWorkout.add(t5);
        testWorkout.add(t6);
        testWorkout.add(t7);
        testWorkout.add(t8);
        testWorkout.add(t9);
        testWorkout.add(t10);
    }
}
