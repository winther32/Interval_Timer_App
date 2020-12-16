package com.example.intervaltimer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Unit Tests for Workout Objs.
public class WorkoutUnitTest {

    // Workout Name Testing
    @Test
    public void getWorkoutName_default_Workout() {
        Workout workout = new Workout();
        assertEquals("Workout", workout.getWorkoutName());
    }

    @Test
    public void setWorkoutName_Test() {
        Workout workout = new Workout();
        workout.setWorkoutName("Test");
        assertEquals("Test", workout.getWorkoutName());
    }


    // Empty/Null Testing
    @Test
    public void default_list_empty() {
        Workout workout = new Workout();
        assertTrue(workout.empty());
    }

    @Test
    public void default_size_0() {
        Workout workout = new Workout();
        assertEquals(0, workout.size());
    }


    // Add/Remove Testing
    @Test
    public void add_to_empty_1() {
        Workout workout = new Workout();
        Timer t1 = new Timer("1", 0, 50);
        WorkoutItem w1 = new WorkoutItem(t1);
        workout.add(w1);
        assertEquals(1, workout.size());
    }

    @Test
    public void remove_by_item_0() {
        Workout workout = new Workout();
        Timer t1 = new Timer("1", 0, 50);
        WorkoutItem w1 = new WorkoutItem(t1);
        workout.add(w1);
        assertEquals(1, workout.size());
        workout.remove(w1);
        assertEquals(0, workout.size());
    }

    @Test
    public void remove_by_index() {
        Workout workout = new Workout();
        Timer t1 = new Timer("1", 0, 50);
        WorkoutItem w1 = new WorkoutItem(t1);
        workout.add(w1);
        assertEquals(1, workout.size());
        workout.remove(0);
        assertEquals(0, workout.size());
    }


    // Get Testing
    @Test
    public void get_by_index_NotNull() {
        Workout workout = new Workout();
        Timer t1 = new Timer("1", 0, 50);
        WorkoutItem w1 = new WorkoutItem(t1);
        workout.add(w1);
        assertNotNull(workout.get(0));
    }


    // Total Time testing
    @Test
    public void totTime_set_and_timer_300() {
        Workout workout = new Workout();
        Timer t1 = new Timer("1", 0, 50);
        Timer t2 = new Timer("2", 0, 50);
        Timer t3 = new Timer("1", 1, 40);
        Set s1 = new Set();
        WorkoutItem w1 = new WorkoutItem(t1);
        WorkoutItem w2 = new WorkoutItem(t2);
        WorkoutItem w3 = new WorkoutItem(t3);
        WorkoutItem w4 = new WorkoutItem(s1);
        s1.add(w1);
        s1.add(w2);
        s1.setIterations(2);
        workout.add(w4);
        workout.add(w3);
        assertEquals(300, workout.getTotalTime());
    }
}
