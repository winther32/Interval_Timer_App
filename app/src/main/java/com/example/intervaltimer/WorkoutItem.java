package com.example.intervaltimer;

import android.os.Debug;
import android.util.Log;

public class WorkoutItem {
    Timer timer = null;
    Set set = null;

    public WorkoutItem(Timer T) {
        timer = T;
    }

    public WorkoutItem(Set S) {
        set = S;
    }


    // Methods to change type
    public void setItem(Timer T) {
        timer = T;
        set = null;
    }

    public void setItem(Set S) {
        timer = null;
        set = S;
    }

    // method to retrieve minutes from the held non-null item
    public int getMin(){
        if (timer == null) {
            return set.Minutes;
        } else if (set == null) {
            return timer.Minutes;
        } else {
            return 0;
        }
    }

    // method to retrieve seconds from the held non-null item
    public int getSec() {
        if (timer == null) {
            return set.Seconds;
        } else if (set == null) {
            return timer.Seconds;
        } else {
            return 0;
        }
    }

    public int getType() {
        if (timer == null && set != null) {
            return TimeUnit.TYPE_SET;
        } else if (timer != null && set == null) {
            return  TimeUnit.TYPE_TIMER;
        } else {
//            Log.e() // throw an error message into log
            return -1;
        }
    }
}
