package com.example.acer.attandance_free_feature;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.acer.attandance_free_feature.AsyncTask.AsyncTaskInsertSchedule;
import com.example.acer.attandance_free_feature.data.Model;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.app.DatePickerDialog.*;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    //BindView
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.client_name)
    EditText edt_client;

    @BindView(R.id.job)
    EditText edt_job;

    @BindView(R.id.service)
    EditText edt_service;

    @BindView(R.id.date)
    EditText edt_date;

    @BindView(R.id.time)
    EditText edt_time;

    @BindView(R.id.meet)
    EditText edt_meet;

    @BindView(R.id.description)
    EditText edt_description;

    @BindView(R.id.save)
    Button btn_save;

    @BindView(R.id.cancel)
    Button btn_cancel;

    @BindView(R.id.icon_date)
    ImageView ic_date;

    @BindView(R.id.icon_time)
    ImageView ic_time;

    //initiate
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    SimpleDateFormat dateFormat;
    Context context;
    WordViewModel wvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        context = this;

        wvm = ViewModelProviders.of(this).get(WordViewModel.class);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mToolbar    = findViewById(R.id.toolbar);
        edt_client  = findViewById(R.id.client_name);
        edt_job     = findViewById(R.id.job);
        edt_service = findViewById(R.id.service);
        edt_date    = findViewById(R.id.date);
        edt_time    = findViewById(R.id.time);
        edt_meet    = findViewById(R.id.meet);
        edt_description    = findViewById(R.id.description);
        ic_date     = findViewById(R.id.icon_date);
        ic_time     = findViewById(R.id.icon_time);

        btn_save    = findViewById(R.id.save);
        btn_cancel  = findViewById(R.id.cancel);

        setDateTimeField();

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //check if from edit
        //if edit add extras to edit text
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setDateTimeField() {

        ic_date.setOnClickListener(this);
        ic_time.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                edt_date.setText(dateFormat.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(hourOfDay, minute);
                edt_time.setText(hourOfDay + ":" + minute);
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
    }

    private boolean validationField() {

        if(TextUtils.isEmpty(edt_client.getText())){
            edt_client.setError("cannot empty");
            edt_client.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(edt_job.getText())){
            edt_job.setError("cannot empty");
            edt_job.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(edt_service.getText())){
            edt_service.setError("cannot empty");
            edt_service.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(edt_date.getText())){
            edt_date.setError("cannot empty");
            edt_date.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(edt_time.getText())){
            edt_time.setError("cannot empty");
            edt_time.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(edt_meet.getText())){
            edt_meet.setError("cannot empty");
            edt_meet.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(edt_description.getText())){
            edt_description.setError("cannot empty");
            edt_description.requestFocus();
            return false;
        }

        return true;
    }

    private HashMap<String, String> getValuefromView(){

        final int idUser = Model.getInstance().id;
        HashMap<String, String> list = new HashMap<>();
        list.put("name", edt_client.getText().toString());
        list.put("job", edt_job.getText().toString());
        list.put("service", edt_service.getText().toString());
        list.put("date", edt_date.getText().toString());
        list.put("time", edt_time.getText().toString());
        list.put("meet", edt_meet.getText().toString());
        list.put("desc", edt_description.getText().toString());
        list.put("id", String.valueOf(idUser));

        return list;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.icon_date:
                datePickerDialog.show();
                break;
            case R.id.icon_time:
                timePickerDialog.show();
                break;
            case R.id.save:
                if(validationField()){

                    //check if edit mode, update data

                    AsyncTaskInsertSchedule asyncTaskInsertSchedule = new AsyncTaskInsertSchedule(getValuefromView(), this, btn_save, wvm);
                    asyncTaskInsertSchedule.execute();
                    btn_save.setEnabled(false);
                }
                default:
                    break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScheduleActivity.this, ScheduleMainActivity.class));
        finish();
    }
}
