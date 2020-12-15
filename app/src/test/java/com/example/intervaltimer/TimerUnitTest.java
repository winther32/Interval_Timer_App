package com.example.intervaltimer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

// Unit tests for Timer class methods
public class TimerUnitTest {

    @Test
    public void invalid_cons_args_Exception() {
        try {
            Timer timer = new Timer("Fail", 0, 0);
        } catch (Exception e) {
            assertEquals("Min and sec value <= 0", e.getMessage());
        }
    }

    @Test
    public void timer_type_is_0() {
        Timer timer = new Timer("Test Timer", 0, 1);
        int type = timer.getType();
        assertEquals(0, type);
    }

    @Test
    public void default_iter_1() {
        Timer timer = new Timer("Test", 0, 10);
        assertEquals(1, timer.getIterations());
    }

    @Test
    public void default_parentName_null() {
        Timer timer = new Timer("Test", 0, 5);
        assertNull(timer.getParentName());
    }

    @Test
    public void setParentName_test() {
        Timer timer = new Timer("Timer", 0, 15);
        timer.setParentName("Test");
        assertEquals("Test", timer.getParentName());
    }

    @Test
    public void parentIter_getSet_2() {
        Timer timer = new Timer("ParIter", 1,1);
        timer.setParentIterations(2);
        assertEquals(2, timer.getParentIterations());
    }

    @Test
    public void parentTimerCount_getSet_2() {
        Timer timer = new Timer("ParTC",1,1);
        timer.setParentTimerCount(2);
        assertEquals(2, timer.getParentTimerCount());
    }
}
