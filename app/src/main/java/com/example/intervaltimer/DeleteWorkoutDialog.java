package com.example.intervaltimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

// Dialog alert used for deletion events as a verification of user intent.
// Mode and interface allow for flexible use with multiple deletion dialogs
// Uses methods to change the mode of the dialog to adapt to situation
// TODO: Mode is set via a constructor not a method
public class DeleteWorkoutDialog extends AppCompatDialogFragment {
    private DeleteWorkoutDialogListener listener;
    private String mode = "Workout"; // Default mode  of instance.
    private int pos; // Used when deleting sets

    // Method to change dialog to handle Set deletion. Pass pos of set to delete
    public void setMode(int position) {
        mode = "Set";
        pos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflation of dialog alert
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.delete_workout_dialog, null);

        // Build the dialog and functionality within the alert box
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing and close on cancel
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Based on mode call interface func.
                        // Use switch to add more methods
                        if ("Set".equals(mode)) {
                            listener.delete_dialog_Set(pos);
                        } else {
                            listener.deleteWorkout();
                        }
                    }
                });

        // Set the title of alert box based on mode
        if ("Set".equals(mode)) {
            builder.setTitle("Delete Set?");
        } else {
            builder.setTitle("Delete Workout?");
        }

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteWorkoutDialog.DeleteWorkoutDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "implement DeleteWorkoutDialogListener");
        }
    }

    // interface with the class
    public interface DeleteWorkoutDialogListener {
        void deleteWorkout();
        void delete_dialog_Set(int position);
    }

}
