package com.example.acer.attandance_free_feature;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder> {
    private final LayoutInflater inflater;

    ScheduleViewAdapter(Context ctx){inflater = LayoutInflater.from(ctx);}



    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private final TextView details;
        private ScheduleViewHolder(View scheduleView){
            super(scheduleView);

            details = scheduleView.findViewById(R.id.scheduleDetails);

        }
    }
}
