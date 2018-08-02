package com.example.acer.attandance_free_feature;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.acer.attandance_free_feature.adapter.ScheduleAdapterMain;
import com.example.acer.attandance_free_feature.data.Movie;
import com.example.acer.attandance_free_feature.data.MyDividerItemDecoration;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;
import com.vamsi.customcalendar.CustomCalendar;

import java.util.ArrayList;
import java.util.List;

public class ScheduleMainActivity extends AppCompatActivity {

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScheduleAdapterMain scheduleAdapterMain;
    private boolean isExpanded = true;
    private LinearLayout mLinearLayout, mLinearToolbar, mLinearSearch;
    private ImageView imgSearch, imgCalendar;
    private EditText edt_search;
    private Animation slide_down, slide_up;
    private Context context;
    private CustomCalendar customCalendar;
    private View inflaterCalendar;
    private WordViewModel wordViewModel;
    private List<Schedules> schedulesList;


    private int first = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        context = getApplicationContext();

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        scheduleAdapterMain = new ScheduleAdapterMain(this);

        //slide
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        // init element recycler
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mLinearToolbar = (LinearLayout) findViewById(R.id.linearToolbar);
        mLinearSearch = (LinearLayout) findViewById(R.id.linearSearch);


        //item in Calendar layout
        imgCalendar = (ImageView) findViewById(R.id.calendar);
        imgSearch = (ImageView) findViewById(R.id.search);
        edt_search = (EditText) findViewById(R.id.edt_search);

        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleMainActivity.this, CalendarActivity.class));
                finish();
            }
        });

        wordViewModel.getAllSchedule().observe(this, new Observer<List<Schedules>>() {
            @Override
            public void onChanged(@Nullable List<Schedules> schedules) {
                if(schedules != null){
                    Log.d("TEST", ""+schedules.size());
                    scheduleAdapterMain.setSchedule(schedules);
                    scheduleAdapterMain.notifyDataSetChanged();
                    schedulesList = schedules;
                }
            }
        });


        edt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (edt_search.getRight() - edt_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        Toast.makeText(getApplicationContext(), "bisa", Toast.LENGTH_SHORT).show();
                        mLinearToolbar.setVisibility(View.VISIBLE);
                        mLinearSearch.setVisibility(View.GONE);
                        mLinearSearch.startAnimation(slide_down);
                        return true;
                    }
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearToolbar.setVisibility(View.GONE);
                mLinearSearch.setVisibility(View.VISIBLE);
                mLinearSearch.startAnimation(slide_up);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayout.VERTICAL, 16));
        recyclerView.setAdapter(scheduleAdapterMain);

//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//
//            private LinearLayout linearLayout;
//            private RelativeLayout relativeLayout;
//
//            @Override
//            public void onClick(View view, int position) {
//                linearLayout = view.findViewById(R.id.linierlayout);
//                relativeLayout = view.findViewById(R.id.relative);
//
//                relativeLayout.setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);
//
//                ImageView imageView = view.findViewById(R.id.close);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                Movie movie = movieList.get(position);
//                Toast.makeText(getApplicationContext(), movie.getYears() +","+ movie.getGenre() + "," + movie.getTitle(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                Movie movie = movieList.get(position);
//                Toast.makeText(getApplicationContext(), movie.getYears() +","+ movie.getGenre() + "," + movie.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        }));

//        prepareMovieData();
    }

    private void filter(String s) {
        List<Schedules> filterList = new ArrayList<>();

        for(Schedules item: schedulesList){
            if(item.getName().toLowerCase().contains(s.toLowerCase())){
                filterList.add(item);
            }
        }

        scheduleAdapterMain.filterList(filterList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScheduleMainActivity.this, MainActivity.class));
        finish();
    }

    //    private void prepareMovieData() {
//
//        Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2015");
//        movieList.add(movie);
//
//        movie = new Movie("Inside Out", "Animation, Kids & Family", "2015");
//        movieList.add(movie);
//
//        movie = new Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
//        movieList.add(movie);
//
//        movie = new Movie("Shaun the Sheep", "Animation", "2015");
//        movieList.add(movie);
//
//        movie = new Movie("The Martian", "Science Fiction & Fantasy", "2015");
//        movieList.add(movie);
//
//        movie = new Movie("Mission: Impossible Rogue Nation", "Action", "2015");
//        movieList.add(movie);
//
//        movie = new Movie("Up", "Animation", "2009");
//        movieList.add(movie);
//
//        movie = new Movie("Star Trek", "Science Fiction", "2009");
//        movieList.add(movie);
//
//        movie = new Movie("The LEGO Movie", "Animation", "2014");
//        movieList.add(movie);
//
//        movie = new Movie("Iron Man", "Action & Adventure", "2008");
//        movieList.add(movie);
//
//        movie = new Movie("Aliens", "Science Fiction", "1986");
//        movieList.add(movie);
//
//        movie = new Movie("Chicken Run", "Animation", "2000");
//        movieList.add(movie);
//
//        movie = new Movie("Back to the Future", "Science Fiction", "1985");
//        movieList.add(movie);
//
//        movie = new Movie("Raiders of the Lost Ark", "Action & Adventure", "1981");
//        movieList.add(movie);
//
//        movie = new Movie("Goldfinger", "Action & Adventure", "1965");
//        movieList.add(movie);
//
//        movie = new Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
//        movieList.add(movie);
//
//        movieAdapter.notifyDataSetChanged();
//    }
}
