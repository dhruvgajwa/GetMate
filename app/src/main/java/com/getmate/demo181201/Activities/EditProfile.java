package com.getmate.demo181201.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.InterestSelection.InterestSelectionActivity;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfile extends AppCompatActivity  {


    private static final int REQUEST_FOR_INTERNET_PERMISSION = 10;
    MaterialEditText dateView;
    int mYear,mMonth,mDay,age;
    String DOB;
    Button done,checkHandle;
    MaterialEditText name,Bio;
    MaterialEditText scl1,scl2,scl3;
    EditText pos1,pos2,pos3;
    EditText comp1,comp2,comp3;
    private String address =null;
    EditText fb_url,insta_url,linkedin_url;
    MaterialEditText phoneNo,email,website;
    Profile profile = new Profile();
    ArrayList<String> school = new ArrayList<>();
    ArrayList<Profile.Work>  work = new ArrayList<>();
    ArrayList<String> company = new ArrayList<>();
    ArrayList<String> interestB = new ArrayList<>();
    ArrayList<String> interestI = new ArrayList<>();
    ArrayList<String> interestE = new ArrayList<>();
    ArrayList<String> allParentTags = new ArrayList<>();
    Button pickDate,interestbutton,handleButton,locationbutton;
    private static final int INTEREST_SELECTION = 10;
    private static final int UNIQUE_HANDLE =1;
    String unique_handle = null;
    final private int TO_INTEREST_SELECTION =10;
    private MaterialEditText cityname;

    final private int REQUEST_FOR_LOCATION = 123;
    final private int REQUEST_FOR_STORAGE= 111;
    private String profilePic = null;
    Location currentLocation=null;
    private ArrayList<String> allInterestsList = new ArrayList<>();
    private Uri compressedFileUri=null;
    private static  final  int GALLARY_REQUEST=2;
    private Uri uri = null; //store image value or path of image
    private CircleImageView imageButton;
    private  Button submit;
    MaterialEditText handle;
    private StorageReference storageReference;
    private RadioButton RadioBtnM,RadioBtnF,RadioBtnO;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private RadioButton radioStudent, radioEmployee;
    private String Tagline;
    private Button verifyEmail;
    private Profile oldUserData ;
    FlexboxLayout flexboxLayout;

    private boolean isHandleUnique = false;
    private boolean fromRegisterActivity = false;
    private boolean fromProfileFragment = false;
    private boolean isProfileUploaded = false;
    private boolean isProfileUpdated = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile2);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//why locale .US?
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViewsById();
        checkInternetPermission();

        Intent intent = getIntent();
        if (intent.getBooleanExtra("fromRegisterActivity",false)){
            fromRegisterActivity = true;
            profile.setEmail(intent.getStringExtra("email"));
            email.setText(intent.getStringExtra("email"));
            Log.i("KaunHu","from Register activity to EditProfile");
        }
        if (intent.getBooleanExtra("fromProfileFragment",false)){
            fromProfileFragment = true;
            String profileStringData = intent.getStringExtra("dataFromProfileFragment");
            Gson gson = new Gson();
            oldUserData = gson.fromJson(profileStringData,Profile.class);
            Log.i("EDP",profileStringData);
            profile = oldUserData;
            updtaeUI(oldUserData);
           }

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                //launch date picker dialogue

                DatePickerDialog dpd = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dob = Calendar.getInstance();
                        age = mYear-year;
                        dob.set(year,month,dayOfMonth);
                        dateView.setVisibility(View.VISIBLE);
                        dateView.setText(simpleDateFormat.format(dob.getTime()));
                        DOB = String.valueOf(dob.getTimeInMillis());
                        profile.setDOB(DOB);
                        //
                    }

                },mYear,mMonth,mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()); //to set Maximum date
                dpd.show();
            }
        });


        RadioBtnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioBtnM.setChecked(true);
                RadioBtnF.setChecked(false);
                RadioBtnO.setChecked(false);
                profile.setGender("male");
            }
        });
        RadioBtnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioBtnM.setChecked(false);
                RadioBtnF.setChecked(true);
                RadioBtnO.setChecked(false);
                profile.setGender("female");
            }
        });
        RadioBtnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioBtnM.setChecked(false);
                RadioBtnF.setChecked(false);
                RadioBtnO.setChecked(true);
                profile.setGender("other");
            }
        });

        /*LinearLayout workLinLay = findViewById(R.id.workLinLay);
        workLinLay.setVisibility(View.GONE);
        scl3.setVisibility(View.GONE);*/

        radioStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioStudent.setChecked(true);
                radioEmployee.setChecked(false);
                scl1.setHint("Where you study? eg. IIT Madras");
                scl2.setHint("What you study? eg. Biomedical Engineering");

            }
        });
        radioEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioStudent.setChecked(false);
                radioEmployee.setChecked(true);
                scl1.setHint("Where you work? eg. Amazon");
                scl2.setHint("What kind of work do at company? eg. Data Analyst");


            }
        });



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:check for galary permission here before

                boolean ispermissionGiven = checkPermissionForGallary();

                if (ispermissionGiven){
                    Intent gallaryintent = new Intent(Intent.ACTION_GET_CONTENT);
                    gallaryintent.setType("Image/*");
                    startActivityForResult(gallaryintent,GALLARY_REQUEST);
                }

                }
        });

        interestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, InterestSelectionActivity.class);
                intent.putExtra("From",2);
                startActivityForResult(intent,TO_INTEREST_SELECTION);

            }
        });


        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
            checkUserPermission();
            }
        });


        checkHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sHandle = handle.getText().toString().trim();
                boolean d = check_Handle(sHandle);
                if (d){
                    profile.setHandle(sHandle);
                }

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Run all the checks and then make a method to register user in database

                boolean isValid = validateForm();

                if (!isValid){
                    return;
                }


                if(currentUser.getUid()!=null){
                    profile.setFirebase_id(currentUser.getUid());
                 }

                profile.setEmail(currentUser.getEmail());
                profile.setPhoneNo(phoneNo.getText().toString().trim());
                profile.setRangeInKm(30);

                if (isProfileUpdated){
                    profile.setProfilePic(profilePic);
                }


               // profile.setLastKnownLocation(new Location(1,1));
               if(!TextUtils.isEmpty(scl1.getText().toString())){
                   school.add(scl1.getText().toString());
                   }
                if(!TextUtils.isEmpty(scl2.getText().toString())){
                    school.add(scl2.getText().toString());
                }
               /* if(!TextUtils.isEmpty(scl3.getText().toString())){
                    school.add(scl3.getText().toString());
                }*/

               // profile.setSchools(school);

             /*  if(!TextUtils.isEmpty(pos1.getText().toString())&&TextUtils.isEmpty(comp1.getText().toString())){
                    work.add( new Profile.Work(pos1.getText().toString(),comp1.getText().toString()));
                }
                if(!TextUtils.isEmpty(pos2.getText().toString())&&TextUtils.isEmpty(comp2.getText().toString())){
                    work.add( new Profile.Work(pos2.getText().toString(),comp2.getText().toString()));
                }
                if(!TextUtils.isEmpty(pos3.getText().toString())&&TextUtils.isEmpty(comp3.getText().toString())){
                    work.add( new Profile.Work(pos3.getText().toString(),comp3.getText().toString()));
                }
*/


             if (radioEmployee.isChecked()){
                   profile.setStudent(false);
                    Tagline = "Works as "+scl2.getText().toString()+" at "+scl1.getText().toString();

                }
                if (radioStudent.isChecked()){
                    profile.setStudent(true);
                    Tagline = "Studies "+scl2.getText().toString()+" at "+scl1.getText().toString();
                }
                if (fromProfileFragment){
                          }
                if (fromRegisterActivity){
                    profile.setLat(currentLocation.getLatitude());
                    profile.setLon(currentLocation.getLongitude());
                    profile.setCreatedAt(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    }
                profile.setSclComp(scl1.getText().toString());
                profile.setSector(scl2.getText().toString());
                profile.setTagline(Tagline);
                profile.setWork(work);

                profile.setAddress(address);

                if(profilePic!=null){
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().
                            setDisplayName(profile.getName()).setPhotoUri(Uri.parse(profilePic))
                            .build();

                    currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //profile updated
                            if (task.isSuccessful()){
                                Log.i("KaunHuMein"," ProfilePic and Basic data updated successfully");
                                /*Intent intent1 = new Intent(EditProfile.this,MainActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent1);
                                finish();*/
                                onBackPressed();

                            }
                            else {
                                Log.i("KaunHuMein","profile couldn't create");
                            }

                        }
                    });

                }


                if (fromRegisterActivity){
                    //create new document request
                    Log.i("Kaun","started data writing");


                   db.collection("profiles").
                           document(currentUser.getUid()).
                           set(profile).addOnSuccessListener(
                                   new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Log.i("KaunHuMein","fromSignupActivityDataNewCreationSuccessFull");
                           Intent i = new Intent(EditProfile.this,MainActivity.class);
                           startActivity(i);
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.i("KaunHuMein","fromSignupActivityDataNewCreationFailed",e.getCause());

                       }
                   });
                   }
                if (fromProfileFragment){
                    //Update data request
                    db.collection("profiles").
                            document(currentUser.getUid()).
                            set(profile,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("KaunHuMein","fromProfileFragmentDataUpdationSuccessFull");

                            /*FragmentTransaction transaction ;
                            ProfileFragment fragment = new ProfileFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data",profile);
                            fragment.setArguments(bundle);
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commitAllowingStateLoss();
                            finish();*/
                            Intent i = new Intent(EditProfile.this,MainActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("KaunHuMein","fromProfileFragmentDataUpdationFailed",e.getCause());
                        }
                    });
                }
            }

        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==TO_INTEREST_SELECTION){
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> tags = new ArrayList<>();
                interestB = data.getStringArrayListExtra("interestB");
                interestI = data.getStringArrayListExtra("interestI");
                interestE = data.getStringArrayListExtra("interestE");
                allParentTags = data.getStringArrayListExtra("AllParentInterests");
                //use this data
                profile.setAllParentTags(allParentTags);
                profile.setInterestsB(interestB);
                profile.setInterestsI(interestI);
                profile.setInterestsE(interestE);
                if (interestB!=null){
                    tags.addAll(interestB);
                }
                if (interestE!=null){
                    tags.addAll(interestE);
                }
                if (interestI!=null){
                    tags.addAll(interestI);
                }
                profile.setAllInterests(tags);


                flexboxLayout.setVisibility(View.VISIBLE);
                int  tagsCount =tags.size() ;//... integer number of textviews
                TextView[] tages= new TextView[tagsCount];//create dynamic textviewsarray
                LinearLayout.LayoutParams layoutParams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                //loop to customize the text view and add it to flex box
                for (int i = 0; i < tagsCount; i++) {
                    tages[i] = new TextView(EditProfile.this);
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

        }
        else if (requestCode==UNIQUE_HANDLE){
            if(resultCode==Activity.RESULT_OK){
                unique_handle = data.getStringExtra("handle");
                profile.setHandle(unique_handle);
/*

                handle.setText(unique_handle);
                handle.setVisibility(View.VISIBLE);
*/

            }
        }
        else if (requestCode==GALLARY_REQUEST){
            Log.i("KaunHuMein","arrived on to gallary");
            if(resultCode == Activity.RESULT_OK){
                Uri imageURI = data.getData();
                if(imageURI!=null){
                    CropImage.activity(imageURI).setGuidelines
                            (CropImageView.Guidelines.ON).setAspectRatio(1,1).
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
                        compressedFileUri = Uri.fromFile(compressedFile);
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

    private void checkUserPermission(){

        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                },REQUEST_FOR_LOCATION);

                //if request is not available then here
            }
            else {
                //if permission is already available then
                getLocation();

                }

        }
        getLocation();
        //if Build.Version<23//find out what to do then?

    }

    private void UploadImage(Uri uri){
        final StorageReference ref = storageReference.child("profilePic/"+currentUser.getUid());
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
                    if (profilePic==null){
                        isProfileUpdated = true;
                        profilePic = downloadUri.toString();
                        Log.i("Kaun","profilePic = "+profilePic.toString() );

                    }
                    isProfileUploaded = true;
                    Picasso.get().load(Uri.parse(profilePic)).into(imageButton);

                } else {

                }
            }
        });


    }


    public boolean validateForm(){
        boolean isValid= true;

        profile.setName(name.getText().toString());
        if(profile.getName()==null){
            name.setError("Name Required");
            isValid =false;
        }

        if (profile.getDOB()==null){
            Toast.makeText(getApplicationContext(),"DOB not specified",Toast.LENGTH_LONG).show();
            isValid=false;
        }


        if (profile.getGender()==null){
            Toast.makeText(getApplicationContext(),"Gender not specified",Toast.LENGTH_LONG).show();
            isValid=false;
        }

        profile.setBio(Bio.getText().toString().trim());
        if (profile.getBio()==null){
            Bio.setError("Fill bio");
            Toast.makeText(getApplicationContext(),"Bio is empty not specified",Toast.LENGTH_LONG).show();
            isValid=false;
        }

        if (profile.getAllInterests()==null){
            Toast.makeText(getApplicationContext(),"Interests are not specified",Toast.LENGTH_LONG).show();
            isValid=false;
        }
        profile.setHandle(handle.getText().toString().trim());
        if (profile.getHandle()==null){
            Toast.makeText(getApplicationContext(),"Handle not specified",Toast.LENGTH_LONG).show();
            isValid=false;
        }



        if (!isProfileUploaded && fromRegisterActivity){
            Toast.makeText(getApplicationContext(),"profile Image Not uploaded",Toast.LENGTH_LONG).show();
            isValid=false;
        }


        return isValid;
    }
    private void findViewsById() {
        email = findViewById(R.id.email_ep);
        cityname = findViewById(R.id.city_name);
        imageButton = findViewById(R.id.profile_aep);
        done = findViewById(R.id.done_aep);
        name = findViewById(R.id.name_aep);
        dateView = findViewById(R.id.date);
        Bio = findViewById(R.id.bio_aep);
        scl1= findViewById(R.id.scl1);
        scl2= findViewById(R.id.scl2);
        checkHandle = findViewById(R.id.check_handle);
        pos1= findViewById(R.id.pos1);
        handle = findViewById(R.id.unique_handle);
        pos2= findViewById(R.id.pos2);
        pos3= findViewById(R.id.pos3);
        fb_url= findViewById(R.id.fb_url_aep);
        insta_url = findViewById(R.id.insta_url_aep);
        linkedin_url= findViewById(R.id.linkedin_url_aep);
        phoneNo = findViewById(R.id.phone_no_aep);
        pickDate = findViewById(R.id.pick_date);
        comp1 = findViewById(R.id.company1);
        comp2 = findViewById(R.id.company2);
        comp3 = findViewById(R.id.company3);
        interestbutton = findViewById(R.id.interest_btn_aep);
        //handleButton = findViewById(R.id.handle_btn_aep);
        locationbutton =findViewById(R.id.location_aep);
        RadioBtnO = findViewById(R.id.radio_other);
        RadioBtnF= findViewById(R.id.radio_female);
        RadioBtnM = findViewById(R.id.radio_male);
        radioEmployee= findViewById(R.id.radio_employee);
        radioStudent = findViewById(R.id.radio_student);
        verifyEmail = findViewById(R.id.verify_email);
        flexboxLayout = findViewById(R.id.tagsView_ep);

    }



    public void getLocation() {
        final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.
                        ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationListener locationListener = new LocationListener() {
            private Location l;

            @Override
            public void onLocationChanged(Location location) {

                Toast.makeText(getApplicationContext(), location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_LONG).show();
                Log.i("location2", location.getLatitude() + "" + location.getLongitude());
               // textView.setText(location.getLatitude() + "and " + location.getLongitude());

                if (location!= null) {
                    lm.removeUpdates(this);

                    l = location;
                    currentLocation = location;

                    Geocoder geocoder = new Geocoder(EditProfile.this,Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),
                                currentLocation.getLongitude(),1);
                        if(addresses.size()>0){
                            address = addresses.toString();
                            Log.i("KaungetLocality",addresses.get(0).getLocality());
                            Log.i("KaungetAddress",addresses.get(0).getAddressLine(0));
                            String cityName = addresses.get(0).getLocality();
                            profile.setCity (cityName);
                            cityname.setText(cityName);
                            profile.setAddress(addresses.get(0).getAddressLine(0));

                            }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            public Location getL() {
                return l;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);



        //this getAddres should br on another thread! This sucks a lot of resources
        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l!=null){
            Log.i("locationl",l.getLatitude()+""+l.getLongitude());
            currentLocation = l;
            Geocoder geocoder = new Geocoder(EditProfile.this,Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),
                        currentLocation.getLongitude(),1);
                if(addresses.size()>0){

                    address = addresses.toString();

                    Log.i("KaunHuMein",addresses.get(0).getLocality());
                    String cityName = addresses.get(0).getLocality();
                    profile.setCity(cityName);
                    profile.setAddress(addresses.get(0).getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_FOR_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();

            } else {
                Toast.makeText(this, "permission for location is denied", Toast.LENGTH_LONG).show();

            }
        }

        else if (requestCode==REQUEST_FOR_STORAGE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent gallaryintent = new Intent(Intent.ACTION_GET_CONTENT);
                gallaryintent.setType("Image/*");
                startActivityForResult(gallaryintent,GALLARY_REQUEST);

            }

            else {
                Toast.makeText(this, "permission for storage is denied", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


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

    private void updtaeUI(Profile profile){

        if(profile.getProfilePic()!=null){
            Picasso.get().load(profile.getProfilePic()).into(imageButton);
            }


            name.setText(profile.getName());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar c = Calendar.getInstance();
        if (profile.getDOB()!=null){
            c.setTimeInMillis(Long.valueOf(profile.getDOB()));
        }

        dateView.setText(simpleDateFormat.format(c.getTime()));
        RadioBtnM.setChecked(profile.getGender().equals("male"));
        RadioBtnF.setChecked(profile.getGender().equals("female"));
        RadioBtnO.setChecked(profile.getGender().equals("other"));
        Bio.setText(profile.getBio());
        handle.setText(profile.getHandle());
        String s = profile.getTagline();
        if (profile.isStudent()){
            radioStudent.setChecked(true);
            radioEmployee.setChecked(false);
            }
        else {
            radioEmployee.setChecked(true);
            radioStudent.setChecked(false);
        }
        if (profile.getSclComp()!=null && profile.getSector()!=null){
            scl1.setText(profile.getSclComp());
            scl2.setText(profile.getSector());
        }
        cityname.setText(profile.getCity());
  // scl1.setText(profile.getSchools().get(1));
        email.setText(currentUser.getEmail());
        if (profile.isEmailVerified()){
            verifyEmail.setText("verified");
            verifyEmail.setBackgroundColor(getResources().getColor(R.color.image_color));
        }

        if (profile.getHandle()!=null){
            handle.setText(profile.getHandle());
        }

        checkHandle.setText("change");
        phoneNo.setText(profile.getPhoneNo());
    }


    private void checkInternetPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!=
                    PackageManager.PERMISSION_GRANTED)
            {  Log.i("Kaun","internet permission is not given");
                requestPermissions(new String[]{Manifest.permission.INTERNET
                },REQUEST_FOR_INTERNET_PERMISSION);
                //if request is not available then here
            }

        }
        else {
            Log.i("Kaun","build version is smaller");
        }
    }



    public boolean check_Handle(String handle){
        //run a querry that result in this data

        db.collection("profiles").whereEqualTo("handle",handle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                if (list.size()==0){
                    isHandleUnique = true;
                    Log.i("Kaun","The handle is unique");
                    isHandleUnique = true;
                    checkHandle.setBackgroundColor(getResources().getColor(R.color.image_color));

                }
                else {
                    isHandleUnique = false;
                    Log.i("Kaun","The Handle already exists");
                    Toast.makeText(getApplicationContext(),"Handle already Exixits",Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Kaun","Failed",e.getCause());
                Toast.makeText(getApplicationContext(),"Not Reachable",Toast.LENGTH_LONG).show();
                isHandleUnique= false;
            }
        });
    return  isHandleUnique;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
