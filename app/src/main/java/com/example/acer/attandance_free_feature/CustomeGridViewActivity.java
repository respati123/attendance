package com.example.acer.attandance_free_feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomeGridViewActivity extends BaseAdapter {

    private Context mContext;
    private final String[] gridviewString;
    private final int[] gridViewImageId;

    public CustomeGridViewActivity(Context mContext, String[] gridviewString, int[] gridViewImageId) {
        this.mContext = mContext;
        this.gridviewString = gridviewString;
        this.gridViewImageId = gridViewImageId;
    }



    @Override
    public int getCount() {
        return gridviewString.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater lf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            gridViewAndroid = new View(mContext);
            gridViewAndroid = lf.inflate(R.layout.gridview_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);

            textViewAndroid.setText(gridviewString[position]);
            imageViewAndroid.setImageResource(gridViewImageId[position]);

        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}
