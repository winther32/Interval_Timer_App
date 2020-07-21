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


public class DeleteWorkoutDialog extends AppCompatDialogFragment {

    DeleteWorkoutDialogListener listener;
    String mode = "Workout"; // To help det title;
    int pos; // For set mode

    public void setMode(int position) {
        mode = "Set";
        pos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.delete_workout_dialog, null);


        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing on cancel
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("Set".equals(mode)) {
                            listener.deleteSet(pos);
                        } else {
                            listener.deleteWorkout();
                        }
                    }
                });

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
        void deleteSet(int position);
    }

}
