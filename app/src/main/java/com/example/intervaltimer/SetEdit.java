package com.example.intervaltimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woxthebox.draglistview.DragListView;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SetEdit extends AppCompatActivity {

    TextView setName, repTime, totTime, iterations;
    DragListView dragListView;
    Workout workout;
    FloatingActionButton addTimer, done;

    ArrayList<Workout> workoutList; // Save/Load location (Probably better way to do this

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_edit);
        Toolbar toolbar = findViewById(R.id.setToolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true); // Enable the Up button

        ////////// Init all vars w/views ///////////

        setName = findViewById(R.id.editSetName);
        repTime = findViewById(R.id.editSetRepTime);
        totTime = findViewById(R.id.editSetTotTime);
        iterations = findViewById(R.id.editSetIterations);



    }

    // Function to save to shared Prefs
    private void saveWorkout(){
        if (!workoutList.contains(workout)) {
            workoutList.add(workout); // add newly created workout to save array
        }
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


}
