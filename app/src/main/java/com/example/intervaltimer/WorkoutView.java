package com.example.intervaltimer;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener {

    TextView currentTimerDisplay, currentNameDisplay;
    Workout workout;
    ToggleButton startStop;
    Button reset, save;

    // Vars for basic timer
    //Declare Vars used
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds ;
    MediaPlayer shortBeep, twoBeeps;

    ArrayList<Workout> workoutList; // Location to save to.

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

        ////////////////////// Edit View functionality ///////////////////////////////////
        // To Contain: recycler view of all timers in workout, save button and add
        // timer and later set FAB.

        loadData(); // Load the workout array as storage location.
        workout = new Workout(); // In create new workout.. This is new obj.
        final FloatingActionButton newTimer = findViewById(R.id.newTimer);
        reset = findViewById(R.id.resetButton);
        save = findViewById(R.id.saveButton);

        // Launches timer info dialog
        newTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                openTimerCreation();
            }
        });

        // Save button click listener
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
            }
        });

        //////////////////////// Pertaining to Running Timer Functionality ////////////////////////
        // To Contain: edit button, progress bar, current timer display, recycler view of upcoming
        // timers, start/stop toggle, workout reset button, sounds for intervals and completion,
        // handler for runnable, timer runnable.

        // Create the buttons and timer in code land to match layout land
        handler = new Handler();
        shortBeep = MediaPlayer.create(this, R.raw.short_beep01);
        twoBeeps = MediaPlayer.create(this, R.raw.beeps2);
        currentTimerDisplay = findViewById(R.id.topTimerDisplay);
        currentNameDisplay = findViewById(R.id.currentName);
        startStop = findViewById(R.id.startStopButton);

        // If nothing in the workout. You can't start it. (Prevent Null calls)
        if (workout.size() == 0) {
            startStop.setEnabled(false);
            reset.setEnabled(false);
        }

        // Toggle button for start stop of workout
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Timer should be running (ie start pressed)
                    StartTime = SystemClock.elapsedRealtime();
                    handler.postDelayed(runnable, 0);
                    newTimer.setEnabled(false);
                    reset.setEnabled(false);
                } else {
                    // Timer should be stopped (Stop pressed)
                    TimeBuff -= MillisecondTime;
                    handler.removeCallbacks(runnable);
                    newTimer.setEnabled(true);
                    reset.setEnabled(true);
                }
            }
        });

        // Restart workout mid workout on pause
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset workout to the start
                workout.restart(); // Reset position in workout to start
                Timer firstTimer = workout.currentTimer();
                // Change Display
                currentTimerDisplay.setText("" + String.format("%02d", firstTimer.Minutes) +
                        ":" + String.format("%02d", firstTimer.Seconds) + ".000");
                currentNameDisplay.setText(firstTimer.Name);
                // Reset timer buff to countdown time
                MillisecondTime = 0L;
                TimeBuff = (firstTimer.Minutes * 60 + firstTimer.Seconds) * 1000;
                startStop.setChecked(false); // Reset start/stop toggle to "START"
                reset.setEnabled(false); // Reset disabled since already at start
                handler.removeCallbacks(runnable); // Remove runnable from Q
            }
        });
    }

    // Countdown Runnable
    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.elapsedRealtime() - StartTime;
            UpdateTime = TimeBuff - MillisecondTime;

            // Check if timer is at 0 or close enough :/ (hundreth sec accuracy I think)
            if (UpdateTime <= 0) {
                // If here, current timer has ended
                Timer timerOrNull = workout.move_and_get(); // Check for another timer
                if (timerOrNull == null) {
                    // Have reached end of workout
                    twoBeeps.start(); // Completion sound
                    // Reset workout to the start
                    workout.restart(); // Reset position in workout to start
                    Timer firstTimer = workout.currentTimer();
                    // Change Display
                    currentTimerDisplay.setText("" + String.format("%02d", firstTimer.Minutes) +
                            ":" + String.format("%02d", firstTimer.Seconds) + ".000");
                    currentNameDisplay.setText(firstTimer.Name);
                    // Reset timer buff to countdown time
                    MillisecondTime = 0L;
                    TimeBuff = (firstTimer.Minutes * 60 + firstTimer.Seconds) * 1000;
                    startStop.setChecked(false); // Reset start/stop toggle to "START"
                    reset.setEnabled(false); // Reset disabled since already at start
                    handler.removeCallbacks(runnable); // Remove runnable from Q
                } else { // Found another timer to run. Set and continue
                    shortBeep.start();
                    TimeBuff = (timerOrNull.Minutes * 60 + timerOrNull.Seconds) * 1000;
                    StartTime = SystemClock.elapsedRealtime();
                    currentTimerDisplay.setText("" + String.format("%02d", timerOrNull.Minutes) +
                            ":" + String.format("%02d", timerOrNull.Seconds) + ".000");
                    currentNameDisplay.setText(timerOrNull.Name);
                    handler.postDelayed(this, 0);
                }
            } else { // Continue countdown timer not done
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

    // Function to save to shared Prefs
    private void saveWorkout(){
        workoutList.add(workout); // add newly created workout to save array
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutList);
        editor.putString("Workout list", json);
        editor.apply();
    }

    // Load the saved workout list from Shared Prefs.
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Workout list", null);
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();
        workoutList = gson.fromJson(json, type);
        // If no previous workout List array, create a new one
        if (workoutList == null) {
            workoutList = new ArrayList<>();
        }
    }

    // Launches the new dialog to prompt for new timer info. Part of edit screen
    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    // Used by the NewTimerDialog to create timer and put into this context. Part of edit screen
    public void addTimer(Timer timer) {
        // Add timer to workout list
        workout.add(timer);
        // Reset to start of workout.
        workout.restart();
        Timer timerNow = workout.currentTimer();
        currentTimerDisplay.setText("" + String.format("%02d", timerNow.Minutes) + ":" +
                String.format("%02d",timerNow.Seconds) + ".000");
        currentNameDisplay.setText(timerNow.Name);
        // Set buffer to count down from
        TimeBuff = (timerNow.Minutes * 60 + timerNow.Seconds) * 1000;
        // Since timer added, start stop button enabled.
        startStop.setEnabled(true);
    }
}