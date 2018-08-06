package com.example.acer.attandance_free_feature;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.acer.attandance_free_feature.db.entities.Schedules;

import java.util.List;

public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder> {
    private final LayoutInflater inflater;
    private List<Schedules> scheduleList;
    private int expandedPosition;

    ScheduleViewAdapter(Context ctx){inflater = LayoutInflater.from(ctx);}

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ScheduleViewAdapter.ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        final boolean isExpanded = position == expandedPosition;
        final int currPosition = position;
        Log.i("TEST", String.valueOf(scheduleList.size()));

        if(scheduleList != null){
            Schedules currSchedule = scheduleList.get(position);
            holder.details.setText(currSchedule.getDesc());
            holder.clientName.setText(currSchedule.getName());
            holder.repName.setText(currSchedule.getMeet());
        }else{
            holder.details.setText("No schedule");
        }

        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                expandedPosition = isExpanded? -1:currPosition;
                notifyDataSetChanged();
            }
        });
    }

    public void setSchedule(List<Schedules> schedule){
        this.scheduleList = schedule;
    }

    @Override
    public int getItemCount() {
        if(scheduleList != null){
            return scheduleList.size();
        }
        return 0;
    }

    public Schedules getChosenSchedule(){
        return scheduleList.get(expandedPosition);
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private final TextView details;
        private final TextView clientName;
        private final TextView repName;

        private ScheduleViewHolder(View scheduleView){
            super(scheduleView);

            details = scheduleView.findViewById(R.id.scheduleDetails);
            clientName = scheduleView.findViewById(R.id.clientName);
            repName = scheduleView.findViewById(R.id.repName);

        }
    }
}
