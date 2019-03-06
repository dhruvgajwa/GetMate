package com.getmate.demo181201.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.getmate.demo181201.R;
import com.getmate.demo181201.createEvent.GetVenueActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class getEventLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng lng;

    Boolean isFromISA = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_event_location);
        Log.i("Dhruv","B12");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i("Dhruv","B7");

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab_done);

        isFromISA = getIntent().getBooleanExtra("isFromISA",false);
        Log.i("Dhruv","B8");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Kaun","location is"+lng.latitude+lng.longitude);
                Log.d("TAG","fab clicked");


                if (isFromISA){
                    Intent i = new Intent(getEventLocation.this,GetVenueActivity.class);
                    Log.i("Dhruv","B9");

                    i.putExtra("description",getIntent().getStringExtra("description"));
                    i.putExtra("title",getIntent().getStringExtra("title"));
                    i.putExtra("from",getIntent().getLongExtra("from",0));
                    i.putExtra("to",getIntent().getLongExtra("to",0));
                    i.putStringArrayListExtra("interestB",getIntent().getStringArrayListExtra("interestB"));
                    i.putStringArrayListExtra("interestI",getIntent().getStringArrayListExtra("interestI"));
                    i.putStringArrayListExtra("interestE",getIntent().getStringArrayListExtra("interestE"));
                    i.putStringArrayListExtra("AllParentInterests",getIntent().
                            getStringArrayListExtra("AllParentInterests"));
                    i.putExtra("lat",lng.latitude);
                    i.putExtra("lon",lng.longitude);
                    startActivity(i);
                }
                else {
                    Log.i("Dhruv","B10");

                    Intent intent = new Intent();
                    intent.putExtra("lat",lng.latitude);
                    intent.putExtra("lon",lng.longitude);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("Dhruv","B11");

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(13.0827, 80.2707);
        lng= sydney;
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                lng= latLng;
                Log.i("Kaun","l"+lng.latitude+lng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("chosen Location"));
            }
        });
    }
}
