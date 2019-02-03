package com.getmate.demo181201;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.Activities.ChatActivity;
import com.getmate.demo181201.Activities.StartActivity;
import com.getmate.demo181201.Fragments.FindMate;
import com.getmate.demo181201.Fragments.MapFragment;
import com.getmate.demo181201.Fragments.ProfileFragment;
import com.getmate.demo181201.Fragments.TimelineFragment;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.ProfileFragments.ConnectionsFragment;
import com.getmate.demo181201.ProfileFragments.RecentActivityFragment;
import com.getmate.demo181201.ProfileFragments.SavedItemsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  implements
        MapFragment.OnFragmentInteractionListener ,
ProfileFragment.OnFragmentInteractionListener,
        RecentActivityFragment.OnFragmentInteractionListener,SavedItemsFragment.OnFragmentInteractionListener,
        ConnectionsFragment.OnFragmentInteractionListener {
    final Fragment fragment1 =new  TimelineFragment();

    final Fragment fragment2 = new FindMate();
    final Fragment fragment3 = new MapFragment();
    final Fragment fragment4 = new ProfileFragment();
    Fragment active = fragment1;
    FragmentManager fm = getSupportFragmentManager();

    private static final int REQUEST_FOR_INTERNET_PERMISSION = 10;
    ProgressDialog progressDialog;
    private String currentCity =null;
    private Location currentLocation ;
    private Profile currentUserProfile;
    private static final String TAG = "MainActivity";
    final private int REQUEST_CODE_ASK_PERMISSION = 123;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textView;
    private ActionBar toolbar;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private ArrayList<Event> events = new ArrayList<>();
    private FloatingActionButton logout;

    private boolean GetLocationFlag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //toolbar.setTitle("Shop");
        logout= findViewById(R.id.logout);
        logout.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        if (mAuth.getCurrentUser()!=null){
            currentUser = mAuth.getCurrentUser();
            if(currentUser.getUid()==null){

            }
            else {
                getProfileData(currentUser.getUid());
            }

        }
        else {
            Intent i = new Intent(MainActivity.this,StartActivity.class);
            startActivity(i);
            finish();
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cohortso");

        if (GetLocationFlag){
            checkUserPermission();
            GetLocationFlag = false;
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
            }
        });




    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_gifts:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_cart:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;


                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

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


                if (location!= null) {
                    lm.removeUpdates(this);
                    l = location;
                    currentLocation = location;
                    //new BEckGround().execute();

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




        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l!=null){
            Log.i("locationl",l.getLatitude()+""+l.getLongitude());
            currentLocation = l;
            lm.removeUpdates(locationListener);
            //new BEckGround().execute();
        }
        }



    private void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
               {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                },REQUEST_CODE_ASK_PERMISSION);

                //if request is not available then here
            }

            getLocation();



        }
        getLocation();

    }


        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case REQUEST_CODE_ASK_PERMISSION:{
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        getLocation();
                    }
                    else {
                        Toast.makeText(this, "LOcation permission is denied", Toast.LENGTH_LONG).show();
                    }
                }


                case REQUEST_FOR_INTERNET_PERMISSION:{
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    }
                    else {
                        Toast.makeText(this, "Internet permission is denied", Toast.LENGTH_LONG).show();
                    }
                }

                    break;
                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }



        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.chat){
            Intent intent = new Intent(MainActivity.this,ChatActivity.class);
            Gson gson = new Gson();
            String data = gson.toJson(currentUserProfile);
            intent.putExtra("data",data);
            startActivity(intent);
        }

        if(id==R.id.action_logout){


            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();

         }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    private void getEventsData(){
        Log.i("getEventsData","Started");
       // progressDialog.setMessage("Loading events data");
        ArrayList<String> tags = new ArrayList<>();
        //tags = currentUserProfile.getAllInterests();
         tags = currentUserProfile.getAllParentTags();
        Calendar c = Calendar.getInstance();
        Long t = c.getTimeInMillis();

        for (String tag: tags) {
            //find By city and tags
            db.collection("events").whereEqualTo("city",
                    currentUserProfile.getCity()).whereArrayContains("allParentTags",
                    tag)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot querry:task.getResult()) {
                            events.add(querry.toObject(Event.class));
                            Log.i("getEventsData","success"+task.getResult().size());
                           //fragment1.eventListAdapter.notifyDataSetChanged();
                            Bundle bundle =new Bundle();
                            bundle.putParcelable("currentUserProfile",currentUserProfile);
                            bundle.putParcelableArrayList("events",events);
                            fragment4.setArguments(bundle);
                            fragment3.setArguments(bundle);
                            fragment2.setArguments(bundle);
                            fragment1.setArguments(bundle);
                            fm.beginTransaction().add(R.id.main_container,fragment4,"4").hide(fragment4).commit();
                            fm.beginTransaction().add(R.id.main_container,fragment3,"3").hide(fragment3).commit();
                            fm.beginTransaction().add(R.id.main_container,fragment2,"2").hide(fragment2).commit();
                            fm.beginTransaction().add(R.id.main_container,fragment1,"2").commit();
                        }
                    }
                    else {
                        Log.i("getEventsData","task unsuccessful");
                    }

                }
            });

        }
        Log.i("getEventsData","ended");
        //progressDialog.dismiss();

    }

    private void getProfileData(String firebase_id) {
        progressDialog.show();
        progressDialog.setMessage("getting profile data");
        DocumentReference documentReference = db.collection("profiles").document(firebase_id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUserProfile = documentSnapshot.toObject(Profile.class);
                 if (currentUserProfile!=null){
                     Gson gson = new Gson();
                     String json = gson.toJson(currentUserProfile);
                     Log.i("KaunHUMein",json+"\n"+"now calling demoFunctions");
                     progressDialog.dismiss();
                     getEventsData();
                    //BEckGround b = new BEckGround();
                    //b.execute();

                     // TODO: getEventsData();

                }
                Log.i("KaunH",documentSnapshot.toString());
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("KaunH","getProfileDataFailed"+e.getCause());
                Log.e("KaunH","getProfileDataFailed",e.getCause());

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Coudn't receive profile data from server",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Get the pr described event
    private void demoFunction(){
    progressDialog.show();
    progressDialog.setMessage("Loading Demo Event data");
        db.collection("events").
                document("MAXFIMvh7CKlNZYZwRnD").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Event event = new Event();
                if (documentSnapshot!=null){
                    event = documentSnapshot.toObject(Event.class);
                    if (event!=null){
                        events.add(event);
                        progressDialog.dismiss();
                        Bundle bundle =new Bundle();
                        bundle.putParcelable("currentUserProfile",currentUserProfile);
                        bundle.putParcelableArrayList("events",events);
                        fragment4.setArguments(bundle);
                        fragment3.setArguments(bundle);
                        fragment2.setArguments(bundle);
                        fragment1.setArguments(bundle);
                        fm.beginTransaction().add(R.id.main_container,fragment4,"4").hide(fragment4).commit();
                        fm.beginTransaction().add(R.id.main_container,fragment3,"3").hide(fragment3).commit();
                        fm.beginTransaction().add(R.id.main_container,fragment2,"2").hide(fragment2).commit();
                        fm.beginTransaction().add(R.id.main_container,fragment1,"2").commit();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"NOT REACHABLE",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Log.i("KaunHai","documnent snapshot is null");
                }

            }
        });
    }

    public class BEckGround extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            getEventsData();
            return null;
        }
    }


}




