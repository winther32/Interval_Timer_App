package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
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

public class SetEdit extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener,
        editDragAdapter.dragAdapterClickListener, WorkoutNameDialog.WorkoutNameDialogListener,
        DeleteWorkoutDialog.DeleteWorkoutDialogListener {

    ImageButton iterUp, iterDown;
    TextView setName, repTime, totTime, iterations;
    DragListView dragListView;
    Workout workout;
    Set set;
    WorkoutItem workoutItem;
    FloatingActionButton addTimer, done;
    Boolean newSet;

    ArrayList<Workout> workoutList; // Save/Load location (Probably better way to do this

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////////// Init all vars w/views ///////////

        setContentView(R.layout.set_edit);
        Toolbar toolbar = findViewById(R.id.setToolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); // Enable the Up button

        setName = findViewById(R.id.editSetName);
        repTime = findViewById(R.id.editSetRepTime);
        totTime = findViewById(R.id.editSetTotTime);
        iterations = findViewById(R.id.editSetIterations);
        dragListView = findViewById(R.id.setDragList);
        addTimer = findViewById(R.id.setAddFab);
        done = findViewById(R.id.setDone);
        iterUp = findViewById(R.id.setIterUp);
        iterDown = findViewById(R.id.setIterDown);

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
            newSet = true;
            set = new Set();
            workoutItem = new WorkoutItem(set);
            set = workoutItem.getSet();
            launchSetName();
        } else {
            newSet = false;
            workoutItem = workout.get(setIndex);
            set = workout.get(setIndex).getSet();
        }


        /////////// Init Display Times //////////////

        setName.setText(set.Name);

        repTime.setText("" + String.format("%02d", set.Minutes) +
                ":" + String.format("%02d", set.Seconds));
        int totSec = set.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;

        totTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));


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
        editDragAdapter itemAdapter = new editDragAdapter(set.setList, R.id.timer_swipe_card, true, this);
        dragListView.setAdapter(itemAdapter, true);

        /////// Buttons ///////////

        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimerCreation();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newSet) {
                    // If making a new set and set not empty, add set to workout
                    if (!set.empty()) {
                        workout.add(workoutItem);
                    }
                } else {
                    // If editing existing set and set is empty remove from workout
                    if (set.empty()) {
                        workout.remove(workoutItem);
                    }
                }
                saveWorkout();
                backToEditWorkout();
            }
        });


        // Touch listeners for the up and down arrows on the set iterations
        iterUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Initial iterate up one
                        if (set.Iterations < 99) {
                            set.Iterations++;
                        }
                        iterations.setText(Integer.toString(set.Iterations));
                        // If held for a second trigger fast increase
                        handler.postDelayed(fastUp, 1000);
                        return true;
                    case (MotionEvent.ACTION_UP):
                        // On release stop the fast up runnable;
                        handler.removeCallbacks(fastUp);
                        return true;
                }
                return false;
            }
        });

        iterDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Initial decrement on first click
                        if (set.Iterations > 1) {
                            set.Iterations--;
                        }
                        iterations.setText(Integer.toString(set.Iterations));
                        // If held for one second then fast decrease
                        handler.postDelayed(fastDown, 1000);
                        return true;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(fastDown);
                        return true;
                }
                return false;
            }
        });

    }

    // Runnable for the repeat increase action
    public Runnable fastUp = new Runnable() {
        @Override
        public void run() {
            if (set.Iterations < 99) {
                set.Iterations++;
            }
            iterations.setText(Integer.toString(set.Iterations));
            handler.postDelayed(this, 100);
        }
    };

    // Runnable for the repeat decrease action
    public Runnable fastDown = new Runnable() {
        @Override
        public void run() {
            if (set.Iterations > 1) {
                set.Iterations--;
            }
            iterations.setText(Integer.toString(set.Iterations));
            handler.postDelayed(this, 100);
        }
    };

    // Function to save to shared Prefs
    private void saveWorkout() {
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
    private void loadData() {
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

    // Finish this activity and launch new edit workout activity
    public void backToEditWorkout() {
        Intent intent = new Intent(this, WorkoutView.class );
        intent.putExtra("Workout Index", workoutList.indexOf(workout));
        startActivity(intent);
        finish();
    }


    /////////////////// Menu functions ////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home: // close activity on up button
                if (set.empty()) {
                    workout.remove(workoutItem);
                }
                backToEditWorkout();
                return true;
            case R.id.editSetRename:
                launchSetName();
                return true;
            case R.id.editSetDelete:
                // Create a deletion verification dialog
                DeleteWorkoutDialog deleteWorkoutDialog = new DeleteWorkoutDialog();
                deleteWorkoutDialog.setMode(workout.masterList.indexOf(workoutItem));
                deleteWorkoutDialog.show(getSupportFragmentManager(), "Set Menu Delete Verify");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //////////////// NewTimerDialog Interface funcs ////////////////////

    // Launches the new dialog to prompt for new timer info. Part of edit screen
    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    @Override
    public void addTimer(WorkoutItem item) {
        dragListView.getAdapter().addItem(set.setList.size(), item);

        // Assign the set as the parent of the Timer being added to the set
        item.getTimer().parentName = set.Name;

        // Update set values (rep time)
        int totSec = set.getRepTime();
        set.Seconds = totSec % 60;
        set.Minutes += totSec / 60;

        // Update rep time display
        repTime.setText("" + String.format("%02d", set.Minutes) +
                ":" + String.format("%02d", set.Seconds));

        // Update total time display
        totSec = set.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        totTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
    }

    @Override
    public void editTimer(WorkoutItem item, int pos) {
        // Rn jut add and new then remove the old timer to "edit"
        dragListView.getAdapter().addItem(pos, item);
        dragListView.getAdapter().removeItem(pos+1);

        // Update set values (rep time)
        int totSec = set.getRepTime();
        set.Seconds = totSec % 60;
        set.Minutes += totSec / 60;

        // Update rep time display
        repTime.setText("" + String.format("%02d", set.Minutes) +
                ":" + String.format("%02d", set.Seconds));

        // Update total time display
        totSec = set.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        totTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
    }


    /////////////////////// Edit Adapter interface funcs /////////////////////

    @Override
    public void deleteTimer_dAdapter(int position) {
        // Update set values (rep time)
        dragListView.getAdapter().removeItem(position);
        int totSec = set.getRepTime();
        set.Seconds = totSec % 60;
        set.Minutes -= totSec / 60;

        // Update rep time display
        repTime.setText("" + String.format("%02d", set.Minutes) +
                ":" + String.format("%02d", set.Seconds));

        // Update total time display
        totSec = set.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        totTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
    }

    @Override
    public void editTimer_dAdapter(int position) {
        Timer timer = set.get(position);
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.editInstance(timer.Name, String.valueOf(timer.Minutes) , String.valueOf(timer.Seconds), position);
        timerDialog.show(getSupportFragmentManager(), "Timer edit");
    }

    // Currently not used since no Sets in Sets
    @Override
    public void deleteSet_dAdapter(int position) {

    }
    @Override
    public void editSet_dAdapter(int position) {

    }


    ///////////  Set Rename interface (WorkoutNameDialog) //////////

    public void launchSetName() {
        WorkoutNameDialog nameDialog = new WorkoutNameDialog();
        nameDialog.setNameChange();
        nameDialog.show(getSupportFragmentManager(), "Set Name Prompt");
    }

    @Override
    public void passTitle(String title) {
        if (!title.equals("")) {
            set.Name = title;
            setName.setText(title);
            // Change parent name for all timers in the Set
            // Would be better if all timers could have a single reference to the set as the parent
            for (int i = 0; i < set.setList.size(); i++) {
                set.get(i).parentName = title;
            }
        }
    }


    /////////// Delete Set dialog interface //////////////

    // Should never be called form this activity;
    @Override
    public void deleteWorkout() {
    }

    @Override
    public void delete_dialog_Set(int position) {
        workout.remove(position);
        saveWorkout();
        backToEditWorkout();
    }
}
