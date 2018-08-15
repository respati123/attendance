package com.example.acer.attandance_free_feature;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.view.View;
import android.widget.Button;

import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiOffice;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfficeCheckIn extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context ctx = this.getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_office_check_in);

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

        clockIn = findViewById(R.id.clockIn);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManagerNetwork = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationListener = new LocationListener() {
            private Button clockIn = findViewById(R.id.clockIn);

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
        mapController.setCenter(user);

        office = new GeoPoint(locGPS.getLatitude(), locGPS.getLongitude());

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

        BoundingBox bb = map.getBoundingBox();
        CacheManager cm = new CacheManager(map);
        cm.downloadAreaAsync(this, bb, 9, 19);
//
//        MapEventsOverlay eo = new MapEventsOverlay(this, this);
//        map.getOverlays().add(0, eo);

        this.updateButton();

        map.invalidate();
    }

    public void updateButton(){
        minDistance = -1;
        for(int i = 1; i < points.size(); i++){
            distance = user.distanceToAsDouble(points.get(i));
            if(minDistance >= distance || minDistance == -1){
                minDistance = distance;
            }
        }

        if(minDistance <= 100){
            clockIn.setClickable(true);
            clockIn.setAlpha(1f);
        }else{
            clockIn.setClickable(false);
            clockIn.setAlpha(0.5f);
        }

        Log.i("TEST", "distance: "+String.valueOf(minDistance));
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

    private AbsensiOffice insert;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_SELFIE_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

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

            Button checkOut = findViewById(R.id.checkOut);
            checkOut.setVisibility(View.VISIBLE);

            Button checkIn = findViewById(R.id.clockIn);
            checkIn.setVisibility(View.GONE);
        }
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
        //startActivity(new Intent(CheckInActivity.this, MainActivity.class));
        finish();
    }

    public void checkOut(View view) {
        SimpleDateFormat dtime = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        if(user.distanceToAsDouble(office) <= 100){
            insert.setOut_time(dtime.format(date));
            insert.setCheckout_lat(user.getLatitude());
            insert.setCheckout_lon(user.getLongitude());

            wordViewModel.insert(insert);

            Button checkOut = findViewById(R.id.checkOut);
            checkOut.setVisibility(View.GONE);

            Button checkIn = findViewById(R.id.clockIn);
            checkIn.setVisibility(View.VISIBLE);
        }

    }

}
