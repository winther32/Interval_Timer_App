package com.example.intervaltimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SetEdit extends AppCompatActivity {

    TextView setName, repTime, totTime, iterations;
    DragListView dragListView;
    Workout workout;
    Set set;
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
        dragListView = findViewById(R.id.setDragList);

        loadData(); // Load all workouts into workoutList

        // Get appropriate workout
        int workoutIndex = getIntent().getIntExtra("Workout Index", -1);
        if (workoutIndex == -1) {
            // TODO: add error to log
        } else {
            workout = workoutList.get(workoutIndex);
        }

        // Get the set from workout
        int setIndex = getIntent().getIntExtra("Set Index", -1);
        if (setIndex == -1) {
            set = new Set();
//            WorkoutItem workoutItem = new WorkoutItem(set);
//            workout.add(workoutItem);
//            launchSetName();
        } else {
            set = workout.get(setIndex).getSet();
        }

        ////////// Init Drag List ////////////

        // Needed for swipe capabilities
        dragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListener() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {
            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
            }

            @Override
            public void onItemSwiping(ListSwipeItem item, float swipedDistanceX) {
            }
        });
        dragListView.setLayoutManager(new LinearLayoutManager(this));
        dragListView.setCanDragHorizontally(false);
        editDragAdapter itemAdapter = new editDragAdapter(workout.masterList, R.id.timer_swipe_card, true);
        dragListView.setAdapter(itemAdapter, true);

        ///////// Buttons ///////////

        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
