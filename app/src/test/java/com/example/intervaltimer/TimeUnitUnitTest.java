package com.example.intervaltimer;


import org.junit.Test;

import java.security.spec.ECField;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Unit tests for the getter/setter methods of TimeUnit abstract class, using timer instances.
public class TimeUnitUnitTest {

    // Name Tests
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


    // Minutes Testing
    @Test
    public void getMinutes_5() {
        Timer timer = new Timer("Test", 5,0);
         assertEquals(5, timer.getMinutes());
    }

    @Test
    public void setMinutes_negativeIn_1() {
        Timer timer = new Timer("Test", 1,0);
        try {
            timer.setMinutes(-2);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
        assertEquals(1, timer.Minutes);
    }

    @Test
    public void setMinutes_5() {
        Timer timer = new Timer("Test", 1,0);
        timer.setMinutes(5);
        assertEquals(5, timer.Minutes);
    }


    // Seconds testing
    @Test
    public void getSeconds_5() {
        Timer timer = new Timer("Test", 1,5);
        assertEquals(5, timer.getSeconds());
    }

    @Test
    public void setSeconds_negativeIn_2() {
        Timer timer = new Timer("Test", 1,2);
        try {
            timer.setSeconds(-5);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
        assertEquals(2, timer.Seconds);
    }

    @Test
    public void setSeconds_5() {
        Timer timer = new Timer("Test", 1,5);
        assertEquals(5, timer.Seconds);
    }

    @Test
    public void setSeconds_over_59_0() {
        Timer timer = new Timer("Test", 1,0);
        try {
            timer.setSeconds(61);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
        assertEquals(0, timer.Seconds);
    }


    // ID test
    @Test
    public void getID_UUID() {
        Timer timer = new Timer("Test", 1,0);
        assertNotNull(timer.getID());
    }


    // Iterations Testing
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
