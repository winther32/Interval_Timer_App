package com.example.intervaltimer;

//This class is the default/example workout that is automatically loaded on app's first load.

public class DebugTestWorkout {
    // Instantiate new timers
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

    // Instantiate new set
    Set s1 = new Set();
    Set s2 = new Set("Long Set");

    // Create workoutItem objs. w/timers
    WorkoutItem w1 = new WorkoutItem(t1);
    WorkoutItem w2 = new WorkoutItem(t2);
    WorkoutItem w3 = new WorkoutItem(t3);
    WorkoutItem w4 = new WorkoutItem(t4);
    WorkoutItem w5 = new WorkoutItem(t5);
    WorkoutItem w6 = new WorkoutItem(t6);
    WorkoutItem w7 = new WorkoutItem(t7);
    WorkoutItem w8 = new WorkoutItem(t8);
    WorkoutItem w9 = new WorkoutItem(t9);
    WorkoutItem w10 = new WorkoutItem(t10);

    Workout testWorkout;

    // Create workout obj.
    public DebugTestWorkout(){
        testWorkout = new Workout();
        testWorkout.setWorkoutName("Example Workout");

        // Add timers to set
        s1.add(w1);
        s1.add(w2);
        s1.add(w3);
        WorkoutItem w11 = new WorkoutItem(s1);
        // Add set
        testWorkout.add(w11);

        // Add timers in the middle
        testWorkout.add(w4);
        testWorkout.add(w5);
        testWorkout.add(w6);
        testWorkout.add(w7);
        testWorkout.add(w8);

        // Second set
        s2.add(w9);
        s2.add(w10);
        WorkoutItem w12 = new WorkoutItem(s2);
        testWorkout.add(w12);


    }
}
