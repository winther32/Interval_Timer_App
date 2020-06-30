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

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.EditViewHolder> {

    private ArrayList<Timer> timerList;
    Context context;

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
        holder.timerName.setText(timer.Name);
        holder.timerClock.setText("" + String.format("%02d", timer.Minutes) + ":" +
                String.format("%02d", timer.Seconds));
    }

    @Override
    public int getItemCount() {
        return timerList.size();
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {

        TextView timerName, timerClock;

        public EditViewHolder(@NonNull View itemView) {
            super(itemView);
            timerName = itemView.findViewById(R.id.editTimerName);
            timerClock = itemView.findViewById(R.id.editTimerClock);
        }
    }
}
