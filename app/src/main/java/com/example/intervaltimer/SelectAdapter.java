package com.example.intervaltimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {

    private ArrayList<Workout> allWorkouts;
    Context context;

    // Constructor
    public SelectAdapter(Context ct, ArrayList<Workout> workouts) {
        // Pull info from the workout list and arrange into more manageable arrays
        // ie names, length first timer, difficulty
        context = ct;
        allWorkouts = workouts;
    }

    @NonNull
    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_workout, parent, false);
        return new SelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {
        holder.wrkName.setText(allWorkouts.get(position).workoutName);
        holder.wrkRun.setText(String.valueOf(allWorkouts.get(position).getTotalTime()));
    }

    @Override
    public int getItemCount() {
        return allWorkouts.size();
    }

    public static class SelectViewHolder extends RecyclerView.ViewHolder {

        TextView wrkName, wrkRun;

        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);
            wrkName = itemView.findViewById(R.id.selectNameText);
            wrkRun = itemView.findViewById(R.id.selectRunView);
        }
    }
}
