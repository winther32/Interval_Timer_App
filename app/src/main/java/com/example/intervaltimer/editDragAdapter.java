package com.example.intervaltimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class editDragAdapter extends DragItemAdapter<TimeUnit, DragItemAdapter.ViewHolder> {

    private ArrayList<TimeUnit> masterList;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;

    public editDragAdapter(ArrayList<TimeUnit> wrkList, int grabID, boolean dragOnLongPress) {
        masterList = wrkList;
        mGrabHandleId = grabID;
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
    public editDragAdapter.TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.swipe_item, parent, false);
        return new editDragAdapter.TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DragItemAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TimeUnit unit = masterList.get(position);

        // TODO: determine if set or timer and bind accordingly

        if (unit instanceof Timer) {
            // Cast super-classed items to appropriate subclass
            Timer timer = (Timer) unit;
            editDragAdapter.TimerViewHolder viewHolder = (editDragAdapter.TimerViewHolder) holder;
            // Assign vars.
            viewHolder.timerName.setText(timer.Name);
            viewHolder.timerClock.setText("" + String.format("%02d", timer.Minutes) + ":" +
                    String.format("%02d", timer.Seconds));

            // Super Cheesy way to pass the pos of view to use for swipe clicks.
            viewHolder.timerPosDel.setHint("" + String.format("%d", position));
            viewHolder.timerPosEdt.setHint("" + String.format("%d", position));
        } else {
            // Cast super-classed items to appropriate subclass
            Set set = (Set) unit;
            editDragAdapter.SetViewHolder viewHolder = (editDragAdapter.SetViewHolder) holder;
            // Assign variables
            viewHolder.setName.setText(set.Name);
            viewHolder.totalTime.setText("Total Time" + String.format("%02d", set.Minutes) + ":" +
                    String.format("%02d", set.Seconds));

            // Cheesy way to pass pos to view for swipe clicks.
            viewHolder.setPosDel.setHint("" + String.format("%d", position));
            viewHolder.setPosEdt.setHint("" + String.format("%d", position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return masterList.get(position).getType();
    }

    public class TimerViewHolder extends DragItemAdapter.ViewHolder {

        public TextView timerName, timerClock, timerPosDel, timerPosEdt;

        public TimerViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);

            timerName = itemView.findViewById(R.id.swipeTimerName);
            timerClock = itemView.findViewById(R.id.swipeTimerClock);
            timerPosDel = itemView.findViewById(R.id.swipeDelete);
            timerPosEdt = itemView.findViewById(R.id.swipeEdit);
        }
    }

    public class SetViewHolder extends DragItemAdapter.ViewHolder {

        TextView setName, timerCount, iterations, totalTime, repTime, setPosDel, setPosEdt;

        public SetViewHolder(View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);

            setName = itemView.findViewById(R.id.swipeSetName);
            timerCount = itemView.findViewById(R.id.setTimerCount);
            iterations = itemView.findViewById(R.id.setIters);
            totalTime = itemView.findViewById(R.id.setTotalTime);
            repTime = itemView.findViewById(R.id.setRepTime);
            setPosDel = itemView.findViewById(R.id.setSwipeDelete);
            setPosEdt = itemView.findViewById(R.id.setSwipeEdit);
        }
    }
}