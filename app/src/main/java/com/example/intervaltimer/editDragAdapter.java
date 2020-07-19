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

    public editDragAdapter(ArrayList<WorkoutItem> wrkList, int grabID, boolean dragOnLongPress) {
        masterList = wrkList;
        grabSet = grabID;
        grabTimer= R.id.timer_swipe_card;
        mDragOnLongPress = dragOnLongPress;
        setItemList(masterList);
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
            return new editDragAdapter.TimerViewHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.swipe_set, parent, false);
            return new editDragAdapter.SetViewHolder(view);
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

            // Super Cheesy way to pass the pos of view to use for swipe clicks.
            viewHolder.timerPosDel.setHint("" + String.format("%d", position));
            viewHolder.timerPosEdt.setHint("" + String.format("%d", position));
        } else if (item.getType() == TYPE_SET){
            editDragAdapter.SetViewHolder viewHolder = (editDragAdapter.SetViewHolder) holder;
            // Assign variables
            viewHolder.setName.setText(item.getSet().Name);
            viewHolder.repTime.setText("" + String.format("%02d", item.getSet().Minutes) + ":" +
                    String.format("%02d", item.getSet().Seconds));
            viewHolder.timerCount.setText(Integer.toString(item.getSet().size()));

            int totSec = item.getSet().getTotalTime();
            int min = totSec / 60;
            totSec = totSec % 60;
            viewHolder.totalTime.setText("" + String.format("%02d", min) + ":" +
                    String.format("%02d", totSec));

            // Cheesy way to pass pos to view for swipe clicks.
            viewHolder.setPosDel.setHint("" + String.format("%d", position));
            viewHolder.setPosEdt.setHint("" + String.format("%d", position));
        }  // TODO: add a graceful error catch

    }

    @Override
    public int getItemViewType(int position) {
        return masterList.get(position).getType();
    }

    public class TimerViewHolder extends DragItemAdapter.ViewHolder {

        public TextView timerName, timerClock, timerPosDel, timerPosEdt;

        public TimerViewHolder(final View itemView) {
            super(itemView, grabTimer , mDragOnLongPress);

            timerName = itemView.findViewById(R.id.swipeTimerName);
            timerClock = itemView.findViewById(R.id.swipeTimerClock);
            timerPosDel = itemView.findViewById(R.id.swipeDelete);
            timerPosEdt = itemView.findViewById(R.id.swipeEdit);
        }
    }

    public class SetViewHolder extends DragItemAdapter.ViewHolder {

        TextView setName, timerCount, iterations, totalTime, repTime, setPosDel, setPosEdt;

        public SetViewHolder(View itemView) {
            super(itemView, grabSet, mDragOnLongPress);

            setName = itemView.findViewById(R.id.swipeSetName);
            timerCount = itemView.findViewById(R.id.setTimerCount);
            iterations = itemView.findViewById(R.id.setIters);
            totalTime = itemView.findViewById(R.id.setSwipeTotTime);
            repTime = itemView.findViewById(R.id.setSwipeRepTime);
            setPosDel = itemView.findViewById(R.id.setSwipeDelete);
            setPosEdt = itemView.findViewById(R.id.setSwipeEdit);
        }
    }

}