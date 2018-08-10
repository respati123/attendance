package com.example.acer.attandance_free_feature;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.attandance_free_feature.data.Model;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String LOG = "TEST";

    @BindView(R.id.toolBarPicture) CircleImageView toolbarPic;

    @BindView(R.id.nameToolbar) TextView nameToolbar;

    Context context;
    private String name, username, image;
    private int id;
    private WordViewModel wordViewModel;
    private LiveData<List<Users>> getDataUser;
    private boolean fineLocPermission;
    private boolean coarseLocPermission;
    private boolean externalStoragePermission;

    private RecyclerView mRecyclerView;
    private ScheduleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fineLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        coarseLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        externalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(!fineLocPermission){
            Log.v("TEST", "NO PERMISSION FOR LOCATION, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
        }

        if(!coarseLocPermission){
            Log.v("TEST", "NO PERMISSION FOR LOCATION, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1
            );
        }

        if(!externalStoragePermission) {
            Log.v("TEST", "NO PERMISSION FOR EXTERNAL STORAGE, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
        }

        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nameToolbar = findViewById(R.id.nameToolbar);
        toolbarPic = findViewById(R.id.toolBarPicture);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        getUserInformation(getDataUser = wordViewModel.getUserList());




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CircleImageView circleImageView = toolbar.findViewById(R.id.toolBarPicture);
        circleImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }

    private void getUserInformation(LiveData<List<Users>> getDataUser) {

        getDataUser.observe(this, new Observer<List<Users>>() {
            @Override
            public void onChanged(@Nullable List<Users> users) {
                if(users != null){
                    for (int i = 0; i < users.size(); i++){
                        Users user = users.get(0);
                        Model.getInstance().id = user.getId();
                        Model.getInstance().image = user.getImage();
                        Model.getInstance().username = user.getUsername();
                        Model.getInstance().name = user.getName();

                        username = user.getUsername();
                        name = user.getName();
                        id = user.getId();
                        image = user.getImage();

                        if(image != null) {
                            nameToolbar.setText(name);
                            decodeStringToBitmap decodeStringToBitmap = new decodeStringToBitmap(image);
                            Bitmap imageBitmap = decodeStringToBitmap.decode();
                            toolbarPic.setImageBitmap(imageBitmap);
                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        AlertDialog.Builder dialog;
        View mView;
        final AlertDialog ad;

        switch (id){
            case R.id.bahasa:
                dialog = new AlertDialog.Builder(MainActivity.this);
                mView = getLayoutInflater().inflate(R.layout.view_menu_bahasa, null);
                dialog.setTitle("RESPATII");
                dialog.setView(mView);
                ad = dialog.create();

                Button btnIndonesia = (Button) mView.findViewById(R.id.bhs_indo);
                btnIndonesia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Indonesia", Toast.LENGTH_LONG).show();
                        ad.dismiss();
                    }
                });

                Button btnInggris = (Button) mView.findViewById(R.id.bhs_ing);
                btnInggris.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "inggris", Toast.LENGTH_LONG).show();
                        ad.dismiss();
                    }
                });

                ad.show();

                break;
            case R.id.local:

                dialog = new AlertDialog.Builder(MainActivity.this);
                mView = getLayoutInflater().inflate(R.layout.view_menu_local, null);
                dialog.setTitle("Local Data");
                dialog.setView(mView);
                ad = dialog.create();
                //EditText
                final EditText password = (EditText) findViewById(R.id.local_data);
                dialog.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Saved" , Toast.LENGTH_LONG).show();
                        ad.dismiss();
                    }
                });
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Saved" , Toast.LENGTH_LONG).show();
                        password.clearFocus();
                        ad.dismiss();
                    }
                });

                ad.show();
                break;
            default:
                    break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToCheckIn(View view) {
        fineLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        coarseLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        externalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(!coarseLocPermission && !fineLocPermission){
            Log.v("TEST", "NO PERMISSION FOR LOCATION, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1
            );

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
            if(!coarseLocPermission && !fineLocPermission) return;
        }

        if(!externalStoragePermission) {
            Log.v("TEST", "NO PERMISSION FOR EXTERNAL STORAGE, ATTEMPT REQUEST");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
            if(!externalStoragePermission) return;
        }

        Intent intent = new Intent(this, CheckInActivity.class);
        startActivity(intent);
    }

    public void goToSchedule(View view){

        startActivity(new Intent(MainActivity.this, ScheduleMainActivity.class));
        finish();
    }

    public void goToExport(View view){
        startActivity(new Intent(MainActivity.this, ExportActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        if(doubleBackToExitPressedOnce){

            super.finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please Click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
