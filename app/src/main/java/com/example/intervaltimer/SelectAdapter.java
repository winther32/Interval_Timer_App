package com.example.intervaltimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// This is the adapter for the RecyclerView of all workouts in the home page. Holds workout items
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {

    private ArrayList<Workout> allWorkouts;
    private Context context;
    private OnWorkoutClickListener workoutClickListener;

    public SelectAdapter(Context ct, ArrayList<Workout> workouts, OnWorkoutClickListener clickListener) {
        context = ct;
        allWorkouts = workouts;
        workoutClickListener = clickListener;
    }

    @NonNull
    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_workout, parent, false);
        return new SelectViewHolder(view, workoutClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {
        // Set name
        holder.wrkName.setText(allWorkouts.get(position).workoutName);
        // Set total time of workout
        int totSec = allWorkouts.get(position).getTotalTime();
        int Min = totSec / 60;
        totSec = totSec % 60;
        holder.wrkRun.setText("Total Time " + String.format("%02d", Min) +
                ":" + String.format("%02d", totSec));
    }

    @Override
    public int getItemCount() {
        return allWorkouts.size();
    }

    // Holder for workout items
    public class SelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView wrkName, wrkRun;

        public SelectViewHolder(@NonNull View itemView, OnWorkoutClickListener listener) {
            super(itemView);
            wrkName = itemView.findViewById(R.id.selectNameText);
            wrkRun = itemView.findViewById(R.id.selectRunView);
            workoutClickListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            workoutClickListener.workoutClicked(allWorkouts.get(getAdapterPosition()));
        }
    }

    // Interface for clicking the recycler view
    public interface OnWorkoutClickListener {
        void workoutClicked(Workout workout);
    }
}
