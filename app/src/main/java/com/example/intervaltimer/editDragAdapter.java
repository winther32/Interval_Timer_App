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
        // Should probably account for rare collision
        return ((int) masterList.get(position).ID.getMostSignificantBits() & Long.MAX_VALUE);
    }

    @NonNull
    @Override
    public DragItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Check for type and use type appropriate layout.
        if (viewType == TYPE_TIMER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.swipe_item, parent, false);
            return new editDragAdapter.TimerViewHolder(view, clickListener);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.swipe_set, parent, false);
            return new editDragAdapter.SetViewHolder(view, clickListener);
        } // TODO: add error catch
    }

    @Override
    public void onBindViewHolder(@NonNull DragItemAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        WorkoutItem item = masterList.get(position);

        if (item.getType() == TYPE_TIMER) {
            editDragAdapter.TimerViewHolder viewHolder = (editDragAdapter.TimerViewHolder) holder;
            // Assign vars.
            viewHolder.timerName.setText(item.getTimer().Name);
            viewHolder.timerClock.setText("" + String.format("%02d", item.getTimer().Minutes) + ":" +
                    String.format("%02d", item.getTimer().Seconds));

        } else if (item.getType() == TYPE_SET){
            editDragAdapter.SetViewHolder viewHolder = (editDragAdapter.SetViewHolder) holder;
            // Assign variables
            viewHolder.setName.setText(item.getSet().Name);
            viewHolder.repTime.setText("" + String.format("%02d", item.getSet().Minutes) + ":" +
                    String.format("%02d", item.getSet().Seconds));
            viewHolder.timerCount.setText(Integer.toString(item.getSet().size()));
            // Grammar for 1 or multiple timer(s)
            if (item.getSet().size() == 1) {
                viewHolder.timerCountText.setText(R.string.timer);
            } else {
                viewHolder.timerCountText.setText(R.string.timers);
            }
            viewHolder.iterations.setText("X" + Integer.toString(item.getSet().Iterations));
            // Set total time
            int totSec = item.getSet().getTotalTime();
            int min = totSec / 60;
            totSec = totSec % 60;
            viewHolder.totalTime.setText("" + String.format("%02d", min) + ":" +
                    String.format("%02d", totSec));

        }  // TODO: add a graceful error catch

    }

    @Override
    public int getItemViewType(int position) {
        return masterList.get(position).getType();
    }

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

    public class SetViewHolder extends DragItemAdapter.ViewHolder {

        TextView setName, timerCount, timerCountText, iterations, totalTime, repTime, setPosDel,
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