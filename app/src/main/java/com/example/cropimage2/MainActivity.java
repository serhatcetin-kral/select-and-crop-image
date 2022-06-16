package com.example.cropimage2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView ProfilResmi;

    private Button resimekle;
    private static int GaleriSecme=1;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

ProfilResmi=findViewById(R.id.profil_resmi);
resimekle=findViewById(R.id.resimekle);



//profil resmin uzerine tiklariz
ProfilResmi.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     //   bu kodlar resim secmek icin ama cropla olmuyo
        Intent galeriIntent=new Intent();
        galeriIntent.setAction(Intent.ACTION_GET_CONTENT);
        galeriIntent.setType("image/*");
        startActivityForResult(galeriIntent,GaleriSecme);
//
       //   bu kod resim secmek ve crop icin yukaridakiler veya asagidaki birini kullanabilirin ikisine gerek yok
     CropImage.startPickImageActivity(MainActivity.this);



    }
});

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//////////////////
        //bu aradaki kisim profil resmi yapmak icin ama data base eklemiyo yada yapamadim
if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

    //imageUri=data.getData();// asagidaki kodu yada bu kodu kullanabilirsin
   Uri imageUri=CropImage.getPickImageResultUri(this,data);
    startCrop(imageUri);
    ProfilResmi.setImageURI(imageUri);
}
//////////////

//        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK)
//        {
//
//             Uri uri=CropImage.getPickImageResultUri(this,data);
//if(CropImage.isReadExternalStoragePermissionsRequired(this,imageUri))
//{
//  uri=imageUri;
//requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);
//
//
//}else {
//
//           startCrop(imageUri);
//}
//
//        }
//        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
//        {
//            CropImage.ActivityResult result=CropImage.getActivityResult(data);
//            if(requestCode==RESULT_OK)
//            {
//             ProfilResmi.setImageURI(result.getUri());
//             uploadpicture();
//                Toast.makeText(this, "profil picture update is successfully", Toast.LENGTH_LONG).show();
//            }
//        }
    }

    private void uploadpicture() {
  final ProgressDialog pd=new ProgressDialog(this);
  pd.setTitle("uploading picture");
  pd.show();
  final String randomKey= UUID.randomUUID().toString();
        StorageReference reversesRef = storageReference.child("images/"+randomKey);

reversesRef.putFile(imageUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content),"image upload",Snackbar.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"faild to upload",Toast.LENGTH_LONG).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progressPercent=(100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("pertences:"+(int)progressPercent+"%");

            }
        });

    }

    private void startCrop(Uri imageuri) {
    CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this);


    }
}