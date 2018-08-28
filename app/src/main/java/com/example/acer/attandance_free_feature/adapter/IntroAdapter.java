package com.example.acer.attandance_free_feature.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.acer.attandance_free_feature.R;

public class IntroAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public IntroAdapter(Context context){
        this.context = context;
    }

    private int[] slide_images = {
            R.mipmap.icon_logo_foreground,
            R.mipmap.ic_report_foreground,
            R.mipmap.icon_checkin_main_foreground


    };

    private String[] slide_desc = {
            "``Aplikasi mendukung untuk HR``",
            "``aplikasi untuk Reporting``",
            "``Aplikasi untuk Check-in``"
    };

    private String[] slide_heading = {
            "HR",
            "Report",
            "Check-in"
    };

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.imgSlide);
        TextView slideHeading = (TextView) view.findViewById(R.id.heading);
        TextView slideDesc = (TextView) view.findViewById(R.id.description);

        slideImage.setImageResource(slide_images[position]);
        slideHeading.setText(slide_heading[position]);
        slideDesc.setText(slide_desc[position]);


        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
