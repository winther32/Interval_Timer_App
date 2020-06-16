package com.example.intervaltimer;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener {

    TextView currentTimerDisplay, currentNameDisplay;
    Workout workout;
    ToggleButton startStop;

    // Vars for basic timer
    //Declare Vars used
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds ;
    MediaPlayer shortBeep;


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

        // Timer Vars and code ####################################################
        // Create the buttons and timer in code land to match layout land
        handler = new Handler();
        shortBeep = MediaPlayer.create(this, R.raw.short_beep01);

        currentTimerDisplay = findViewById(R.id.topTimerDisplay);
        currentNameDisplay = findViewById(R.id.currentName);
        // For New assuming only new workouts.
        // TODO: expand to accept saved workouts
        workout = new Workout();

        startStop = findViewById(R.id.startStopButton);

        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Timer should be running (ie start pressed)
                    StartTime = SystemClock.elapsedRealtime();
                    handler.postDelayed(runnable, 0);
                } else {
                    // Timer should be stopped (Stop pressed)
                    TimeBuff -= MillisecondTime;
                    handler.removeCallbacks(runnable);
                }
            }
        });

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

    // Countdown Runnable
    public Runnable runnable = new Runnable() {

        public void run() {
            // TODO: Find place to set initial time buffer
            MillisecondTime = SystemClock.elapsedRealtime() - StartTime;
            UpdateTime = TimeBuff - MillisecondTime;

            // Check if timer is at 0 or close enough :/ (hundreth sec accuracy I think)
            if (UpdateTime <= 0) {
                shortBeep.start();
                currentTimerDisplay.setText("00:00.000");
                handler.removeCallbacks(runnable);
            } else { // Continue countdown
                Seconds = (int) (UpdateTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (UpdateTime % 1000);
                currentTimerDisplay.setText("" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds) + "."
                        + String.format("%03d", MilliSeconds));
                handler.postDelayed(this, 0);
            }
        }

    };

    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    @Override
    public void applyTexts(String name, int minutes, int seconds) {
        // Relic method that may be used later.
    }

    public void addTimer(Timer timer) {
        // Add timer to workout list
        workout.add(timer);
        // Display current timer in list
        Timer timerNow = workout.currentTimer();
        currentTimerDisplay.setText("" + String.format("%02d", timerNow.Minutes) + ":" + String.format("%02d",timerNow.Seconds) + ".000");
        currentNameDisplay.setText(timerNow.Name);
        TimeBuff = (timerNow.Minutes * 60 + timerNow.Seconds) * 1000;

    }
}