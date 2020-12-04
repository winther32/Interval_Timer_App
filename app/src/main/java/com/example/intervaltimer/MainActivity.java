package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

// This constitutes the landing page activity when app is first opened.
// Displays all workouts currently saved locally in recyclerView. Click to launch them.
public class MainActivity extends AppCompatActivity implements SelectAdapter.OnWorkoutClickListener{

    private RecyclerView workoutRecycler;
    private ArrayList<Workout> workoutList; // Local list of all workouts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Set title of the toolbar
        actionBar.setTitle(getResources().getString(R.string.my_workouts));

        // Load and set up recycler view
        loadData(); // Load any previously saved workouts into workoutList
        workoutRecycler = findViewById(R.id.workoutSelectRecycler);
        SelectAdapter selectAdapter = new SelectAdapter(this, workoutList, this);
        workoutRecycler.setAdapter(selectAdapter);
        workoutRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    // Load the saved workout list from Shared Prefs.
    // Data is stored as a GSON file converted from JSON
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Workout list", null);
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();

        // Set local list from loaded workout list
        workoutList = gson.fromJson(json, type);

        // Protocol for first time instantiation.
        // If nothing has been saved before (first time load) instance
        if (workoutList == null) {
            // Create a new list to store data
            workoutList = new ArrayList<>();

            // Save a the default/example workout to display.
            saveWorkout(new DebugTestWorkout().testWorkout);
        }
    }

    // Function to save to shared Prefs
    // Saves ArrayList as JSON converted to GSON
    private void saveWorkout(Workout workout){
        workoutList.add(workout); // add newly created workout to save array
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutList);
        editor.putString("Workout list", json);
        editor.apply();
    }


    // Called onClick of the newWorkout Button
    // Launches workout edit view of new empty workout
    public void launchNewWorkout(View view) {
        Intent intent = new Intent(this, WorkoutView.class );
        startActivity(intent);
        finish();
    }

    // Call onClick of recycler workout item. Interface of select adapter
    // This should launch into the workout clicked
    @Override
    public void workoutClicked(Workout workout) {
        // What to do when recycler item is clicked ie run selected workout if exists
        // If the workout is empty launch into edit view
        if (!workout.empty()) {
            Intent intent = new Intent(this, WorkoutRun.class);
            // Pass the index of the workout in workoutList to new activity.
            intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, WorkoutView.class);
            // Pass the index of the workout in workoutList to new activity.
            intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
            startActivity(intent);
            finish();
        }
    }

    // Main activity menu methods
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

        // Handle selection of the menu options
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
