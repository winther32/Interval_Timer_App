package com.example.intervaltimer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class WorkoutView extends AppCompatActivity implements NewTimerDialog.NewTimerDialogListener {

    TextView tempTimerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        tempTimerName = findViewById(R.id.topTimerName);

        FloatingActionButton newTimer = findViewById(R.id.newTimer);
        newTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                openTimerCreation();
            }
        });
    }

    public void openTimerCreation() {
        NewTimerDialog timerDialog = new NewTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "Timer creation");
    }

    @Override
    public void applyTexts(String name, String minutes, String seconds) {
        tempTimerName.setText(name + " " + minutes + ":" + seconds);
    }
}