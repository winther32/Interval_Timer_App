package com.example.intervaltimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;


public class WorkoutNameDialog extends AppCompatDialogFragment {

    EditText wrkNameText;
    WorkoutNameDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.workout_name_dialog, null);

        wrkNameText = view.findViewById(R.id.editWorkoutName);

        builder.setView(view)
                .setTitle("Name Your Workout")
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Launch to home screen.
                        //listener.toHome();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save the workout and init as empty timer list
                        //TODO: Save workout, fix recyclers and displays to handle empty wrkouts
                        String wrkName = wrkNameText.getText().toString().trim();
                        listener.passTitle(wrkName);
                    }
                });
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (WorkoutNameDialog.WorkoutNameDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "implement WorkoutNameDialogListener");
        }
    }

    // interface with the class
    public interface WorkoutNameDialogListener {
       void passTitle(String title);
       void toHome();
    }

}
