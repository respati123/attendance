package com.example.acer.attandance_free_feature;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.acer.attandance_free_feature.data.Model;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.models.WordViewModel;

import java.io.ByteArrayOutputStream;

import butterknife.BindString;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG = "TEST";
    ImageView btnBack, btnCamera;
    Context context;
    WordViewModel wvm;

    @BindView(R.id.image_profile)
    CircleImageView imageProfile;

    @BindView(R.id.name_profile)
    TextView nameProfile;

    @BindView(R.id.username_profile)
    TextView usernameProfile;

    private String username, name, image;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //find
        usernameProfile = findViewById(R.id.username_profile);
        nameProfile = findViewById(R.id.name_profile);
        imageProfile = (CircleImageView) findViewById(R.id.image_profile);


        context = getApplicationContext();
        wvm = ViewModelProviders.of(this).get(WordViewModel.class);

        btnBack = (ImageView) findViewById(R.id.back_profile);
        btnCamera = (ImageView) findViewById(R.id.buttonCamera);

        btnBack.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

    }

    private void setDataInActivity() {
        usernameProfile.setText(username);
        nameProfile.setText(name);
        if(image != null){
            Log.d(LOG, "image ada");
            decodeStringToBitmap decodeStringToBitmap = new decodeStringToBitmap(image);
            Bitmap imageBitmap = decodeStringToBitmap.decode();
            imageProfile.setImageBitmap(imageBitmap);
        } else {
            Log.d(LOG, "image tidak ada");
            imageProfile.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.back_profile:
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.buttonCamera:
                Intent IntentForSelfie = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(IntentForSelfie.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(IntentForSelfie, 1);
                }
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Bitmap bitmap;
        if(requestCode == 1 && resultCode == RESULT_OK){


            ByteArrayOutputStream bm = new ByteArrayOutputStream();
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bm);
            byte[] bytes = bm.toByteArray();

            String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
            if(encoded != null){
                boolean result = updateDataImage(encoded);
                if(result){
                    //initiate
                    Model.getInstance().image = encoded;
                    username = Model.getInstance().username;
                    name = Model.getInstance().name;
                    id = Model.getInstance().id;
                    image = Model.getInstance().image;

                    setDataInActivity();

                    Log.d("TEST", Model.getInstance().username + "," + Model.getInstance().name + "," + Model.getInstance().image + "," + Model.getInstance().id);
                }
            }
        }
    }

    private boolean updateDataImage(String encoded) {
        Users users = new Users();
        users.username = Model.getInstance().username;
        users.name = Model.getInstance().name;
        users.id = Model.getInstance().id;
        users.image = encoded;
        wvm.updateData(users);

        return true;

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
