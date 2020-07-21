package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SelectAdapter.OnWorkoutClickListener{

    RecyclerView workoutRecycler;
    ArrayList<Workout> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);

        // Load and set up recycler view
        loadData(); // Load any previously saved workouts into workoutList
        workoutRecycler = findViewById(R.id.workoutSelectRecycler);
        SelectAdapter selectAdapter = new SelectAdapter(this, workoutList, this);
        workoutRecycler.setAdapter(selectAdapter);
        workoutRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    // Load the saved workout list from Shared Prefs.
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Workout list", null);
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();
        workoutList = gson.fromJson(json, type);
        // If nothing saved create an empty list to use.
        if (workoutList == null) {
            workoutList = new ArrayList<>();

            // Default for Testing
            saveWorkout(new DebugTestWorkout().testWorkout);
        }
    }

    // Here for debug Test addition
    // Function to save to shared Prefs
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
    public void launchNewWorkout(View view) {
        Intent intent = new Intent(this, WorkoutView.class );
        startActivity(intent);
        finish();
    }

    // Call onClick of recycler workout item.
    @Override
    public void workoutClicked(Workout workout) {
        // What to do when recycler item is clicked ie run selected workout
        // TODO: Verify that workout exists in workoutList. (prevent null pointers)
        Intent intent = new Intent(this, WorkoutRun.class );
        // Pass the index of the workout in workoutList to new activity.
        intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
