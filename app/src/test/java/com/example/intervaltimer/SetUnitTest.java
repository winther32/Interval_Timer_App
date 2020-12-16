package com.example.intervaltimer;

import org.junit.Test;

import static org.junit.Assert.*;

// Unit tests for Set sub-class of TimeUnit class
public class SetUnitTest {

    // Type test
    @Test
    public void set_type_is_1() {
        Set set = new Set("Test Set");
        int type = set.getType();
        assertEquals(1, type);
    }


    // Empty/Null and size testing
    @Test
    public void new_set_is_empty() {
        Set set = new Set("Test Set");
        boolean bool = set.empty();
        assertTrue(bool);
    }

    @Test
    public void new_set_size_0() {
        Set set = new Set();
        assertEquals(0, set.size());
    }

    @Test
    public void add_timer_to_set_notEmpty() {
        Timer timer = new Timer("First", 0,1);
        WorkoutItem item = new WorkoutItem(timer);
        Set set = new Set();
        set.add(item);
        assertFalse(set.empty());
    }

    @Test
    public void add_timer_to_empty_set_size_1() {
        Timer timer = new Timer("First", 0,1);
        WorkoutItem item = new WorkoutItem(timer);
        Set set = new Set();
        set.add(item);
        assertEquals(1, set.size());
    }


    // Rep Timing tests
    @Test
    public void empty_set_rep_time_0() {
        Set set = new Set();
        int result = set.getRepTime();
        assertEquals(result, 0);
    }

    @Test
    public void one_timer_rep_time_100() {
        Timer timer = new Timer("First", 1,40);
        WorkoutItem item = new WorkoutItem(timer);
        Set set = new Set();
        set.add(item);
        assertEquals(100, set.getRepTime());
    }

    @Test
    public void one_timer_3Iter_rep_time_100() {
        Timer timer = new Timer("First", 1,40);
        WorkoutItem item = new WorkoutItem(timer);
        Set set = new Set();
        set.add(item);
        set.setIterations(3);
        assertEquals(100, set.getRepTime());
    }

    @Test
    public void two_timer_repTime_90() {
        Timer t1 = new Timer("1", 0, 45);
        Timer t2 = new Timer("2", 0,45);
        Set set = new Set();
        WorkoutItem w1 = new WorkoutItem(t1);
        WorkoutItem w2 = new WorkoutItem(t2);
        set.add(w1);
        set.add(w2);
        assertEquals(90, set.getRepTime());
    }


    // Tot timing tests
    @Test
    public void empty_set_tot_time_0() {
        Set set = new Set();
        int result = set.getTotalTime();
        assertEquals(result, 0);
    }

    @Test
    public void one_timer_3_iterations_tot_time_300() {
        Timer timer = new Timer("First", 1,40);
        WorkoutItem item = new WorkoutItem(timer);
        Set set = new Set();
        set.add(item);
        set.Iterations = 3;
        assertEquals(300, set.getTotalTime());
    }
}
