package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener, WorkoutNameDialog.WorkoutNameDialogListener {

    Button save, done;
    Workout workout;
    ArrayList<Workout> workoutList; // Location to save to.
    RecyclerView editRecycler;
    TextView wrkName, wrkTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Toolbar toolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        ////////////////////// Edit View functionality ///////////////////////////////////
        // To Contain: recycler view of all timers in workout, save button and add
        // timer and later set FAB.

        wrkName = findViewById(R.id.dispWrkName);
        wrkTime = findViewById(R.id.dispWrkTotalTime);

        loadData(); // Load the workout array as storage location.

        // TODO: Change below so can edit existing workouts
        workout = new Workout();// In create new workout.. This is new obj.
        launchNamePrompt();

        final FloatingActionButton newTimer = findViewById(R.id.newTimer);
        save = findViewById(R.id.saveButton);
        done = findViewById(R.id.doneEdit);

        // Setting up Recycler view
        editRecycler = findViewById(R.id.workoutEditRecycler);
        EditAdapter editAdapter = new EditAdapter(this, workout.timerList);

        editRecycler.setAdapter(editAdapter);
        editRecycler.setLayoutManager(new LinearLayoutManager(this));


        // Protect against launching Run activity with no workout
        // Will need further checks with the addition of timer removal functionality
        if (workout.size() > 0) {
            done.setEnabled(true);
        } else {
            done.setEnabled(false);
        }

        // Launches new timer info dialog (get info)
        newTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimerCreation();
            }
        });

        // Save button click listener
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure no empty workouts are saved. Save notification
                if (workout.size() > 0) {
                    saveWorkout();
                    Snackbar.make(v, "Workout Saved", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Snackbar.make(v, "Cannot Save Empty Workout", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    // Function to save to shared Prefs
    private void saveWorkout(){
        workoutList.add(workout); // add newly created workout to save array
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutList);
        editor.putString("Workout list", json);
        editor.apply();

        // For now workout must be saved in order to be run
        done.setEnabled(true);
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
        workout.timerList.add(timer);
        // Add timer to the recycler view
        editRecycler.getAdapter().notifyItemInserted((workout.timerList.size()) - 1);
        // Update total time display
        int totSec = workout.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        wrkTime.setText("Total Time " + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
        // Workout now has at least one timer and thus can be run. Enable running
        //done.setEnabled(true);
    }

    private void launchNamePrompt() {
        WorkoutNameDialog nameDialog = new WorkoutNameDialog();
        nameDialog.show(getSupportFragmentManager(), "Workout Name Prompt");
    }

    // Set the title to the name of the workout (usr input)
    // Also set name of workout
    public void passTitle(String title) {

        // Notification in background OK for now but not the best.
        // Assert Something had been input
        if (title.length() == 0) {
            Snackbar.make(findViewById(android.R.id.content), "Workouts Must Have a Name", Snackbar.LENGTH_SHORT)
                    .show();
            launchNamePrompt();
        } else {
            workout.workoutName = title;
            wrkName.setText(title);
        }
    }

    // Way to get back to home screen from dialog cancel.
    // Possibly not best way since launches new intent. Could use old?
    public void toHome() {
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

    // Called onClick of the Save Button
    public void launchRunWorkout(View view) {
        // TODO: Verify that workout exists in workoutList. (prevent null pointers)
        // TODO: Save after every edit
        Intent intent = new Intent(this, WorkoutRun.class );
        // Pass the index of the workout in workoutList to new activity.
        intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            launchNamePrompt(); // Rename workout
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}