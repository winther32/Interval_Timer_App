package com.example.intervaltimer;

public class DebugTestWorkout {
    // Basic Test Workout obj.
    WorkoutItem t1 = new WorkoutItem(new Timer("1", 0, 1));
    WorkoutItem t2 = new WorkoutItem(new Timer("2", 0, 2));
    WorkoutItem t3 = new WorkoutItem(new Timer("3", 0, 3));
    WorkoutItem t4 = new WorkoutItem(new Timer("4", 0, 4));
    WorkoutItem t5 = new WorkoutItem(new Timer("5", 0, 5));
    WorkoutItem t6 = new WorkoutItem(new Timer("6", 0, 6));
    WorkoutItem t7 = new WorkoutItem(new Timer("7", 0, 7));
    WorkoutItem t8 = new WorkoutItem(new Timer("8", 0, 8));
    WorkoutItem t9 = new WorkoutItem(new Timer("9", 0, 9));
    WorkoutItem t10 = new WorkoutItem(new Timer("10", 0, 10));
    Workout testWorkout;

    public DebugTestWorkout(){
        testWorkout = new Workout();
        testWorkout.workoutName = "Debug Workout";
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
