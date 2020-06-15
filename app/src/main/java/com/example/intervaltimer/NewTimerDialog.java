package com.example.intervaltimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NewTimerDialog extends AppCompatDialogFragment {

    // Variables
    EditText WorkoutName;
    EditText Min, Sec;
    NewTimerDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_timer_creation, null);

        builder.setView(view)
                .setTitle("Timer")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Want nothing to happen so nothing here
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Where pass editText info back to underlying activity (workout view)
                        // Will make new/edit the timer at this point
                        String name = WorkoutName.getText().toString();
                        String minutes = Min.getText().toString();
                        String seconds = Sec.getText().toString();
                        int intMinutes;
                        int intSeconds;
                        // Null pointer checks
                        if (minutes.trim().length() == 0) {
                            intMinutes = 0;
                        } else {intMinutes = Integer.valueOf(minutes);}
                        if (seconds.trim().length() == 0) {
                            intSeconds = 0;
                        } else {intSeconds = Integer.valueOf(seconds);}
                        // Some info must be provided to create a new timer
                        if (name != "" || intMinutes != 0 || intSeconds != 0) {
                            Timer newTimer = new Timer(name, intMinutes, intSeconds);
                            listener.addTimer(newTimer);
                        }
                    }
                });

        WorkoutName = view.findViewById(R.id.inWorkoutName);
        Min = view.findViewById(R.id.inMin);
        Sec = view.findViewById(R.id.inSec);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NewTimerDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "implement NewTimerDialogListener");
        }
    }

    // interface with the class
    public interface NewTimerDialogListener {
        void applyTexts(String name, int minutes, int seconds);
        void addTimer(Timer timer);
    }
}
