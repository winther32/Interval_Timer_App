package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.intervaltimer.TimeUnit.TYPE_SET;
import static com.example.intervaltimer.TimeUnit.TYPE_TIMER;

// Activity where timers are run. Contains countdown timer and progress bar runnable.
public class WorkoutRun extends AppCompatActivity {

    private ArrayList<Workout> workoutList; // Where workouts are stored
    private RecyclerView runRecycler;
    private ArrayList<Timer> runTimers = new ArrayList<>(); // Array of only timers. (All timers in workout in order)
    private ArrayList<Timer> nextTimers = new ArrayList<>(); // For recycler display
    private int position = 0; // Position in runTimers (may not be used)
    private int setPosition = 0; // Used to track position in a set

    // Progress Bar vars (essentially just stop watch)
    private ProgressBar progressBar;
    private int totalTime, timeRun;
    private long MilliUp, totStart, newTime, upBuff = 0L;

    // Vars for basic timer
    private ConstraintLayout setBox, repBox;
    private TextView currentTimerDisplay, currentNameDisplay, setName, setCurrIter, setTotIter;
    private Workout workout;
    private ToggleButton startStop;
    private Button reset;
    private Handler handler;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private int Seconds, Minutes, MilliSeconds ;
    private MediaPlayer shortBeep, twoBeeps;
    private Timer timerOrNull;

    // Settings Booleans
    private Boolean tPause, sPause, endOfSet = false, delay = false; // Pause after timer/set automatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_run);
        Toolbar toolbar = findViewById(R.id.RunToolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        final ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Loading in workoutList (probs faster way to init workout)
        loadData();
        // Getting the index of selected workout from the intent from WorkoutView
        int index = getIntent().getIntExtra("Workout Index", -1);
        // Assert that got a proper workout index
        if (index ==  -1){
            // Got no workout index
            Log.e("Debug", "Workout Run Error: Got no workout index");
            // Go back to main menu and create toast message
            Toast.makeText(this, "Unable to retrieve workout.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class );
            startActivity(intent);
            finish();
        }

        // Init setting booleans
        // Set Run preference booleans from prefs set in settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tPause = sharedPreferences.getBoolean("timer pause", false);
        sPause = sharedPreferences.getBoolean("set pause", false);

        // Init workout from list. Index from click
        workout = workoutList.get(index);
        // Set bar title to workout name
        actionBar.setTitle(workout.getWorkoutName());

        // Verify workout is not empty. If empty launch edit activity
        if (workout.empty()) {
            // Launch into edit workout activity.
            Intent intent = new Intent(this, WorkoutView.class);
            // Pass the index of the workout in workoutList to new activity.
            intent.putExtra("Workout Index", index);
            startActivity(intent);
            finish();
        }

        // Init runTimers array. All timers to be run queued here
        // For all items in the workout
        for (int i = 0; i < workout.size(); i++) {
            WorkoutItem item = workout.get(i);
            // Extract timers from set objs.
            if (item.getType() == TYPE_SET) {
                Set set = item.getSet();
                // For iterations of the set
                for (int j = 1; j <= set.Iterations; j++) {
                    // Add all timers in the Set
                    for (int k = 0; k < set.size(); k++) {
                        // Pass parent info to the timers being added
                        set.get(k).setParentIterations(set.getIterations()); ;
                        set.get(k).setParentTimerCount(set.size());
                        runTimers.add(set.get(k));
                    }
                }
            } else {
                if (item.getType() == TYPE_TIMER) {
                    runTimers.add(item.getTimer());
                }
                // For Debug. If grabbing something that is neither a timer or set
                else {
                    Log.e("Debug", "Workout Run Error: During runTimer inti, array got neither set or timer");
                    Timer t1 = new Timer("Item Type Error", 1, 1);
                    runTimers.add(t1);
                }
            }
        }

        // Init runRecycler of next timers
        // get clone of workout runTimers with head cut off
        // (surely a better way to do this exists)
        for (int i = 1; i < runTimers.size(); i++) {
            nextTimers.add(runTimers.get(i));
        }
        runRecycler = findViewById(R.id.runNextTimers);
        EditAdapter runAdapter = new EditAdapter(this, nextTimers);
        runRecycler.setAdapter(runAdapter);
        runRecycler.setLayoutManager(new LinearLayoutManager(this));

        // init Progress bar
        progressBar = findViewById(R.id.runProgressBar);
        totalTime = workout.getTotalTime() * 1000; // total time in milli sec.
        timeRun = 0;

        //////////////////////// Pertaining to Running Timer Functionality ////////////////////////
        // To Contain: edit button, progress bar, current timer display, recycler view of upcoming
        // timers, start/stop toggle, workout reset button, sounds for intervals and completion,
        // handler for runnable, timer runnable.

        // Media (sounds)
        shortBeep = MediaPlayer.create(this, R.raw.short_beep01);
        twoBeeps = MediaPlayer.create(this, R.raw.beeps2);

        // Display vars.
        currentTimerDisplay = findViewById(R.id.topTimerDisplay);
        currentNameDisplay = findViewById(R.id.currentName);
        setBox = findViewById(R.id.runSetNameConstraint);
        setName = findViewById(R.id.runSetName);
        setCurrIter = findViewById(R.id.runSetCurrentIter);
        setTotIter = findViewById(R.id.runSetTotalIter);
        repBox = findViewById(R.id.runRepConstraint);
        // Buttons
        startStop = findViewById(R.id.startStopButton);
        reset = findViewById(R.id.resetButton);

        handler = new Handler();

        // Init displays from loaded workout (Current timer, recycler next timers)
        Timer firstTimer = runTimers.get(0);
        currentNameDisplay.setText(firstTimer.getName());
        currentNameDisplay.setMovementMethod(new ScrollingMovementMethod());
        currentTimerDisplay.setText("" + String.format("%02d", firstTimer.getMinutes()) + ":" +
                String.format("%02d", firstTimer.getSeconds()));

        // Set the the init Set info display
        updateSetName(firstTimer);
        setCurrIter.setText("1"); // Will always start on first iter;
        setTotIter.setText(Integer.toString(firstTimer.getParentIterations()));
        // Init of set position variable if the first timer is in a set
        if (firstTimer.getParentName() != null) {
            setPosition = 1;
        }

        // TODO: implement rep iterations?
        repBox.setVisibility(View.GONE);

        // Init timeBuff for countdown
        TimeBuff = (firstTimer.getMinutes() * 60 + firstTimer.getSeconds()) * 1000;
        // Init button states
        reset.setEnabled(false);

        // Toggle button for start stop of workout
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Timer should be running (ie start pressed)
                    StartTime = SystemClock.elapsedRealtime();
                    totStart = SystemClock.elapsedRealtime();
                    handler.postDelayed(runnable, 0);
                    reset.setEnabled(false);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                } else {
                    // Timer should be stopped (Stop pressed)
                    TimeBuff -= MillisecondTime;
                    upBuff += MilliUp;
                    handler.removeCallbacks(runnable);
                    reset.setEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        });

        // Restart workout mid workout when paused
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset workout to the start
               resetRunDisplay();
            }
        });
    }

    // Countdown Runnable for main timer and progress bar
    public Runnable runnable = new Runnable() {
        public void run() {
            // Update Prog Bar
            MilliUp = SystemClock.elapsedRealtime() - totStart;
            newTime = upBuff + MilliUp;
            timeRun = (int) (newTime); // / 1000);
            progressBar.setProgress((timeRun*100)/totalTime);

            // Update Timer vars
            MillisecondTime = SystemClock.elapsedRealtime() - StartTime; // Time passed since start
            UpdateTime = TimeBuff - MillisecondTime;

            // Check if timer is at 0 or close enough :/ (hundredth sec accuracy I think)
            if (UpdateTime <= 0) {
                // If here, current timer has ended

                // Move to next position in runTimers
                position++;
                // Assigning the next timer or null if out of range;
                if (position < runTimers.size()) {
                    timerOrNull = runTimers.get(position); // Check for another timer
                } else {
                    timerOrNull = null;
                }

                // Have reached end of workout
                if (timerOrNull == null) {
                    twoBeeps.start(); // Completion sound
                    resetRunDisplay();
                } else { // Found another timer to run. Set and continue/pause
                    // Delay end of set bool by 1 timer (ex. must complete last timer 5/5)
                    if (delay) {
                        delay = false;
                        endOfSet = true;
                    }

                    // Update Parent Set rep info and iteration
                    if (timerOrNull.getParentName() != null) { // Next timer is in a set
                        setPosition++;
                        int currIter = (setPosition / timerOrNull.getParentTimerCount());
                        if (currIter == 0) { currIter = 1; } // Can never be on 0th rep
                        setCurrIter.setText(Integer.toString(currIter));
                        // Check if set is completed. If so reset setPos.
                        if (setPosition >= (timerOrNull.getParentTimerCount() * timerOrNull.getParentIterations())) {
                            setPosition = 0;
                            delay = true;
                        }
                    }

                    // Check if settings dictate a pause
                    if (tPause) { // Pause after every Timer
                        startStop.setChecked(false);
                    } else if (sPause && endOfSet) { // Pause on set completion
                        startStop.setChecked(false);
                        endOfSet = false;
                    } else { // No Pauses active, continue with next timer
                        // Pass new updated runnable (pause briefly before start user can see init numb)
                        handler.postDelayed(this, 50);
                    }

                    // Updating Display for new Timer
                    shortBeep.start(); // End of timer sound
                    // Update vars for new timer
                    TimeBuff = (timerOrNull.getMinutes() * 60 + timerOrNull.getSeconds()) * 1000;
                    StartTime = SystemClock.elapsedRealtime();
                    // Set new display
                    currentTimerDisplay.setText("" + String.format("%02d", timerOrNull.getMinutes()) +
                            ":" + String.format("%02d", timerOrNull.getSeconds()));
                    currentNameDisplay.setText(timerOrNull.getName());
                    // Update next timer view
                    nextTimers.remove(0);
                    runRecycler.getAdapter().notifyItemRemoved(0);
                    // Set set info display
                    updateSetName(timerOrNull); // Name and box visibility
                    setTotIter.setText(Integer.toString(timerOrNull.getParentIterations()));
                }
            } else { // Continue countdown, timer not done
                Seconds = (int) (UpdateTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (UpdateTime % 1000) / 10;
                currentTimerDisplay.setText("" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds));
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

    // Reset workout displays
    public void resetRunDisplay() {
        //////// Reset workout to the start ///////
        position = 0; // Reset position in workout to start
        Timer firstTimer = runTimers.get(position);

        // Change Main Display
        currentTimerDisplay.setText("" + String.format("%02d", firstTimer.getMinutes()) +
                ":" + String.format("%02d", firstTimer.getSeconds()));
        currentNameDisplay.setText(firstTimer.getName());

        // Reset set information
        updateSetName(firstTimer); // Set name box update
        setCurrIter.setText("1");
        if (firstTimer.getParentName() != null) {
            setPosition = 1;
            setTotIter.setText(Integer.toString(firstTimer.getParentIterations()));
        } else {
            setPosition = 0; // Reset Set position
        }

        // Reset timer buff to countdown time
        MillisecondTime = 0L;
        TimeBuff = (firstTimer.getMinutes() * 60 + firstTimer.getSeconds()) * 1000;
        startStop.setChecked(false); // Reset start/stop toggle to "START"
        reset.setEnabled(false); // Reset disabled since already at start
        handler.removeCallbacks(runnable); // Remove runnable from Q

        // Update next Timer display (slow but functional)
        nextTimers.clear(); // Remove all remaining timers
        for (int i = 1; i < runTimers.size(); i++) {
            nextTimers.add(runTimers.get(i));
        }
        runRecycler.getAdapter().notifyDataSetChanged();

        // Reset Progress Bar and it's timer
        MilliUp = 0L;
        upBuff = 0L;
        progressBar.setProgress(0);
    }

    // Function to update the set name display next to running timer
    public void updateSetName(Timer timer) {
        if (timer.getParentName() == null) {
            setBox.setVisibility(View.GONE);
            setName.setText("");
        } else {
            setBox.setVisibility(View.VISIBLE);
            setName.setText(timer.getParentName());
        }
    }

    // Launch edit view with given workout
    // Called onClick of the edit Button in Run menu
    public void launchEditWorkout(Workout workout) {
        Intent intent = new Intent(this, WorkoutView.class );
        // Pass the index of the workout in workoutList to new activity.
        intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
        startActivity(intent);
        finish(); // End this run activity
    }


    // Menu functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_run, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class );
                startActivity(intent);
                finish();
                return true;
            case R.id.runToEdit:
                launchEditWorkout(workout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}