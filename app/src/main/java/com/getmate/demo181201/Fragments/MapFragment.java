package com.getmate.demo181201.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getmate.demo181201.Activities.DetailedEvents;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener
         {

    ArrayList<Event> events = new ArrayList<>();
    GoogleApiClient mGoogleApiCLient;


    Location mLastLocation;
    GoogleMap mMap;
    MarkerOptions markerOptions = new MarkerOptions();
    Marker mCurrentLocationMarker;
    final String URL_MAPFRAG = "Some required url";
    int user_id;
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    RadioGroup levelGroup,eventPeopleGroup;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {

    }


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Bundle bundle = getArguments();
        if(bundle!=null){
            events = bundle.getParcelableArrayList("events");
            //here call all possible methods regarding
            //updating UI and making calls
        }
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this::onMapReady);

        levelGroup = view.findViewById(R.id.level_radio_grp);
        eventPeopleGroup = view.findViewById(R.id.people_event_radioGroup);
        levelGroup.setVisibility(View.GONE);
        eventPeopleGroup.setVisibility(View.GONE);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(googleMap.MAP_TYPE_TERRAIN);
        LatLng sydney = new LatLng(events.get(0).getLat(),events.get(0).getLon());
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (events!=null){
            Log.i("Kaun","events are available"+events.get(0).getTitle());
            for (Event event:events) {
                int i = 1;
                Log.i("Kaun","Ran for "+ i +"times");
                i++;
                LatLng latLng = new LatLng(event.getLat(),event.getLon());
                mMap.addMarker(new MarkerOptions().position(latLng).title(event.getTitle())).setTag(event);
                }
        }
        else {
            Toast.makeText(this.getActivity(),"There are no events for you",Toast.LENGTH_LONG).show();
            Log.i("Kaun","No events are bitch");
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent i = new Intent(getActivity(),DetailedEvents.class);
                //i.putExtra("event", (Event) marker.getTag());
                startActivity(i);
                return false;
            }
        });

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildgoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildgoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    @Override
    public void onLocationChanged(Location location) {


        mLastLocation= location;
        if(mCurrentLocationMarker!=null){
            mCurrentLocationMarker.remove();

        }

                 LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude())  ;
                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position(latLng);
                 markerOptions.title("yeh mere baap ki jagah hain");

                 markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                 //set bitmap to icons related to their interest
                 mCurrentLocationMarker = mMap.addMarker(markerOptions);
                 mCurrentLocationMarker.setTag(latLng);

                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,8f));
                 mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                /* if(mGoogleApiCLient!=null){
                     LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiCLient,
                             (com.google.android.gms.location.LocationListener) this);
                 }
*/
             }

             @Override
             public void onStatusChanged(String s, int i, Bundle bundle) {

             }

             @Override
             public void onProviderEnabled(String s) {

             }

             @Override
             public void onProviderDisabled(String s) {

             }

             @Override
             public void onConnected(@Nullable Bundle bundle) {

            // Request location update here!




             }

             @Override
             public void onConnectionSuspended(int i) {

             }

             @Override
             public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

             }



             /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    protected  synchronized void buildgoogleApiClient(){
     mGoogleApiCLient = new GoogleApiClient.Builder(getActivity()).
                         addConnectionCallbacks(this).
                         addOnConnectionFailedListener(this).
                         addApi(LocationServices.API).
                         build();

                 mGoogleApiCLient.connect();
    }
}
