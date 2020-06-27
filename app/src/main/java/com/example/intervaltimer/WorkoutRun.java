package com.example.intervaltimer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WorkoutRun extends AppCompatActivity {

    ArrayList<Workout> workoutList;

    // Vars for basic timer
    TextView currentTimerDisplay, currentNameDisplay;
    Workout workout;
    ToggleButton startStop;
    Button reset;
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds ;
    MediaPlayer shortBeep, twoBeeps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_run);
        Toolbar toolbar = findViewById(R.id.RunToolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        loadData(); // Loading in workoutList (probs faster way to init workout)
        // Getting the index of selected workout from the intent from WorkoutView
        int index = getIntent().getIntExtra("Workout Index", -1);

        // Assertion check that got info from last activity
//        if (index ==  -1){
//            // Bad things have happened
//            // Couldn't retrieve the index passed in intent.
//            // Gracefully fail here
//        }

        workout = workoutList.get(index);
        // Reset workout may change later if want to be able to pick up where left off
        workout.restart();

        // Potentially bloat. Shouldn't have empty workouts by now
        // If nothing in the workout. You can't start it. (Prevent Null calls)
//        if (workout.size() == 0) {
//            startStop.setEnabled(false);
//            reset.setEnabled(false);
//        }

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
        reset = findViewById(R.id.resetButton);

        // Init displays from loaded workout (Current timer, recycler next timers)
        currentNameDisplay.setText(workout.currentTimer().Name);
        currentTimerDisplay.setText("" + String.format("%02d", workout.currentTimer().Minutes) + ":" +
                String.format("%02d",workout.currentTimer().Seconds) + ".000");
        // Init timeBuff for countdown
        TimeBuff = (workout.currentTimer().Minutes * 60 + workout.currentTimer().Seconds) * 1000;

        // Toggle button for start stop of workout
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Timer should be running (ie start pressed)
                    StartTime = SystemClock.elapsedRealtime();
                    handler.postDelayed(runnable, 0);
                    reset.setEnabled(false);
                } else {
                    // Timer should be stopped (Stop pressed)
                    TimeBuff -= MillisecondTime;
                    handler.removeCallbacks(runnable);
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
}