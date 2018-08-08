package com.example.acer.attandance_free_feature.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.acer.attandance_free_feature.R;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiAndSchedule;
import com.example.acer.attandance_free_feature.db.entities.Schedules;

import org.w3c.dom.Text;

import java.util.List;

public class ExportAdapter extends RecyclerView.Adapter<ExportAdapter.MyViewHolder>{

    private final LayoutInflater inflater;
    private List<AbsensiAndSchedule> absensiAndScheduleList;
    private List<Absensi> absensiList;


    public ExportAdapter(Context ctx){
        inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ExportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.export_list_row, parent, false);
        return new ExportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExportAdapter.MyViewHolder holder, int position) {


        if(absensiAndScheduleList != null){
            Absensi absensi = absensiAndScheduleList.get(position).getAbsensi();
            Schedules schedules = absensiAndScheduleList.get(position).getSchedules();

            holder.client_name.setText("Client :" + schedules.getName());
            holder.type.setText("Type :" + absensi.getType());
            holder.tanggal.setText(absensi.getDate());
            holder.time.setText(absensi.getTime());

        } else if(absensiList != null){
            Absensi absensi = absensiList.get(position);
            holder.client_name.setText("Client :" + absensi.getId());
            holder.type.setText("Type :" + absensi.getType());
            holder.tanggal.setText(absensi.getDate());
            holder.time.setText(absensi.getTime());
        }
    }

    @Override
    public int getItemCount() {
        if(absensiList != null){
            return absensiList.size();

        } else if(absensiAndScheduleList != null){

            return absensiAndScheduleList.size();

        } else {

            return 0;
        }
    }

    public List<Absensi> setAbensi(List<Absensi> abensi) {
        return absensiList = abensi;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView client_name, type, tanggal, time;

        public MyViewHolder(View itemView) {
            super(itemView);

            client_name = (TextView) itemView.findViewById(R.id.name_client);
            type = (TextView) itemView.findViewById(R.id.type);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal_client);
            time = (TextView) itemView.findViewById(R.id.time_client);
        }
    }

    public void setAbsensiAndSchedule (List<AbsensiAndSchedule> absensiAndSchedule){
        this.absensiAndScheduleList = absensiAndSchedule;
    }
}
