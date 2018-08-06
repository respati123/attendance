package com.example.acer.attandance_free_feature;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.acer.attandance_free_feature.adapter.ExportAdapter;
import com.example.acer.attandance_free_feature.data.MyDividerItemDecoration;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiAndSchedule;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExportActivity extends AppCompatActivity  {

    WordViewModel wordViewModel;

    private EditText edt_from;
    private EditText edt_to;
    private Button importExcel;


    private Context context;
    SimpleDateFormat dateFormat;
    private RecyclerView recyclerView;
    private ExportAdapter exportAdapter;
    private List<AbsensiAndSchedule> absensiAndSchedulesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        absensiAndSchedulesList = new ArrayList<>();
        context = getApplicationContext();
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        edt_from = (EditText) findViewById(R.id.edt_from);
        edt_to = (EditText) findViewById(R.id.edt_to);
        importExcel = (Button) findViewById(R.id.import_excel);

        exportAdapter = new ExportAdapter(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayout.VERTICAL, 16));
        recyclerView.setAdapter(exportAdapter);
    }


    public void ExportFrom(View view){


        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(ExportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                edt_from.setText(dateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dp.show();
    }

    public void ExportTo(View view){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(ExportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                edt_to.setText(dateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dp.show();
    }

    public void FilterData(View view){


        if(edt_from.getText().toString().isEmpty()){
            edt_from.setError("required");
            edt_from.requestFocus();
            return;
        }

        if(edt_from.getText().toString().isEmpty()){
            edt_from.setError("required");
            edt_from.requestFocus();
            return;
        }

        final String from   = edt_from.getText().toString();
        final String to     = edt_to.getText().toString();

        wordViewModel.getAbsensiAndSchedulesList(from, to).observe(this, new Observer<List<AbsensiAndSchedule>>() {
            @Override
            public void onChanged(@Nullable List<AbsensiAndSchedule> absensiAndSchedules) {
                Log.d("TEST", ""+absensiAndSchedules.size());

                if(absensiAndSchedules != null){
                    absensiAndSchedulesList = absensiAndSchedules;

                    exportAdapter.setAbsensiAndSchedule(absensiAndSchedules);
                    exportAdapter.notifyDataSetChanged();

                    importExcel.setVisibility(View.VISIBLE);

                }
            }
        });




    }

    public void importToExcel(View view){

        File sd = Environment.getExternalStorageDirectory();

        File directory = new File(sd.getAbsolutePath());

        if(!directory.isDirectory()){
            directory.mkdirs();
        }

//        try {
//            File file = new File(directory, )
//        }
    }


}
