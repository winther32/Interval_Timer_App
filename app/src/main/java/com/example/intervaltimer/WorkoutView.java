package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener,
        WorkoutNameDialog.WorkoutNameDialogListener, DeleteWorkoutDialog.DeleteWorkoutDialogListener,
        editDragAdapter.dragAdapterClickListener {

    Workout workout;
    ArrayList<Workout> workoutList; // Location to save to.
    TextView wrkName, wrkTime;

    DragListView dragListView;

    FloatingActionButton addFab, newTimer, newSet, doneSave;
    Animation rotateForward, rotateBackwards;
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Toolbar toolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); // Enable the Up button


        ////////////////////// Edit View functionality ///////////////////////////////////
        // To Contain: recycler view of all timers in workout, save button and add
        // timer and later set FAB.

        ////////////////////// Init workout and header labels ///////////////////////////
        wrkName = findViewById(R.id.dispWrkName);
        wrkTime = findViewById(R.id.dispWrkTotalTime);

        loadData(); // Load the workout array as storage location.

        // If new Workout there will be no extra data so index will default to -1
        // If coming from an existing workout there will be an index attached
        int index = getIntent().getIntExtra("Workout Index", -1);
        if (index == -1) {
            // Init new workout
            workout = new Workout();// In create new workout.. This is new obj.
            launchNamePrompt();
        } else {
            // Init workout from list. Index from click
            workout = workoutList.get(index);
            wrkName.setText(workout.workoutName);
            int totSec = workout.getTotalTime();
            int Min = totSec / 60;
            totSec = totSec % 60;
            wrkTime.setText("" + String.format("%02d", Min) +
                    ":" + String.format("%02d", totSec));
        }


        ///////////////////////Set up the drag list ///////////////////////

        dragListView = findViewById(R.id.editDragList);
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
        editDragAdapter itemAdapter = new editDragAdapter(workout.masterList, R.id.set_swipe_card, true, this);
        dragListView.setAdapter(itemAdapter, true);


        ////////////////////////// Init buttons and onClicks  //////////////////////////////

        newTimer = findViewById(R.id.newTimer);
        addFab = findViewById(R.id.addFab);
        newSet = findViewById(R.id.newSet);
        doneSave = findViewById(R.id.editFinishFab);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackwards = AnimationUtils.loadAnimation(this, R.anim.rotate_backwards);

        // Protect against launching Run activity with no workout
        // Will need further checks with the addition of timer removal functionality
        if (!workout.empty()) {
            doneSave.setVisibility(View.VISIBLE);
        } else {
            doneSave.setVisibility(View.GONE);
        }

        editAddAnimation.init(findViewById(R.id.newTimer));
        editAddAnimation.init(findViewById(R.id.newSet));

        // Expandable fab for new timer and new set
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    animateFab();
                    editAddAnimation.showOut(findViewById(R.id.newTimer));
                    editAddAnimation.showOut(findViewById(R.id.newSet));
                    isOpen = false;
                } else {
                    animateFab();
                    editAddAnimation.showIn(findViewById(R.id.newTimer));
                    editAddAnimation.showIn(findViewById(R.id.newSet));
                    isOpen = true;
                }
            }
        });

        // Launches new set activity
        newSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSetActivity();
            }
        });

        // Launches new timer info dialog (get info)
        newTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimerCreation();
            }
        });

        doneSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
                if (!workout.empty()) {
                    launchRunWorkout();
                } else {
                    // Toast notification
                    Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT)
                    .show();
                }
            }
        });

    }

    //Fab animation function
    public void animateFab() {
        if (isOpen) {
            addFab.startAnimation(rotateBackwards);
        } else {
            addFab.startAnimation(rotateForward);
        }
    }

    // Called onClick of the doneSave Button
    public void launchRunWorkout() {
        // Double check if workout not empty
        if (!workout.empty()) {
            Intent intent = new Intent(this, WorkoutRun.class);
            // Pass the index of the workout in workoutList to new activity.
            intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
            startActivity(intent);
            finish();
        }
    }

    // Create a new set item and add it to the workout
    public void launchSetActivity() {
        saveWorkout();
        Intent intent = new Intent(this, SetEdit.class );
        // Pass the index of the workout in workoutList to new activity.
        intent.putExtra("Workout Index", workoutList.indexOf(workout));
        startActivity(intent);
        finish();
    }


    ////////////////// I/O /////////////////////////

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

    //////////////// NewTimerDialog Interface funcs //////////////////////

    // Launches the new dialog to prompt for new timer info. Part of edit screen
    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    // Used by the NewTimerDialog to create timer and put into this context. Interface func.
    public void addTimer(WorkoutItem timer) {
        // TODO: Find out how to appease studio so not warning
        dragListView.getAdapter().addItem(workout.masterList.size(), timer);

        // Update total time display
        int totSec = workout.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        wrkTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));

        // Make complete button visible
        doneSave.setVisibility(View.VISIBLE);

        saveWorkout();
    }

    // Used by NewTimerDialog to edit timer. Interface func.
    public void editTimer(WorkoutItem timer, int pos) {

        // Rn jut add and new then remove the old timer to "edit"
        dragListView.getAdapter().addItem(pos, timer);
        dragListView.getAdapter().removeItem(pos+1);

        // Update total time display
        int totSec = workout.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        wrkTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));

        saveWorkout();
    }


    //////////////////// WorkoutNameDialog Interface funcs //////////////////////////

    // Launch function for Workout names dialog
    private void launchNamePrompt() {
        WorkoutNameDialog nameDialog = new WorkoutNameDialog();
        nameDialog.show(getSupportFragmentManager(), "Workout Name Prompt");
    }

    // Set the title to the name of the workout (usr input) interface func.
    // Also set name of workout
    public void passTitle(String title) {
        // Current idea allows default workout name and ability to rename after
        // Notification in background OK for now but not the best. (commented out)
        // Assert Something had been input
        if (title.length() == 0) {
//            Snackbar.make(findViewById(android.R.id.content), "Workouts Must Have a Name", Snackbar.LENGTH_SHORT)
//                    .show();
//            launchNamePrompt();
        } else {
            // Workout name length cap
            if (title.length() > 64) {
                title = title.substring(0,64);
            }
            workout.workoutName = title;
            wrkName.setText(title);
        }

        saveWorkout();
    }


    /////////////// DeleteWorkoutDialog Interface funcs ////////////////////

    // Launch function for verify delete workout prompt. Used in edit Menu
    public void launchDeleteWorkout() {
        DeleteWorkoutDialog deleteDialog = new DeleteWorkoutDialog();
        deleteDialog.show(getSupportFragmentManager(), "Workout Delete Verify");
    }

    // Func used by delete workout dialog to remove workout. Interface func.
    public void deleteWorkout() {
        // remove from workoutList array
        workoutList.remove(workout);
        // Save updated array ( dont use save workout since would add workout back in)
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutList);
        editor.putString("Workout list", json);
        editor.apply();
        // Return to home screen
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
        finish();
    }

    public void delete_dialog_Set(int position) {
        dragListView.getAdapter().removeItem(position);
        if (workout.empty()) {
            doneSave.setVisibility(View.GONE);
        }
    }


    //////////// Edit and delete functions drag adapter interface  /////////////

    @Override
    public void deleteTimer_dAdapter(int position) {
        dragListView.getAdapter().removeItem(position);
        if (workout.empty()){
            doneSave.setVisibility(View.GONE);
        }

        saveWorkout();
    }

    @Override
    public void editTimer_dAdapter(int position) {
        Timer timer = workout.get(position).getTimer();
        // Launch the newTimerDialog in edit mode. Save happens after edit completed in dialog
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.editInstance(timer.Name, String.valueOf(timer.Minutes) , String.valueOf(timer.Seconds), position);
        timerDialog.show(getSupportFragmentManager(), "Timer edit");
    }

    @Override
    public void deleteSet_dAdapter(int position) {
        // Launch delete workout dialog in set mode. Save occurs after confirmation
        DeleteWorkoutDialog deleteDialog = new DeleteWorkoutDialog();
        deleteDialog.setMode(position);
        deleteDialog.show(getSupportFragmentManager(), "Set Delete Verify");
    }

    @Override
    public void editSet_dAdapter(int position) {
        // Save workout before launch new activity
        saveWorkout();
        // Launch new activity for set management
        Intent intent = new Intent(this, SetEdit.class );
        // Pass the index of the workout in workoutList to new activity.
        intent.putExtra("Workout Index", workoutList.indexOf(workout));
        intent.putExtra("Set Index", position);
        startActivity(intent);
        finish();
    }


    /////////////////// Menu functions ////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home: // close activity on up button
                saveWorkout(); // Save workout on the up button
                Intent intent = new Intent(this, MainActivity.class );
                startActivity(intent);
                finish();
                return true;
            case R.id.action_rename:
                launchNamePrompt(); // Rename workout
                return true;
            case R.id.edit_deleteWrk:
                launchDeleteWorkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}