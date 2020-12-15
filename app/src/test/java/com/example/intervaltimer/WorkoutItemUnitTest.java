package com.example.intervaltimer;

import org.junit.Test;

import java.sql.Time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Unit tests for WorkoutItem methods and constructors
public class WorkoutItemUnitTest {

    @Test
    public void construct_with_timer_NotNull() {
        Timer timer = new Timer("test",0,10);
        WorkoutItem item = new WorkoutItem(timer);
        assertNotNull(item);
    }

    @Test
    public void construct_with_set_NotNull() {
        Set set = new Set();
        WorkoutItem item = new WorkoutItem(set);
        assertNotNull(item);
    }

    @Test
    public void getActive_timer() {
        Timer timer = new Timer("test",0,10);
        WorkoutItem item = new WorkoutItem(timer);
        assertTrue(item.getActive() instanceof Timer);
    }

    @Test
    public void getActive_set() {
        Set set = new Set();
        WorkoutItem item = new WorkoutItem(set);
        assertTrue(item.getActive() instanceof Set);
    }

    @Test
    public void getMinutes_timer_5() {
        Timer timer = new Timer("test",5,10);
        WorkoutItem item = new WorkoutItem(timer);
        assertEquals(5, timer.getMinutes());
    }

    @Test
    public void getMinutes_set_5() {
        Set set = new Set();

    }
}
