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

// Dialog used to get input for a new timer obj. as well as edit timer info
public class NewTimerDialog extends AppCompatDialogFragment {

    private EditText WorkoutName;
    private EditText Min, Sec;
    private NewTimerDialogListener listener;
    private boolean editMode = false;
    private String mName, mMin, mSec;
    private int position;

    // Used for loading in info of a given timer. Particularly for when editing a timer
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

        // Change mode based on if editing a timer or making a new one
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
                        // Do nothing and close
                    }
                })
                .setPositiveButton(getString(R.string.Done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Where pass editText info back to underlying activity (workout view)
                        String name = WorkoutName.getText().toString();
                        String minutes = Min.getText().toString();
                        String seconds = Sec.getText().toString();
                        int intMinutes;
                        int intSeconds;

                        // Null pointer checks on user number input. Default to 0
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

                            // Set a maximum minute size for display.
                            if (intMinutes > 99) {
                                intMinutes = 99;
                            }

                            // Create a new item with new info.
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

    // Note that this dialog passes the new timer item via this interface.
    public interface NewTimerDialogListener {
        void addTimer(WorkoutItem timer);
        void editTimer(WorkoutItem timer, int pos);
    }
}
