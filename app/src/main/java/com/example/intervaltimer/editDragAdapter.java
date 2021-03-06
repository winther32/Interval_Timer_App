package com.example.intervaltimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

import static com.example.intervaltimer.TimeUnit.TYPE_SET;
import static com.example.intervaltimer.TimeUnit.TYPE_TIMER;

// Adapter for workout edit view. Allows items to be dragged to reorder them as well as
// side to side to expose the edit/delete options. This also holds both Set and Timer items in
// different holders.
// Taken from https://github.com/woxblom/DragListView
public class editDragAdapter extends DragItemAdapter<WorkoutItem, DragItemAdapter.ViewHolder> {

    private ArrayList<WorkoutItem> masterList;
    private int grabSet, grabTimer;
    private boolean mDragOnLongPress;
    private dragAdapterClickListener clickListener;

    public editDragAdapter(ArrayList<WorkoutItem> wrkList, int grabID, boolean dragOnLongPress,
                           dragAdapterClickListener clkListener) {
        masterList = wrkList;
        grabSet = grabID;
        grabTimer = R.id.timer_swipe_card;
        mDragOnLongPress = dragOnLongPress;
        setItemList(masterList);
        clickListener = clkListener;
    }

    @Override
    public long getUniqueItemId(int position) {
        // Bit manipulation should give good enough ID to (unlikely to get collision)
        return ((int) masterList.get(position).getID().getMostSignificantBits() & Long.MAX_VALUE);
    }

    @Override
    public int getItemViewType(int position) {
        return masterList.get(position).getType();
    }

    @NonNull
    @Override
    public DragItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Check for type and use type appropriate layout (Set or Timer).
        if (viewType == TYPE_TIMER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.swipe_item, parent, false);
            return new editDragAdapter.TimerViewHolder(view, clickListener);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.swipe_set, parent, false);
            return new editDragAdapter.SetViewHolder(view, clickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DragItemAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        WorkoutItem item = masterList.get(position);
        // Build/Fill in if Timer
        if (item.getType() == TYPE_TIMER) {
            editDragAdapter.TimerViewHolder viewHolder = (editDragAdapter.TimerViewHolder) holder;
            // Assign vars.
            viewHolder.timerName.setText(item.getTimer().getName());
            viewHolder.timerClock.setText("" + String.format("%02d", item.getMinutes()) + ":" +
                    String.format("%02d", item.getSeconds()));
        } else if (item.getType() == TYPE_SET){ // Build if Set item
            editDragAdapter.SetViewHolder viewHolder = (editDragAdapter.SetViewHolder) holder;
            // Assign variables
            viewHolder.setName.setText(item.getSet().getName());
            viewHolder.repTime.setText("" + String.format("%02d", item.getMinutes()) + ":" +
                    String.format("%02d", item.getSeconds()));
            viewHolder.timerCount.setText(Integer.toString(item.getSet().size()));
            // Grammar for 1 or multiple timer(s)
            if (item.getSet().size() == 1) {
                viewHolder.timerCountText.setText(R.string.timer);
            } else {
                viewHolder.timerCountText.setText(R.string.timers);
            }
            // Set iteration text
            viewHolder.iterations.setText("X" + Integer.toString(item.getSet().getIterations()));
            // Set total time
            int totSec = item.getSet().getTotalTime();
            int min = totSec / 60;
            totSec = totSec % 60;
            viewHolder.totalTime.setText("" + String.format("%02d", min) + ":" +
                    String.format("%02d", totSec));
        }
    }

    // Holder for Timers
    public class TimerViewHolder extends DragItemAdapter.ViewHolder {
        public TextView timerName, timerClock, timerPosDel, timerPosEdt;

        public TimerViewHolder(final View itemView, final dragAdapterClickListener listener) {
            super(itemView, grabTimer , mDragOnLongPress);

            timerName = itemView.findViewById(R.id.swipeTimerName);
            timerClock = itemView.findViewById(R.id.swipeTimerClock);
            timerPosDel = itemView.findViewById(R.id.swipeDeleteTimer);
            timerPosEdt = itemView.findViewById(R.id.swipeEditTimer);

            timerPosDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deleteTimer_dAdapter(getAdapterPosition());
                }
            });
            timerPosEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.editTimer_dAdapter(getAdapterPosition());
                }
            });
        }
    }

    // Holder for set items
    public class SetViewHolder extends DragItemAdapter.ViewHolder {
        public TextView setName, timerCount, timerCountText, iterations, totalTime, repTime, setPosDel,
                setPosEdt;

        public SetViewHolder(View itemView, final dragAdapterClickListener listener) {
            super(itemView, grabSet, mDragOnLongPress);

            setName = itemView.findViewById(R.id.swipeSetName);
            timerCount = itemView.findViewById(R.id.setTimerCount);
            timerCountText = itemView.findViewById(R.id.setTimerCountText);
            iterations = itemView.findViewById(R.id.setIters);
            totalTime = itemView.findViewById(R.id.setSwipeTotTime);
            repTime = itemView.findViewById(R.id.setSwipeRepTime);
            setPosDel = itemView.findViewById(R.id.setSwipeDelete);
            setPosEdt = itemView.findViewById(R.id.setSwipeEdit);

            // Set swipe and click funcs to have interface click funcs.
            setPosDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deleteSet_dAdapter(getAdapterPosition());
                }
            });
            setPosEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listener.editSet_dAdapter(getAdapterPosition());
                }
            });
        }
    }

    public interface dragAdapterClickListener {
        void deleteTimer_dAdapter(int position);
        void editTimer_dAdapter(int position);
        void deleteSet_dAdapter(int position);
        void editSet_dAdapter(int position);
    }

}