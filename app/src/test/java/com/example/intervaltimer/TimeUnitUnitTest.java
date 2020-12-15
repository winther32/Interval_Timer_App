package com.example.intervaltimer;


import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Unit tests for the getter/setter methods of TimeUnit abstract class, using timer instances.
public class TimeUnitUnitTest {

    @Test
    public void getName_Test() {
        Timer timer = new Timer("Test", 1,0);
        assertEquals("Test", timer.getName());
    }

    @Test
    public void setName_Done() {
        Timer timer = new Timer("", 1,0);
        timer.setName("Test");
        assertEquals("Test", timer.Name);
    }

    @Test
    public void getMinutes_5() {
        Timer timer = new Timer("Test", 5,0);
         assertEquals(5, timer.getMinutes());
    }

    @Test
    public void setMinutes_negativeIn_0() {
        Timer timer = new Timer("Test", 0,2);
        timer.setMinutes(-2);
        assertEquals(0, timer.Minutes);
    }

    @Test
    public void setMinutes_5() {
        Timer timer = new Timer("Test", 1,0);
        timer.setMinutes(5);
        assertEquals(5, timer.Minutes);
    }

    @Test
    public void getSeconds_5() {
        Timer timer = new Timer("Test", 1,5);
        assertEquals(5, timer.getSeconds());
    }

    @Test
    public void setSeconds_negativeIn_2() {
        Timer timer = new Timer("Test", 1,2);
        timer.setSeconds(-5);
        assertEquals(2, timer.Seconds);
    }

    @Test
    public void setSeconds_5() {
        Timer timer = new Timer("Test", 1,5);
        assertEquals(5, timer.Seconds);
    }

    @Test
    public void setSeconds_over_59_2() {
        Timer timer = new Timer("Test", 1,0);
        timer.setSeconds(61);
        assertEquals(0, timer.Seconds);
    }

    @Test
    public void getID_UUID() {
        Timer timer = new Timer("Test", 1,0);
        assertNotNull(timer.getID());
    }

    @Test
    public void getIterations_1() {
        Timer timer = new Timer("Iter", 1, 1);
        assertEquals(1, timer.getIterations());
    }

    @Test
    public void setIterations_2() {
        Timer timer = new Timer("Iter", 1, 1);
        timer.setIterations(2);
        assertEquals(2, timer.Iterations);
    }
}
