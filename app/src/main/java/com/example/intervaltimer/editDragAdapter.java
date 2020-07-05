package com.example.intervaltimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class editDragAdapter extends DragItemAdapter<Timer, editDragAdapter.editDragViewHolder> {

    private ArrayList<Timer> timerArrayList;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;

    public editDragAdapter(ArrayList<Timer> timerList, int grabID, boolean dragOnLongPress) {
        timerArrayList = timerList;
        mGrabHandleId = grabID;
        mDragOnLongPress = dragOnLongPress;
        setItemList(timerArrayList);
    }

    @Override
    public long getUniqueItemId(int position) {
        return (timerArrayList.get(position).ID.getMostSignificantBits() & Long.MAX_VALUE);
    }

    @NonNull
    @Override
    public editDragAdapter.editDragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.edit_row_timer, parent, false);
        return new editDragAdapter.editDragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull editDragAdapter.editDragViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Timer timer = timerArrayList.get(position);
        holder.timerName.setText(timer.Name);
        holder.timerClock.setText("" + String.format("%02d", timer.Minutes) + ":" +
                String.format("%02d", timer.Seconds));
    }

    public class editDragViewHolder extends DragItemAdapter.ViewHolder {

        public TextView timerName, timerClock;

        public editDragViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);

            timerName = itemView.findViewById(R.id.editTimerName);
            timerClock = itemView.findViewById(R.id.editTimerClock);
        }
    }
}