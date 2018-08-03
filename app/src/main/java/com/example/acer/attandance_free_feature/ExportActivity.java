package com.example.acer.attandance_free_feature;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExportActivity extends AppCompatActivity  {

    WordViewModel wordViewModel;

    private EditText edt_from;
    private EditText edt_to;
    private Context context;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        context = getApplicationContext();

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        edt_from = (EditText) findViewById(R.id.edt_from);
        edt_to = (EditText) findViewById(R.id.edt_to);
    }


    public void ExportFrom(View view){


        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(ExportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                edt_from.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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
                edt_to.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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

//        Log.d(wordViewModel.getAllCheckIn();


    }


}
