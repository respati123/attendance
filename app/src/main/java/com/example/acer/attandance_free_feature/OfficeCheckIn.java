package com.example.acer.attandance_free_feature;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.attandance_free_feature.Services.CountDownService;
import com.example.acer.attandance_free_feature.db.entities.AbsensiOffice;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfficeCheckIn extends AppCompatActivity {


    private static final String CHANNEL_3_ID = "channel3";
    private MapView map = null;
    private Location locGPS;
    private LocationManager locationManager;
    private LocationManager locationManagerNetwork;
    private ArrayList<GeoPoint> points;
    private ArrayList<Double> distances;
    private double minDistance = -1;
    private double lat;
    private double lon;
    private double distance;
    private GeoPoint user;
    private GeoPoint office;
    private LocationListener locationListener;
    private IMapController mapController;
    private Marker userPos;
    private Marker officeMarker;
    private Button clockIn;
    private int userId = 1;

    private String test;
    private String encodedImage;

    private Polygon circle;

    static final int TAKE_SELFIE_REQUEST = 1;
    private WordViewModel wordViewModel;
    private AbsensiOffice insert;



    //for countdown
    //inner class
    MyReceiver myReceiver;
    IntentFilter intentFilter;
    private boolean isCheckIn;
    private Button btnCheckIn;
    private Button btnCheckOut;
    private TextView textTimer;
    private long mMillisLeft;
    private boolean isFirstGateCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context ctx = this.getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_office_check_in);

        //initialize
        btnCheckIn = findViewById(R.id.checkIn);
        btnCheckOut = findViewById(R.id.checkOut);
        textTimer = findViewById(R.id.Timer);
        createNotificationChannels();

//        overtime = (Chronometer) findViewById(R.id.chrometer);

        //Permission Check
        boolean fineLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean externalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(!fineLocPermission){
            finish();
        }

        if(!coarseLocPermission){
            finish();
        }

        if(!externalStoragePermission) {
            finish();
        }

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManagerNetwork = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                user = new GeoPoint(lat, lon);

                updateButton();

                userPos.setPosition(user);
                map.invalidate();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 25, locationListener);
        locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 25, locationListener);

        mapController = map.getController();
        mapController.setZoom(19.0);
        user = new GeoPoint(locGPS.getLatitude(), locGPS.getLongitude());

        office = new GeoPoint(-6.183329, 106.8142254);

        userPos = new Marker(map);
        userPos.setPosition(user);
        userPos.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(userPos);

        points = new <GeoPoint>ArrayList();
        points.add(user);
        points.add(office);
//        RoadManager rm = new OSRMRoadManager(this);
//        Road road = rm.getRoad(points);
//        Polyline roadOverlay = rm.buildRoadOverlay(road);
//        map.getOverlays().add(roadOverlay);

        Polygon circle = new Polygon();
        circle.setPoints(Polygon.pointsAsCircle(office, 100));
        circle.setFillColor(0x12121212);
        circle.setStrokeColor(Color.RED);
        circle.setStrokeWidth(2);
        map.getOverlays().add(circle);



        this.getWorkLocations();
        BoundingBox bb = map.getBoundingBox();

        CacheManager cm = new CacheManager(map);
        cm.downloadAreaAsync(this, bb, 9, 19);
//
//        MapEventsOverlay eo = new MapEventsOverlay(this, this);
//        map.getOverlays().add(0, eo);
        map.invalidate();
    }



//    private void startOvertime() {
//        overtime.setBase(SystemClock.elapsedRealtime());
//        overtime.start();
//        overtime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//            @Override
//            public void onChronometerTick(Chronometer chronometer) {
//                overtime = chronometer;
//                Log.d("TEST", ""+ (SystemClock.elapsedRealtime() - overtime.getBase()));
//            }
//        });
//        overTimeRunning = true;
//    }

    private void updateButton() {

        minDistance = -1;
        distance = user.distanceToAsDouble(office);


        if(isCheckIn) {
            Log.d("TEST", "checin is :" + true);
            btnCheckIn.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.VISIBLE);

            if(distance <= 100 && mMillisLeft == 0){
                btnCheckOut.setClickable(true);
                btnCheckOut.setAlpha(1f);
            } else {
                btnCheckOut.setClickable(false);
                btnCheckOut.setAlpha(0.5f);
            }
        } else {
            Log.d("TEST", "checin is :" + false);

            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.GONE);

            if(distance <= 100 || minDistance == -1){
                btnCheckIn.setClickable(true);
                btnCheckIn.setAlpha(1f);
                if(!isFirstGateCircle){
                    isFirstGateCircle = true;
                    sendNotification();
                }
            } else {
                btnCheckIn.setClickable(false);
                btnCheckIn.setAlpha(0.5f);
            }
        }
    }

    private void sendNotification() {

        Intent intentActivity = new Intent(this, OfficeCheckIn.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                .setContentTitle("ATTENDANCE")
                .setContentText("You can check in right now!")
                .setSmallIcon(R.drawable.ic_notif)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, notification);

    }


    private void getWorkLocations() {

        BoundingBox view = BoundingBox.fromGeoPoints(points);
        GeoPoint center = new GeoPoint(view.getCenterLatitude(), view.getCenterLongitude());
        mapController.setCenter(center);
        mapController.zoomToSpan(view.getLatitudeSpan(), view.getLongitudeSpan());
        map.invalidate();
    }



    public void OnResume(){
        super.onResume();
        map.onResume();
    }

    public void OnPause(){
        super.onPause();
        map.onPause();
        //locationManager.removeUpdates(locationListener);
    }

    public void checkIn(View view) {
//        CacheManager cm = new CacheManager(map);
//        cm.downloadAreaAsync(this, points, 5, 19 );

        if(user.distanceToAsDouble(office) <= 100){
            takeSelfie();
        }else{
            return;
        }
    }

    public void center(View view) {
        mapController.animateTo(user);
    }
//
//    @Override
//    public boolean singleTapConfirmedHelper(GeoPoint p) {
//        return false;
//    }
//
//    @Override
//    public boolean longPressHelper(GeoPoint p) {
//        user = p;
//        userPos.setPosition(user);
//        this.updateButton();
//        map.invalidate();
//        return false;
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_SELFIE_REQUEST && resultCode == RESULT_OK) {

            Intent intent = new Intent(this, CountDownService.class);
            startService(intent);

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            isCheckIn = true;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dtime = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            Log.i("Action", encodedImage);

            insert = new AbsensiOffice(userId, df.format(date), dtime.format(date), user.getLatitude(), user.getLongitude(), encodedImage, "Check In", "1");
            Log.d("TEST", dtime.format(date));
            //remove this
            wordViewModel.insert(insert);
            map.invalidate();


            inputPrefs(true);
            Log.d("TEST", "selesai upload");

        }
    }

    private void inputPrefs(boolean bool) {
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putBoolean("isCheckIn", bool);
        editor.apply();
    }

    public void takeSelfie(){
        Intent takeSelfieIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takeSelfieIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takeSelfieIntent, TAKE_SELFIE_REQUEST);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OfficeCheckIn.this, MainActivity.class));
    }

    public void checkOut(View view) {

        Toast.makeText(getApplicationContext(), "Check OUt ", Toast.LENGTH_SHORT).show();
        SimpleDateFormat dtime = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        Log.d("TEST", dtime.format(date));

        insert.setOut_time(String.valueOf(dtime.format(date)));
        insert.setCheckout_lat(user.getLatitude());
        insert.setCheckout_lon(user.getLongitude());

        wordViewModel.insert(insert);

        updateButton();
        updateCountDownText();

        inputPrefs(false);

        stopService(new Intent(this, CountDownService.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TEST", "STOPPED");
        Log.d("TEST", "checkin in STOPPED :" + isCheckIn );
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putBoolean("isCheckIn", isCheckIn);
        editor.putBoolean("isFirstGate", isFirstGateCircle);
        editor.apply();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean check = prefs.getBoolean("isCheckIn", false);
        isFirstGateCircle = prefs.getBoolean("isFirstGate", false);
        isCheckIn = check;
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("COUNT_DOWN");
        registerReceiver(myReceiver, intentFilter);
        updateCountDownText();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TEST", "SAVED");
        outState.putBoolean("isCheckIn", isCheckIn);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TEST", "RESTORE");
        isCheckIn = savedInstanceState.getBoolean("isCheckIn");
    }

    private void updateCountDownText() {

        if(mMillisLeft == 0 ){
           if(isCheckIn){
               textTimer.setText(R.string.check_out_text);
               textTimer.setTextSize(18f);
               textTimer.setTextColor(Color.RED);
               return;
           } else {
               textTimer.setText(R.string.check_in_text);
               textTimer.setTextSize(18f);
               textTimer.setTextColor(Color.GREEN);
               return;
           }
        }

        int hour = (int) (mMillisLeft / 1000) / 60 / 60;
        int minutes = (int) (mMillisLeft / 1000) / 60 % 60;
        int seconds = (int) (mMillisLeft / 1000) % 60;

        String settTextView = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, seconds);
        textTimer.setText(settTextView);
        textTimer.setTextColor(Color.BLACK);

    }

    ///class receiver

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            mMillisLeft = intent.getLongExtra("millistLeft",  0);
            Log.d("TEST", "receive :" + mMillisLeft);
            updateButton();
            updateCountDownText();

        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel3.setDescription("This is Channel 1");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel3);
        }
    }
}
