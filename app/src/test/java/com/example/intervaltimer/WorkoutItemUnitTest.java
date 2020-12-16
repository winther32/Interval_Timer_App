package com.example.intervaltimer;

import org.junit.Test;

import java.sql.Time;

import static com.example.intervaltimer.TimeUnit.TYPE_SET;
import static com.example.intervaltimer.TimeUnit.TYPE_TIMER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Unit tests for WorkoutItem methods and constructors
public class WorkoutItemUnitTest {

    // Constructor Testing
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


    // Type Testing
    @Test
    public void getType_Timer() {
        Timer timer = new Timer("First", 5,40);
        WorkoutItem w1 = new WorkoutItem(timer);
        assertEquals(TYPE_TIMER, w1.getType());
    }

    @Test
    public void getType_Set() {
        Set set = new Set();
        WorkoutItem item = new WorkoutItem(set);
        assertEquals(TYPE_SET, item.getType());
    }


    // GetActive Testing
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


    // Get Minutes/Seconds
    @Test
    public void getMinutes_timer_5() {
        Timer timer = new Timer("test",5,10);
        WorkoutItem item = new WorkoutItem(timer);
        assertEquals(5, timer.getMinutes());
    }

    @Test
    public void getMinutes_set_5() {
        Timer timer = new Timer("First", 5,40);
        WorkoutItem w1 = new WorkoutItem(timer);
        Set set = new Set();
        set.add(w1);
        WorkoutItem w2 = new WorkoutItem(set);
        assertEquals(5, w2.getMinutes());
    }

    @Test
    public void getSeconds_timer_10() {
        Timer timer = new Timer("test",5,10);
        WorkoutItem item = new WorkoutItem(timer);
        assertEquals(10, timer.getSeconds());
    }

    @Test
    public void getSeconds_set_40() {
        Timer timer = new Timer("First", 5,40);
        WorkoutItem w1 = new WorkoutItem(timer);
        Set set = new Set();
        set.add(w1);
        WorkoutItem w2 = new WorkoutItem(set);
        assertEquals(40, w2.getSeconds());
    }


    // Total Time Testing
    @Test
    public void totTime_set_160() {
        Timer timer = new Timer("First", 2,40);
        WorkoutItem w1 = new WorkoutItem(timer);
        Set set = new Set();
        set.add(w1);
        set.setIterations(2);
        WorkoutItem w2 = new WorkoutItem(set);
        assertEquals(320, w2.getTotalTime());
    }

    @Test
    public void totTime_timer_100() {
        Timer timer = new Timer("test", 1, 40);
        WorkoutItem w1 = new WorkoutItem(timer);
        assertEquals(100, w1.getTotalTime());
    }
}
