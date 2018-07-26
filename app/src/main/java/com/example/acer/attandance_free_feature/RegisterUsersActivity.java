package com.example.acer.attandance_free_feature;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.acer.attandance_free_feature.AsyncTask.LoadingAsyncTask;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;
import com.stephentuso.welcome.WelcomeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterUsersActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.username) EditText username;

    @BindView(R.id.name) EditText name;

    @BindView(R.id.buttonSave) Button buttonSave;

    private WordViewModel wordViewModel;
    private WelcomeHelper wh;
    SharedPreferences sp;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wh = new WelcomeHelper(this, WelcomeActivity.class);
        wh.show(savedInstanceState);

        sp = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("firsttime", true);

        if(!isFirst){
            startActivity(new Intent(RegisterUsersActivity.this, MainActivity.class));
        }

        setContentView(R.layout.register_users_activity);
        ButterKnife.bind(this);



        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        buttonSave.setOnClickListener(this);


    }

    private boolean ValidationForm() {

        if(TextUtils.isEmpty(username.getText())){
            username.setError("cannot empty");
            username.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(name.getText())){
            name.setError("cannot empty");
            name.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSave:
                Log.d("TEST", "click button");
                if(ValidationForm()){

                    Log.d("TEST", "validation true");
//                    startActivity(new Intent(RegisterUsersActivity.this, NavigationDrawerActivity.class));
//                    finish();
                      LoadingAsyncTask loadingAsyncTask = new LoadingAsyncTask(this, buttonSave, wordViewModel, username, name);
                      loadingAsyncTask.execute();
                      buttonSave.setEnabled(false);


                } else {

                    Log.d("TEST", "validation false");
                }
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        wh.onSaveInstanceState(outState);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("firsttime", false);
        editor.commit();
    }


}
