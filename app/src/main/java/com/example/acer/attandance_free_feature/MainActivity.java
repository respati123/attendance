package com.example.acer.attandance_free_feature;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
}
