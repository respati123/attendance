package com.example.acer.attandance_free_feature.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;

import com.example.acer.attandance_free_feature.MainActivity;
import com.example.acer.attandance_free_feature.NavigationDrawerActivity;
import com.example.acer.attandance_free_feature.RegisterUsersActivity;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;


public class LoadingAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Button btn;
    private EditText username, name;
    private WordViewModel wm;
    ProgressDialog pd;


    public LoadingAsyncTask(RegisterUsersActivity registerUsersActivity, Button btn, WordViewModel wm, EditText username, EditText name) {
        this.context = registerUsersActivity;
        this.btn = btn;
        this.wm = wm;
        this.username = username;
        this.name = name;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        int i = 0;

        Users users = new Users();
        users.setUsername(username.getText().toString());
        users.setName(name.getText().toString());
        wm.insert(users);

        while(i < 5){

            try {

                Thread.sleep(1000);
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        btn.setEnabled(true);
        pd.dismiss();

//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setMessage("Loading ......");
        pd.show();

    }

}
