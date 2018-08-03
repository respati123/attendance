package com.example.acer.attandance_free_feature.AsyncTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.acer.attandance_free_feature.ScheduleActivity;
import com.example.acer.attandance_free_feature.ScheduleMainActivity;
import com.example.acer.attandance_free_feature.data.Model;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import java.util.HashMap;

public class AsyncTaskInsertSchedule extends AsyncTask<Void, Void, Void> {

    private WordViewModel wmv;
    private Schedules schedules;
    private Context context;
    ProgressDialog pd;
    private Dialog dialog;
    private Button buttonSave;
    private HashMap<String, String> list;

    public AsyncTaskInsertSchedule(HashMap<String, String> list, ScheduleActivity scheduleActivity, Button button, WordViewModel wmv) {
        this.list = list;
        this.context = scheduleActivity;
        this.buttonSave = button;
        this.wmv = wmv;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        pd.dismiss();

        buttonSave.setEnabled(true);

        Intent intent = new Intent(context, ScheduleMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected Void doInBackground(Void... Void) {

        Log.d("TEST", ""+Model.getInstance().id);
        Schedules schedules = new Schedules();
        schedules.setName(list.get("name"));
        schedules.setJob(list.get("job"));
        schedules.setService(list.get("service"));
        schedules.setDate(list.get("date"));
        schedules.setTime(list.get("time"));
        schedules.setMeet(list.get("meet"));
        schedules.setDesc(list.get("desc"));
        schedules.setId_users(1);

        wmv.insert(schedules);
        int i = 0;
        while (i < 5){

            try {
                Thread.sleep(1000);
                if(i == 3){
                    pd.setMessage("Successfully..");
                }
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage("loading ....");
        pd.show();
    }

}
