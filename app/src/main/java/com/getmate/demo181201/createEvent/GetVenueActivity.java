package com.getmate.demo181201.createEvent;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getmate.demo181201.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetVenueActivity extends AppCompatActivity {


   EditText venue,cityET ;
   Button next,previous;
   String city;
   String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_venue);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        venue = findViewById(R.id.venue);
        cityET = findViewById(R.id.city);

        LatLng lng = new LatLng(getIntent().getDoubleExtra("lat",0.00),
                getIntent().getDoubleExtra("lon",0.00));
        Geocoder geocoder = new Geocoder(GetVenueActivity.this,Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lng.latitude,
                    lng.longitude,1);
            if(addresses.size()>0){
                String cityName =
                city=addresses.get(0).getLocality();
                cityET.setText(city);
              address = addresses.get(0).getAddressLine(0);
                venue.setText(addresses.get(0).getAddressLine(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( GetVenueActivity.this,AddImageActivity.class);

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
                i.putExtra("address",address);
                i.putExtra("city",city);

                startActivity(i);
            }
        });


    }
}
