package com.getmate.demo181201.createEvent;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.CurrentUserData;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import id.zelory.compressor.Compressor;

public class AddImageActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private static  final  int GALLARY_REQUEST=5;
    final private int REQUEST_FOR_STORAGE= 111;
    TextView addImageText;
    Button next;
    Profile currentUserProfile = new Profile();
    Boolean iIUS = true;
    String downloadUri= null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);


    currentUserProfile = CurrentUserData.getInstance().getCurrent();

        addImageText = findViewById(R.id.add_image_text);
        imageButton = findViewById(R.id.add_image_button);
        next = findViewById(R.id.next);

        progressDialog = new ProgressDialog(AddImageActivity.this);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ispermissionGiven = checkPermissionForGallary();

                if (ispermissionGiven){
                    Intent gallaryintent = new Intent(Intent.ACTION_GET_CONTENT);
                    gallaryintent.setType("Image/*");
                    startActivityForResult(gallaryintent,GALLARY_REQUEST);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (iIUS){
                Intent i = new Intent( AddImageActivity.this,AddOtherOrganisers.class);

                i.putExtra("description",getIntent().getStringExtra("description"));
                i.putExtra("title",getIntent().getStringExtra("title"));
                i.putExtra("from",getIntent().getLongExtra("from",0));
                i.putExtra("to",getIntent().getLongExtra("to",0));
                i.putStringArrayListExtra("interestB",getIntent().getStringArrayListExtra("interestB"));
                i.putStringArrayListExtra("interestI",getIntent().getStringArrayListExtra("interestI"));
                i.putStringArrayListExtra("interestE",getIntent().getStringArrayListExtra("interestE"));
                i.putStringArrayListExtra("AllParentInterests",getIntent().
                        getStringArrayListExtra("AllParentInterests"));
                i.putExtra("lat",getIntent().getDoubleExtra("lat",0.00));
                i.putExtra("lon",getIntent().getDoubleExtra("lon",0.00));
                i.putExtra("address",getIntent().getStringExtra("address"));
                i.putExtra("city",getIntent().getStringExtra("city"));
                i.putExtra("imageUri",downloadUri);
                startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(),"Photo Not Uploaded",Toast.LENGTH_LONG).show();
            }
            }
        });


    }


    private boolean checkPermissionForGallary(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_FOR_STORAGE);


                return false;
                //if request is not available then here
            }
            else {
                //if permission is already available then
                return true;
            }

        }
        //if Build.Version<23//find out what to do then?
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLARY_REQUEST){
            Log.i("KaunHuMein","arrived on to gallary");

            if (resultCode == Activity.RESULT_OK){
                Uri imageURI = data.getData();
                if(imageURI!=null){
                    CropImage.activity(imageURI).setGuidelines
                            (CropImageView.Guidelines.ON).
                            start(this);
                }

            }


        }

        else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            Log.i("KaunHuMein","arrived from image crop activity");

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resultUri = result.getUri();
                if (resultUri!=null){
                    File file = new File(resultUri.getPath());
                    File compressedFile= null;
                    try {
                        compressedFile = new Compressor(this).compressToFile(file);
                        Uri compressedFileUri = Uri.fromFile(compressedFile);
                        UploadImage(compressedFileUri);
                        imageButton.setImageURI(compressedFileUri);
                        addImageText.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("EditProfile","image compression error",e.getCause());
                    }

                }
            }


            if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception exception = result.getError();
            }

        }


    }

    private void UploadImage(Uri uri){
            progressDialog.show();
            progressDialog.setMessage("Photo Uploading Please Wait");
        Calendar c = Calendar.getInstance();

        final StorageReference ref = FirebaseStorage.getInstance().getReference().
                child("events/"+currentUserProfile.getHandle()+c.getTimeInMillis());
        //TODO:change this storageRef
        UploadTask uploadTask = ref.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                Log.i("TAG","download url"+ref.getDownloadUrl());

                Toast.makeText(getApplicationContext(),"upload success",Toast.LENGTH_SHORT).show();

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri dU = task.getResult();
                    downloadUri = dU.toString();
                    iIUS = true;
                    Picasso.get().load(dU.toString()).into(imageButton);
                    progressDialog.dismiss();

                } else {

                }
            }
        });


    }


}
