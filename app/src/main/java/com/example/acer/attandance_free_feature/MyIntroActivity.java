package com.example.acer.attandance_free_feature;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acer.attandance_free_feature.adapter.IntroAdapter;

public class MyIntroActivity extends AppCompatActivity {

    private Context context;
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private IntroAdapter introAdapter;
    private TextView[] mDots;
    private int mCurrentItem;
    private Button btnBack, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intro);

        context = this;

        boolean isFirstTime = MyPreferences.isFirst(this);
        if(!isFirstTime){
            Intent intent = new Intent(context, RegisterUsersActivity.class);
            startActivity(intent);
            finish();
        }

        mSlideViewPager = (ViewPager) findViewById(R.id.view_pager);
        mDotLayout = (LinearLayout) findViewById(R.id.linear_layout);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);


        introAdapter = new IntroAdapter(this);

        mSlideViewPager.setAdapter(introAdapter);


        addSeparator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        btnBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentItem - 1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentItem + 1);
            }
        });

    }

    private void addSeparator(int position) {

        mDots = new TextView[3];

        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(android.R.color.white));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addSeparator(position);
            mCurrentItem = position;

            if(position == 0){
                btnBack.setEnabled(false);
                btnNext.setEnabled(true);
                btnBack.setVisibility(View.INVISIBLE);

                btnNext.setText("Next");
                btnBack.setText("");
            } else if (position == mDots.length - 1){

                btnNext.setEnabled(true);
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);

                btnNext.setText("Finish");
                btnBack.setText("Back");

               btnNext.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        Intent intent = new Intent(context, RegisterUsersActivity.class);
                        startActivity(intent);
                        finish();
                   }
               });
            } else {

                btnBack.setEnabled(true);
                btnNext.setEnabled(true);

                btnBack.setVisibility(View.VISIBLE);
                btnBack.setText("Back");
                btnNext.setText("Next");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
