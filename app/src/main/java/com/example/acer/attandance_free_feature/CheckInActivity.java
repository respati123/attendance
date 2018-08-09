package com.example.acer.attandance_free_feature;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.*;
import java.util.List;
import java.util.SimpleTimeZone;

public class CheckInActivity extends AppCompatActivity implements MapEventsReceiver{

    private MapView map = null;
    private Location locGPS;
    private LocationManager locationManager;
    private ArrayList<GeoPoint> points;
    private ArrayList<Double> distances;
    private double minDistance = -1;
    private double lat;
    private double lon;
    private double distance;
    private GeoPoint user;
    private LocationListener locationListener;
    private IMapController mapController;
    private Marker userPos;
    private Button clockIn;
    private int userId = 1;
    private String test;
    private String encodedImage;

    static final int TAKE_SELFIE_REQUEST = 1;
    private WordViewModel wordViewModel;

    private RecyclerView mRecyclerView;
    private ScheduleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context ctx = this.getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_check_in);

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

        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ScheduleViewAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        wordViewModel.getmGetDataScheduleToday().observe(this, new Observer<List<Schedules>>() {
            @Override
            public void onChanged(@Nullable List<Schedules> schedules) {

                mAdapter.setSchedule(schedules);
                mAdapter.notifyDataSetChanged();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        clockIn = findViewById(R.id.clockIn);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationListener = new LocationListener() {
            private Button clockIn = findViewById(R.id.clockIn);

            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                user = new GeoPoint(lat, lon);

                updateButton();

//                Log.i("Action", "Clock In");
//                DateFormat df = new SimpleDateFormat();
//                Date date = new Date();

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

        mapController = map.getController();
        mapController.setZoom(19.0);
        user = new GeoPoint(locGPS.getLatitude(), locGPS.getLongitude());
        mapController.setCenter(user);

        userPos = new Marker(map);
        userPos.setPosition(user);
        userPos.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(userPos);

        points = new <GeoPoint>ArrayList();
        points.add(user);
        //points.add(end);

        this.getWorkLocations();
//        RoadManager rm = new OSRMRoadManager(this);
//        Road road = rm.getRoad(points);
//        Polyline roadOverlay = rm.buildRoadOverlay(road);
//        map.getOverlays().add(roadOverlay);

//        Polygon circle = new Polygon();
//        circle.setPoints(Polygon.pointsAsCircle(end, 100));
//        circle.setFillColor(0x12121212);
//        circle.setStrokeColor(Color.RED);
//        circle.setStrokeWidth(2);
//        map.getOverlays().add(circle);
        BoundingBox bb = map.getBoundingBox();
        CacheManager cm = new CacheManager(map);
        cm.downloadAreaAsync(this, bb, 9, 19);

        MapEventsOverlay eo = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, eo);

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
    }

    private void getWorkLocations(){
//        String url = "http://10.0.2.2:3000/locations";
//        JsonArrayRequest joReq = new JsonArrayRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.v("TEST", response.toString());
//                        try {
//                            int count = response.length();
//                            for(int i = 0; i < count; i++){
//
//                                JSONObject temp = response.getJSONObject(i);
//                                String name = temp.getString("name");
//                                Double lat = temp.getDouble("lat");
//                                Double lng = temp.getDouble("long");
//                                Log.i("TEST", name);
//                                GeoPoint loc = new GeoPoint(lat, lng);
//                                points.add(loc);
//                                Log.i("TEST", points.toString());
//
//                                Polygon circle = new Polygon();
//                                circle.setPoints(Polygon.pointsAsCircle(loc, 100));
//                                circle.setFillColor(0x12121212);
//                                circle.setStrokeColor(Color.RED);
//                                circle.setStrokeWidth(2);
//                                map.getOverlays().add(circle);
//
//                                Marker locMark = new Marker(map);
//                                locMark.setPosition(loc);
//                                locMark.setId(name);
//                                locMark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                                map.getOverlays().add(locMark);
//
//                                map.invalidate();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        BoundingBox view = BoundingBox.fromGeoPoints(points);
//                        GeoPoint viewCenter = new GeoPoint(view.getCenterLatitude(), view.getCenterLongitude());
////        Log.i("Test", String.valueOf(view.getLatitudeSpan()));
////        Log.i("Test", String.valueOf(view.getLongitudeSpan()));
//                        Log.i("Test", user.toDoubleString());
//                        Log.i("Test", viewCenter.toDoubleString());
//                        mapController.setCenter(viewCenter);
//                        mapController.zoomToSpan(view.getLatitudeSpan(), view.getLongitudeSpan());
//                        map.invalidate();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast toast = Toast.makeText(NewCheckInActivity.this, "Error", Toast.LENGTH_LONG);
//                        toast.show();
//                        if(error == null){
//                            Log.v("VOLLEY RESPONSE", "empty");
//                            return;
//                        }
//                        Log.v("VOLLEY RESPONSE", error.getMessage());
//
//                    }
//                });
//        RequestQueue q = Volley.newRequestQueue(this);
//        q.add(joReq);
    }

    public void checkIn(View view) {
//        CacheManager cm = new CacheManager(map);
//        cm.downloadAreaAsync(this, points, 5, 19 );

        if(mAdapter.checkChosen()){
            takeSelfie();
        }else{
            return;
        }
    }

    public void center(View view) {
        mapController.animateTo(user);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        user = p;
        userPos.setPosition(user);
        this.updateButton();
        map.invalidate();
        return false;
    }

    private Absensi insert;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_SELFIE_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            Log.i("Action", "Clock In");
            String url = "http://10.0.2.2:3000/clockIns";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dtime = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();


            Log.i("Action", encodedImage);

            Schedules chosen = mAdapter.getChosenSchedule();

            insert = new Absensi(userId, df.format(date), dtime.format(date), locGPS.getLatitude(), locGPS.getLongitude(), encodedImage, "Check In", 1);
            Log.d("TEST", dtime.format(date));
            insert.setId_schedules(chosen.getId());
            //remove this
            wordViewModel.insert(insert);

            Button checkOut = findViewById(R.id.checkOut);
            checkOut.setVisibility(View.VISIBLE);

            Button checkIn = findViewById(R.id.clockIn);
            checkIn.setVisibility(View.GONE);

            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void takeSelfie(){
        Intent takeSelfieIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takeSelfieIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takeSelfieIntent, TAKE_SELFIE_REQUEST);
        }
    }

    public void reschedule(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        Schedules currSchedule = mAdapter.getChosenSchedule();
        intent.putExtra("date", currSchedule.getDate());
        intent.putExtra("desc", currSchedule.getDesc());
        intent.putExtra("user_id", currSchedule.getId_users());
        intent.putExtra("id", currSchedule.getId());
        intent.putExtra("job", currSchedule.getJob());
        intent.putExtra("meet", currSchedule.getMeet());
        intent.putExtra("name", currSchedule.getName());
        intent.putExtra("service", currSchedule.getService());
        intent.putExtra("time", currSchedule.getTime());
        intent.putExtra("edit", true);
        //start activity
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CheckInActivity.this, MainActivity.class));
        finish();
    }

    public void checkOut(View view) {
        SimpleDateFormat dtime = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        insert.setTime(dtime.format(date));
        wordViewModel.insert(insert);

        Schedules schedules = mAdapter.getChosenSchedule();
        schedules.setChecked_in(true);
        wordViewModel.update(schedules);

        Button checkOut = findViewById(R.id.checkOut);
        checkOut.setVisibility(View.GONE);

        Button checkIn = findViewById(R.id.clockIn);
        checkIn.setVisibility(View.VISIBLE);

        mAdapter.notifyDataSetChanged();

        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
