package com.example.acer.attandance_free_feature;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.example.acer.attandance_free_feature.adapter.ExportAdapter;
import com.example.acer.attandance_free_feature.data.Model;
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
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
    private String from = "";
    private String to = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        absensiAndSchedulesList = new ArrayList<>();
        context = this;
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

        boolean writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean readPermissions = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(!writePermission){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }

        if(!writePermission){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 2);
        }

        if(isExternalStorageReadable()){
            Log.d("TEST", "eksternal readable");
        }

        if(isExternalStorageWritable()){
            Log.d("TEST", "eksternal WRITE");
        }
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

        from   = edt_from.getText().toString();
        to     = edt_to.getText().toString();

//        wordViewModel.getAbsensiList().observe(this, new Observer<List<Absensi>>() {
//            @Override
//            public void onChanged(@Nullable List<Absensi> absensis) {
//                Log.d("TEST","hasil selurush absen" + absensis.get(6).getDate());
//                exportAdapter.setAbensi(absensis);
//                exportAdapter.notifyDataSetChanged();
//            }
//        });

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

        String folder = "Excel";
        File directory = new File(Environment.getExternalStorageDirectory() +"/"+folder);
        String csvFile = "myData1.xls";
        Log.d("TEST", csvFile);
        //create directory if not exist
        if (!directory.isDirectory()) {

            Log.d("TEST", "tidak ada directory");
            directory.mkdirs();
        }

        try {

            //file path
            Log.d("TEST", "ada directory");
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            sheet.addCell(new Label(0, 0, "Name"));
            sheet.addCell(new Label(1, 0, "client"));
            sheet.addCell(new Label(2, 0, "meet"));
            sheet.addCell(new Label(3, 0, "date"));
            sheet.addCell(new Label(4, 0, "time"));
            sheet.addCell(new Label(5, 0, "type"));

            Log.d("TEST", ""+absensiAndSchedulesList.size());
            for(int i = 0; i < absensiAndSchedulesList.size(); i++){

                Absensi absensi = absensiAndSchedulesList.get(i).getAbsensi();
                Schedules schedules = absensiAndSchedulesList.get(i).getSchedules();

                String name = Model.getInstance().name;
                String client_name = schedules.getName();
                String meet = schedules.getMeet();
                String date = absensi.getDate();
                String time = absensi.getTime();
                String type = absensi.getType();

                Log.d("TEST", name);

                sheet.addCell(new Label(0, i+1, name));
                sheet.addCell(new Label(1, i+1, client_name));
                sheet.addCell(new Label(2, i+1, meet));
                sheet.addCell(new Label(3, i+1, date));
                sheet.addCell(new Label(4, i+1, time));
                sheet.addCell(new Label(5, i+1, type));
            }

            workbook.write();
            workbook.close();

            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


}
