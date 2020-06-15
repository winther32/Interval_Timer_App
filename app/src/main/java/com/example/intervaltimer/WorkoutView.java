package com.example.intervaltimer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener {

    TextView currentTimer, currentName;
    Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        currentTimer = findViewById(R.id.topTimerDisplay);
        currentName = findViewById(R.id.currentName);

        // For New assuming only new workouts.
        // TODO: expand to accept saved workouts
        workout = new Workout();

        FloatingActionButton newTimer = findViewById(R.id.newTimer);
        newTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                openTimerCreation();
            }
        });
    }

    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    @Override
    public void applyTexts(String name, int minutes, int seconds) {
        // First need to make a new timer obj. and add to workout

        //workout.add(newTimer);
//        // Update the Display based on workout list
//
    }

    public void addTimer(Timer timer) {
        // Add timer to workout list
        workout.add(timer);
        // Display current timer in list
        Timer timerNow = workout.currentTimer();
        currentTimer.setText("" + String.format("%02d", timerNow.Minutes) + ":" + String.format("%02d",timerNow.Seconds) + ".000");
        currentName.setText(timerNow.Name);

    }
}