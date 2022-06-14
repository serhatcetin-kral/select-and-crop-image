package com.example.cropimage2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView ProfilResmi;

    private Button resimekle;
    private static int GaleriSecme=1;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

ProfilResmi=findViewById(R.id.profil_resmi);
resimekle=findViewById(R.id.resimekle);



//profil resmin uzerine tiklariz
ProfilResmi.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     //   bu kodlar resim secmek icin ama cropla olmuyo
//        Intent galeriIntent=new Intent();
//        galeriIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galeriIntent.setType("image/*");
//        startActivityForResult(galeriIntent,GaleriSecme);
//
       //   bu kod resim secmek ve crop icin
      CropImage.startPickImageActivity(MainActivity.this);



    }
});

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     //   Uri ResimUri=data.getData();
//        CropImage.activity()
//                .setDuidelines(CropImage.Guidelines.ON)
//                .setAspectRatio(1,1)
//                .start(this);

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {

             Uri imageuri=CropImage.getPickImageResultUri(this,data);
if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri))
{
uri=imageuri;
requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);
}else {

           startCrop(imageuri);
}

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(requestCode==RESULT_OK)
            {
             ProfilResmi.setImageURI(result.getUri());
                Toast.makeText(this, "profil picture update is successfully", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCrop(Uri imageuri) {
    CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this);


    }
}