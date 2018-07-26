package com.example.acer.attandance_free_feature;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.example.acer.attandance_free_feature.data.Model;
import com.example.acer.attandance_free_feature.db.dao.UsersDao;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;
import com.stephentuso.welcome.WelcomeHelper;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.acer.attandance_free_feature.R.menu.navigation_drawer;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {



    WelcomeHelper wh;
    Menu drawerMenu;
    NavigationView navigationView;
    Users users;
    private WordViewModel wvm;
    public int id;
    public String username, name, image;
    private boolean fineLocPermission;
    private boolean coarseLocPermission;
    private boolean externalStoragePermission;


    //inisialisasi View
    @BindView(R.id.imageView) CircleImageView ImageViewHeader;
    @BindView(R.id.username_user) TextView txtUsername;
    @BindView(R.id.name_user) TextView txtName;
    View headerview;


    static final int TAKE_SELFIE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wvm = ViewModelProviders.of(this).get(WordViewModel.class);
        users = new Users();

        setContentView(R.layout.activity_navigation_drawer);
        //inisialisasi Toolbar dan sett toolbar di atas kanan
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        //inisialisasi floating button
        //Setonclicklistener sebagai eksekusi jika kita klik tombol floating
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //inisialisasi navigation drawer
        //membuat tombol toggle di toolbar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //inissialisasi untuk navigation view isi dari navigation drawer, misal header dan menu
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerview = navigationView.getHeaderView(0);

        //create view imageview and textview
        ImageViewHeader = headerview.findViewById(R.id.imageView);
        txtUsername     = headerview.findViewById(R.id.username_user);
        txtName         = headerview.findViewById(R.id.name_user);

        //function get user data
        getUserData();

        //initialisasi variable user

        id = Model.getInstance().id;
        username = Model.getInstance().username;
        name = Model.getInstance().name;
        image = Model.getInstance().image;

        //input data activity users

    }

    private void getUserData() {
        wvm.getUserList().observe(this, new Observer<List<Users>>() {
            @Override
            public void onChanged(@Nullable List<Users> users) {
                Log.d("TEST", ""+users.size());
                if(users.size() > 0){
                    Users body = users.get(0);

                    id = body.getId();
                    name = body.getName();
                    username = body.getName();
                    if(body.getImage() != null){
                        image = body.getImage();
                    } else {
                        image = "";
                    }
                    saveInstanceData(username, name, image, id);
                    inputDataToActivity();
                    Log.d("TEST", "get user data" + username + ", " + name + "," + id + "," + image);
                }
            }
        });
    }

    public static void saveInstanceData(String username, String name, String image, int id) {
        Model.getInstance().username = username;
        Model.getInstance().name= name;
        Model.getInstance().image = image;
        Model.getInstance().id = id;
        Log.d("TEST", "ini di function saveinstanceData" + name + "," + username + "," + id);

    }

    public void inputDataToActivity() {
        //put pic to navigation header
        Log.d("TEST", "ini di function activity" + name + "," + username + "," + id);
        if(!image.equals("")){
            ImageViewHeader.setImageBitmap(encodeStringToBitmap(image));
        } else {
            ImageViewHeader.setImageResource(R.mipmap.ic_launcher);
        }

        //put data in header
        txtUsername.setText(username);
        txtName.setText(name);
    }

    private Bitmap encodeStringToBitmap(String image) {
        byte[] data = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if(id == R.id.foto){
            takeSelfie();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void takeSelfie() {
        Intent takeSelfieIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takeSelfieIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takeSelfieIntent, TAKE_SELFIE_REQUEST);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap imageBitmap;

        if(requestCode == TAKE_SELFIE_REQUEST && resultCode == RESULT_OK){
            ByteArrayOutputStream bm = new ByteArrayOutputStream();
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            //set image bitmap
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bm);
            byte[] byteArray = bm.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            if(encoded != null){
                //input to database
                users.image = encoded;
                users.id = id;
                users.name = name;
                users.username = username;
                wvm.updateData(users);
                Model.getInstance().image = encoded;
            }
            Log.d("TEST" , data.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TEST", "onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TEST", "on Resume");
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
}
