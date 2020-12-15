package com.example.intervaltimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// This adapter is only used in the run workout activity and only holds timers.
// Holds the upcoming timers displayed while the workout is running.
// TODO: refactor to change name to something more appropriate
public class EditAdapter extends RecyclerView.Adapter<EditAdapter.EditViewHolder> {
    private ArrayList<Timer> timerList;
    private Context context;

    public EditAdapter(Context ct, ArrayList<Timer> tList) {
        context = ct;
        timerList = tList;
    }


    @NonNull
    @Override
    public EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_row_timer, parent, false);
        return new EditViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EditViewHolder holder, int position) {
        Timer timer = timerList.get(position);
        holder.timerName.setText(timer.getName());
        holder.timerClock.setText("" + String.format("%02d", timer.getMinutes()) + ":" +
                String.format("%02d", timer.getSeconds()));
        if (timer.getParentName() == null) {
            holder.parentSet.setText("");
        } else {
            holder.parentSet.setText(timer.getParentName());
        }
    }

    @Override
    public int getItemCount() {
        return timerList.size();
    }

    // holder for a row in the recyclerview
    public static class EditViewHolder extends RecyclerView.ViewHolder {
        TextView timerName, timerClock, parentSet;

        public EditViewHolder(@NonNull View itemView) {
            super(itemView);
            timerName = itemView.findViewById(R.id.editTimerName);
            timerClock = itemView.findViewById(R.id.editTimerClock);
            parentSet = itemView.findViewById(R.id.timerCardSetName);
        }
    }
}
