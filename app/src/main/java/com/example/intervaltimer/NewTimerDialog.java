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

    boolean editMode = false;
    String mName, mMin, mSec;
    int position;

    // Potential use for loading in info of a given timer
    public void editInstance(String tName, String tMin, String tSec, int pos) {
        editMode = true;
        mName = tName;
        mMin = tMin;
        mSec = tSec;
        position = pos;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_timer_creation, null);

        WorkoutName = view.findViewById(R.id.inWorkoutName);
        Min = view.findViewById(R.id.inMin);
        Sec = view.findViewById(R.id.inSec);

        builder.setView(view);

        // Change mode based on if Editing a timer or making a new one
        if (editMode) {
            builder.setTitle(getString(R.string.edit_timer));
            WorkoutName.setText(mName);
            Min.setText(mMin);
            Sec.setText(mSec);
        } else {
            builder.setTitle(getString(R.string.new_timer));
        }

        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Want nothing to happen so nothing here
                    }
                })
                .setPositiveButton(getString(R.string.Done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Where pass editText info back to underlying activity (workout view)
                        // Will make new/edit the timer at this point
                        String name = WorkoutName.getText().toString();
                        String minutes = Min.getText().toString();
                        String seconds = Sec.getText().toString();
                        int intMinutes;
                        int intSeconds;

                        // Null pointer checks on user number input
                        if (minutes.trim().length() == 0) {
                            intMinutes = 0;
                        } else {intMinutes = Integer.parseInt(minutes);}
                        if (seconds.trim().length() == 0) {
                            intSeconds = 0;
                        } else {intSeconds = Integer.parseInt(seconds);}

                        // Some time info must be provided to create a new timer
                        if (intMinutes != 0 || intSeconds != 0) {
                            // Convert inputs to appropriate sizes if needed
                            intMinutes += intSeconds / 60;
                            intSeconds = intSeconds % 60;

                            // Set a maximum min size for display.
                            if (intMinutes > 99) {
                                intMinutes = 99;
                            }

                            Timer timer = new Timer(name, intMinutes, intSeconds);
                            WorkoutItem item= new WorkoutItem(timer);
                            if (editMode) {
                                listener.editTimer(item, position);
                            } else { listener.addTimer(item); }
                        }
                    }
                });

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
        void addTimer(WorkoutItem timer);
        void editTimer(WorkoutItem timer, int pos);
    }
}
