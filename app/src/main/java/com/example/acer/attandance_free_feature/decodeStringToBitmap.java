package com.example.acer.attandance_free_feature;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

class decodeStringToBitmap {

    private String image;

    public decodeStringToBitmap(String image) {
        this.image = image;
    }

    public Bitmap decode(){

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
