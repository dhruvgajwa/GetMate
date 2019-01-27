package com.getmate.demo181201;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Methods extends AppCompatActivity {
    LocationManager lm;
    private Location fi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("location","location method called");
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationListener locationListener  = new LocationListener() {
            private Location l;


            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(),location.getLatitude()+"",Toast.LENGTH_SHORT).show();
                Log.d("location2",location.getLatitude()+""+location.getLongitude());
                if(location!=null){
                    lm.removeUpdates(this);
                    l = location;
                    fi = location;
                }


            }
            public Location getL(){
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

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l!=null){
            Log.d("locationl",l.getLatitude()+""+l.getLongitude());
            fi = l;
        }



    }


    public Location getLocation() {
        return fi;


    }
}


