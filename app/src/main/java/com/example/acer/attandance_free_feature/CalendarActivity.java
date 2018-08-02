package com.example.acer.attandance_free_feature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vamsi.customcalendar.CustomCalendar;
import com.vamsi.customcalendar.Helpers.Badge;
import com.vamsi.customcalendar.Helpers.CalenderDate;
import com.vamsi.customcalendar.Helpers.ClickInterface;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private CustomCalendar customCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        customCalendar = (CustomCalendar) findViewById(R.id.view);
        customCalendar.setFullScreenWidth(true);

        List<Badge> badgeList = new ArrayList<>();

        Badge badge = new Badge();
        badgeList.add(new Badge(1, 1, 8));
        badgeList.add(new Badge(1, 2, 8));
        badgeList.add(new Badge(1, 3, 8));

        customCalendar.setBadgeDateList(badgeList);

        customCalendar.setOnClickDate(new ClickInterface() {
            @Override
            public void setDateClicked(CalenderDate date) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CalendarActivity.this, ScheduleMainActivity.class));
        fileList();
    }
}
