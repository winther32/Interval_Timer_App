package com.example.intervaltimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener,
        WorkoutNameDialog.WorkoutNameDialogListener, DeleteWorkoutDialog.DeleteWorkoutDialogListener {

    Button save, done;
    Workout workout;
    ArrayList<Workout> workoutList; // Location to save to.
    RecyclerView editRecycler;
    TextView wrkName, wrkTime;

    DragListView dragListView;

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


        // Setting up Recycler view
//        editRecycler = findViewById(R.id.workoutEditRecycler);
//        EditAdapter editAdapter = new EditAdapter(this, workout.timerList);
//
//        editRecycler.setAdapter(editAdapter);
//        editRecycler.setLayoutManager(new LinearLayoutManager(this));


        ///////////////////////Set up the drag list ///////////////////////

        dragListView = findViewById(R.id.editDragList);
//        dragListView.setDragListListener(new DragListView.DragListListener() {
////            @Override
////            public void onItemDragStarted(int position) {
////
////            }
////
////            @Override
////            public void onItemDragging(int itemPosition, float x, float y) {
////
////            }
////
////            @Override
////            public void onItemDragEnded(int fromPosition, int toPosition) {
////
////            }
////        });
        // Swipe listener for edit and delete functionality
        dragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {
                super.onItemSwipeStarted(item);

            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                super.onItemSwipeEnded(item, swipedDirection);

                if (swipedDirection == ListSwipeItem.SwipeDirection.RIGHT) {
//                    Timer timer = (Timer) item.getTag();
//                    int pos = dragListView.getAdapter().getPositionForItem(timer); // UNSAFE?
//                    dragListView.getAdapter().removeItem(pos);
                }
            }
        });
        dragListView.setLayoutManager(new LinearLayoutManager(this));
        dragListView.setCanDragHorizontally(false);
        editDragAdapter itemAdapter = new editDragAdapter(workout.masterList, R.id.timer_swipe_card, true);
        dragListView.setAdapter(itemAdapter, true);


        ////////////////////////// Init buttons and onCLicks  //////////////////////////////

        final FloatingActionButton newTimer = findViewById(R.id.newTimer);
        save = findViewById(R.id.saveButton);
        done = findViewById(R.id.doneEdit);

        // Protect against launching Run activity with no workout
        // Will need further checks with the addition of timer removal functionality
        if (!workout.empty()) {
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

        // TODO: rethink possibility of saving empty workouts
        // Save button click listener
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure no empty workouts are saved. Save notification
                if (!workout.empty()) {
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

    // Launches the new dialog to prompt for new timer info. Part of edit screen
    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    // Used by the NewTimerDialog to create timer and put into this context. Interface func.
    public void addTimer(Timer timer) {
//        // Add timer to workout list
//        workout.timerList.add(timer);
//        // Add timer to the recycler view
//        editRecycler.getAdapter().notifyItemInserted((workout.timerList.size()) - 1);

        // Add to drag and drop list
        // TODO: Find out how to appease studio so not warning
        dragListView.getAdapter().addItem(workout.masterList.size(), timer);

        // Update total time display
        int totSec = workout.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        wrkTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
        // Workout now has at least one timer and thus can be run. Enable running
        //done.setEnabled(true);
    }

    // Used by NewTimerDialog to edit timer. Interface func.
    public  void editTimer(Timer timer, int pos) {

        // Rn jut add and new then remove the old timer to "edit"
        dragListView.getAdapter().addItem(pos, timer);
        dragListView.getAdapter().removeItem(pos+1);

        // Update total time display
        int totSec = workout.getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        wrkTime.setText("" + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
    }

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
    }

    // Way to get back to home screen from dialog cancel.
    // Possibly not best way since launches new intent. Could use old?
    public void toHome() {
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

    // Called onClick of the Done Button
    public void launchRunWorkout(View view) {
        // TODO: Verify that workout exists in workoutList. (prevent null pointers)
        // TODO: Save after every edit
        Intent intent = new Intent(this, WorkoutRun.class );
        // Pass the index of the workout in workoutList to new activity.
        intent.putExtra("Workout Index", workoutList.indexOf(workout)); // DANGER workout needs to be saved before launch as of now.
        startActivity(intent);
    }


    // Launch function for verify delete workout prompt. Used in edit Menu
    public void launchDeleteWorkout() {
        DeleteWorkoutDialog deleteDialog = new DeleteWorkoutDialog();
        deleteDialog.show(getSupportFragmentManager(), "Workout Delete Verify");
    }

    // Func used by delete workout dialog to remove workout. Interface func.
    public void deleteWorkout() {
        // remove from workoutList array
        workoutList.remove(workout);
        // Save updated array
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutList);
        editor.putString("Workout list", json);
        editor.apply();
        // Return to home screen
        toHome();
    }


    //////////// Edit and delete functions on click after swipe /////////////

    // Delete the swiped item on click of delete text onClick of swipeDelete
    public void deleteSwipe(View view) {
        TextView itemID = view.findViewById(R.id.swipeDelete);
        int pos = Integer.parseInt(itemID.getHint().toString());
        dragListView.getAdapter().removeItem(pos);
    }

    // Launch edit timer dialog on click of edit swipe
    public void editSwipe(View view) {
        TextView itemID = view.findViewById(R.id.swipeEdit);
        int pos = Integer.parseInt(itemID.getHint().toString());

        // TODO: WARNING ONLY WORKS WHILE EDT/DLT FOR SET NOT IMPLEMENTED
        Timer timer = (Timer) workout.get(pos);

        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.editInstance(timer.Name, String.valueOf(timer.Minutes) , String.valueOf(timer.Seconds), pos);
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rename) {
            launchNamePrompt(); // Rename workout
            return true;
        }
        if (id == R.id.edit_deleteWrk) {
            launchDeleteWorkout();
        }

        return super.onOptionsItemSelected(item);
    }
}