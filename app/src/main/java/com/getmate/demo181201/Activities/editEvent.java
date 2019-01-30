package com.getmate.demo181201.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getmate.demo181201.InterestSelection.InterestSelectionActivity;
import com.getmate.demo181201.MainActivity;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.getmate.demo181201.getEventLocation;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class editEvent extends AppCompatActivity {
    final private int REQUEST_FOR_STORAGE= 111;
    private static  final  int GALLARY_REQUEST=5;
    private static final int MAP_ACTIVITY = 2;
    private FirebaseFirestore database;
    final int INTEREST_SELECTION = 1;
    int flag =1;
    LinearLayout mainLin;
    FirebaseFirestore db;
    FlexboxLayout flexboxLayout;
 Event newEvent =  new Event(); //get an instance of this class
    ArrayList<String> interestB = new ArrayList<>();
    ArrayList<String> interestI = new ArrayList<>();
    ArrayList<String> interestE = new ArrayList<>();
    ArrayList<Event.Organisers> organisers = new ArrayList<>();
    private ImageButton imageButton;


    MaterialEditText title_et,venue_et,link_et,description_et;


    RadioButton email_rb,phone_rb,fb_rb,linkedin_rb;
    Button map_button,interest_button, date_button,done,time_button;
    int mYear,mMonth,mDay,mHour,mMinute;
    Long date;
    int eventDate,eventMonth,eventYear;
    Calendar d_and_t = Calendar.getInstance();
    //create a new Linear Layout
    LinearLayout[] linearLayouts = new LinearLayout[3];
    EditText[] nameOrgs = new EditText[3];
    EditText[] emailOrgs = new EditText[3];
    EditText[] phoneOrgs = new EditText[3];
    FirebaseAuth mAuth;
    StorageReference storageReference;
    Profile currentUserProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        findViewsById();
        database = FirebaseFirestore.getInstance();
        Button orgButton = findViewById(R.id.organisersButton);
        mainLin = findViewById(R.id.orgMainLinLay);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        boolean isReached = false;


        SharedPreferences mPrefs = editEvent.this.getSharedPreferences("currentUserData",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("currentUserDataInString",null);
        currentUserProfile = gson.fromJson(json,Profile.class);



        for (int i=0;i<3;i++){
            linearLayouts[i] = new LinearLayout(this);
            linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
            nameOrgs[i] = new EditText(this);
            emailOrgs[i] = new EditText(this);
            phoneOrgs[i]= new EditText(this);
            nameOrgs[i].setHint("Name");
            emailOrgs[i].setHint("email");
            phoneOrgs[i].setHint("phone Number");

            nameOrgs[i].setId(EditText.generateViewId());
            linearLayouts[i].addView(nameOrgs[i]);
            linearLayouts[i].addView(emailOrgs[i]);
            linearLayouts[i].addView(phoneOrgs[i]);
            }

           mainLin.addView(linearLayouts[0]);

        orgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag<3){
                    mainLin.addView(linearLayouts[flag]);
                    flag++;
                }
                else {
                    Toast.makeText(getApplicationContext(),"Only three organisers allowed",Toast.LENGTH_LONG).show();
                }
                }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()){
                    //This code is repeating on double clicking Done !
                    for (int i=0;i<flag;i++){
                        Event.Organisers organisers1 = new Event.Organisers(nameOrgs[i].getText().toString().trim(),
                                emailOrgs[i].getText().toString().trim(),
                                phoneOrgs[i].getText().toString().trim());
                        organisers.add(organisers1);

                    }
                    //organisers.add(new Event.Organisers(currentUSerProfile.getName(),
                     //       currentUSerProfile.getEmail(),currentUSerProfile.getPhoneNo()));
                    newEvent.setOrganisers(organisers);

                    //todo: FIREBASE QUERRY HERE

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    newEvent.setCreatorName(currentUser.getDisplayName());
                    newEvent.setCreatorFirebaseId(currentUser.getUid());
                    newEvent.setCreatorProfilePic(currentUser.getPhotoUrl().toString());
                    newEvent.setCreatorEmail(currentUser.getEmail());
                    newEvent.setCreatorHandle(currentUserProfile.getHandle());
                    newEvent.setCreatorPhoneNo(currentUserProfile.getPhoneNo());

                    db.collection("events").add(newEvent).
                            addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()){
                                    Log.i("KaunH","the generated id of document is" + task.getResult().getId());
                                    Intent i = new Intent(editEvent.this,MainActivity.class);
                                    startActivity(i);

                                }
                        }
                    });
                    Gson gson = new Gson();
                    String json = gson.toJson(newEvent);
                    Log.i("kaunHuein",json);

                }
                else {
                    Toast.makeText(getApplicationContext(),"form Invaid bitch",Toast.LENGTH_SHORT).show();
                }
                }
        });

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),getEventLocation.class);
                startActivityForResult(i,MAP_ACTIVITY);
            }
        });

        interest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editEvent.this, InterestSelectionActivity.class);
                intent.putExtra("fromEditEvent",1);
                startActivityForResult(intent, INTEREST_SELECTION);
            }
        });


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


        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                //launch date picker dialogue

                DatePickerDialog dpd = new DatePickerDialog(editEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                d_and_t.set(year,month,dayOfMonth);
                                eventDate = dayOfMonth;
                                eventMonth = month;
                                eventYear = year;
                            }

                        },mYear,mMonth,mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });


        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mHour =c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(editEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newC = Calendar.getInstance();
                        newC.set(eventYear,eventMonth,eventDate,hourOfDay,minute);

                        newEvent.setTime(String.valueOf(newC.getTimeInMillis()));
                    }
                },mHour,mMinute,true);
                timePickerDialog.show();
            }
        });



    }
    private void findViewsById() {
        title_et = findViewById(R.id.title_ee);
        venue_et = findViewById(R.id.venue_ee);
        link_et = findViewById(R.id.link_ee);
        description_et= findViewById(R.id.description_ee);

        map_button = findViewById(R.id.show_on_map_ee);
        date_button=findViewById(R.id.date_ee);
        time_button = findViewById(R.id.time_ee);
        interest_button=findViewById(R.id.interest_ee);
        done=findViewById(R.id.done_ee);
        imageButton = findViewById(R.id.add_image_button);
        flexboxLayout = findViewById(R.id.tagsView);
    }



    public boolean isFormValid(){
        boolean isValid = true;
        newEvent.setTitle(title_et.getText().toString().trim());
        if(newEvent.getTitle()==null){
            isValid = false;
            title_et.setError("SRequired");
        }
        newEvent.setDescription(description_et.getText().toString().trim());
        if(newEvent.getDescription()==null){
            isValid= false;
            description_et.setError("Required");
        }
        newEvent.setAddress(venue_et.getText().toString().trim());
        if(newEvent.getAddress()==null){
            isValid = false;
            venue_et.setError("Required");
        }
        if (newEvent.getTags()== null){
            isValid = false;
            Toast.makeText(getApplicationContext(),"Set Interest Tags",Toast.LENGTH_SHORT).show();
        }
        if(newEvent.getLat()==0 && newEvent.getLon()==0){
            isValid = false;
            Toast.makeText(getApplicationContext(),"Location is not Set",Toast.LENGTH_SHORT).show();
        }
        if (newEvent.getTime()==null){
            isValid = false;
            Toast.makeText(getApplicationContext(),"Time not set",Toast.LENGTH_SHORT).show();
            }

        return isValid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==INTEREST_SELECTION && resultCode==Activity.RESULT_OK){
            ArrayList<String> tags = new ArrayList<>();
            interestB = data.getStringArrayListExtra("interestB");
            interestI = data.getStringArrayListExtra("interestI");
            interestE = data.getStringArrayListExtra("interestE");
            //use this data



            if (interestB!=null){
                tags.addAll(interestB);

            }
            if (interestE!=null){
                tags.addAll(interestE);

            }
            if (interestI!=null){
                tags.addAll(interestI);

            }
            newEvent.setTags(tags);

            flexboxLayout.setVisibility(View.VISIBLE);
            int  tagsCount =tags.size() ;//... integer number of textviews
            TextView[] tages= new TextView[tagsCount];//create dynamic textviewsarray
            LinearLayout.LayoutParams layoutParams = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //loop to customize the text view and add it to flex box
            for (int i = 0; i < tagsCount; i++) {
                tages[i] = new TextView(editEvent.this);
                GradientDrawable gD = new GradientDrawable();
                int strokeWidth = 5;
                int strokeColor = getResources().getColor(R.color.grey);

                gD.setStroke(strokeWidth, strokeColor);
                gD.setCornerRadius(15);
                gD.setShape(GradientDrawable.RECTANGLE);

                tages[i].setBackground(gD);
                tages[i].setText(tags.get(i));
                layoutParams.setMargins(10, 5, 10, 5);
                tages[i].setPadding(17, 15, 17, 15);
                flexboxLayout.addView(tages[i], layoutParams);
            }





        }
        else if (requestCode==MAP_ACTIVITY && resultCode==Activity.RESULT_OK){
            if (data!=null){
                Log.i("KaunINResult",data.toString());
                LatLng lng = new LatLng(data.getDoubleExtra("lat",0),
                        data.getDoubleExtra("lon",0));
                Log.i("KaunHa"," lol"+lng.latitude+lng.longitude);
                newEvent.setLat(lng.latitude);
                newEvent.setLon(lng.longitude);

                Geocoder geocoder = new Geocoder(editEvent.this,Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lng.latitude,
                            lng.longitude,1);
                    if(addresses.size()>0){
                        String cityName = addresses.get(0).getLocality();
                        newEvent.setCity(cityName);
                        newEvent.setAddress(addresses.get(0).getAddressLine(0));
                        venue_et.setText(addresses.get(0).getAddressLine(0));
                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        else if (requestCode==GALLARY_REQUEST){
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

    private void UploadImage(Uri uri){
        final StorageReference ref = storageReference.child("events/");
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
                    Uri downloadUri = task.getResult();
                    Log.i("TAG","downlaod "+downloadUri.toString());
                    if (newEvent.getImageUrl()==null){
                        newEvent.setImageUrl(downloadUri.toString());
                        Log.i("Kaun","profilePic = "+newEvent.getImageUrl().toString() );
                    }
                    Picasso.get().load(Uri.parse(newEvent.getImageUrl())).into(imageButton);

                } else {

                }
            }
        });


    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<editEvent.RecyclerViewAdapter.ViewHolder> {
        private Context mContext;
        private List<String> mInterests;



        public RecyclerViewAdapter(Context context, List<String> mInterests){
            this.mContext = context;
            this.mInterests = mInterests;

        }

        @NonNull
        @Override
        public editEvent.RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).
                    inflate(R.layout.textview_for_interests_after_interest_selection,
                    parent,false);
            return new editEvent.RecyclerViewAdapter.ViewHolder(view);

        }


        @Override
        public void onBindViewHolder(@NonNull editEvent.RecyclerViewAdapter.ViewHolder holder, int position) {
            String interest = mInterests.get(position);
            holder.interst.setText(mInterests.get(position));

        }

        @Override
        public int getItemCount() {
            return mInterests.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView interst;

            public ViewHolder(View itemView) {
                super(itemView);
                interst = itemView.findViewById(R.id.interest);

            }


        }


    }

}
