package com.example.acer.attandance_free_feature.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.acer.attandance_free_feature.R;
import com.example.acer.attandance_free_feature.ScheduleActivity;
import com.example.acer.attandance_free_feature.ScheduleMainActivity;
import com.example.acer.attandance_free_feature.db.entities.Schedules;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapterMain extends RecyclerView.Adapter<ScheduleAdapterMain.ScheduleMainView> implements Filterable{

    private final LayoutInflater layoutInflater;
    private List<Schedules> scheduleList;
    private List<Schedules> scheduleListered;
    private int expandedPosition;
    private Context ctx;

    public ScheduleAdapterMain(Context context){
        layoutInflater = LayoutInflater.from(context);
        ctx = context;
    }

    @NonNull
    @Override
    public ScheduleMainView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.schedule_list_row, parent, false);
        return new ScheduleMainView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleMainView holder, int position) {
        final boolean isExpand = position == expandedPosition;
        final int currPosition = position;


        if(scheduleList != null){
            Schedules schedules = scheduleList.get(position);
            holder.txtNameClient.setText(schedules.getName());
            holder.txtMeetClient.setText(schedules.getMeet());
            holder.txtTanggal.setText(schedules.getDate());

        }

        holder.linearLayout.setVisibility(isExpand?View.VISIBLE:View.GONE);
        holder.relativeLayout.setVisibility(isExpand?View.GONE:View.VISIBLE);
        holder.itemView.setActivated(isExpand);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPosition = isExpand?-1:currPosition;
                notifyDataSetChanged();
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ScheduleActivity.class);
                Schedules currSchedule = getCurrentSchedule();
                intent.putExtra("date", currSchedule.getDate());
                intent.putExtra("desc", currSchedule.getDesc());
                intent.putExtra("user_id", currSchedule.getId_users());
                intent.putExtra("id", currSchedule.getId());
                intent.putExtra("job", currSchedule.getJob());
                intent.putExtra("meet", currSchedule.getMeet());
                intent.putExtra("name", currSchedule.getName());
                intent.putExtra("service", currSchedule.getService());
                intent.putExtra("time", currSchedule.getTime());
                intent.putExtra("edit", true);
                //start activity
                ctx.startActivity(intent);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString  =constraint.toString();
                if(charString.isEmpty()){
                    scheduleListered = scheduleList;
                }else {
                    List<Schedules> filteredList = new ArrayList<>();
                    for (Schedules row: scheduleList){
                        if(row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDate().contains(constraint)){
                            filteredList.add(row);
                        }
                    }

                    scheduleListered = scheduleList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = scheduleListered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                scheduleListered = (ArrayList<Schedules>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void filterList(List<Schedules> filterList) {
        scheduleList = filterList;
        notifyDataSetChanged();
        }

    public Schedules getCurrentSchedule(){return scheduleList.get(expandedPosition);}

    public class ScheduleMainView extends RecyclerView.ViewHolder{

        private TextView txtNameClient;
        private TextView txtMeetClient;
        private TextView txtTanggal;
        private LinearLayout linearLayout;
        private RelativeLayout relativeLayout;
        private Button button;

        private ScheduleMainView(View itemView) {
            super(itemView);

            txtNameClient = itemView.findViewById(R.id.name_client);
            txtMeetClient = itemView.findViewById(R.id.meet_client);
            txtTanggal    = itemView.findViewById(R.id.tanggal_client);
            linearLayout  = itemView.findViewById(R.id.linierlayout);
            relativeLayout = itemView.findViewById(R.id.relative);
            button = itemView.findViewById(R.id.edit_schedule_btn);

        }
    }
}

