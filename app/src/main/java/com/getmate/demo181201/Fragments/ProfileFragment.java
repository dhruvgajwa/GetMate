package com.getmate.demo181201.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.getmate.demo181201.Activities.EditProfile;
import com.getmate.demo181201.Activities.editEvent;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.getmate.demo181201.ViewPagerAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    Profile currentUserProfile =new Profile();
    TextView age,gender,no_connections,school,work,name,handle,email,bio,city;
    CircleImageView profilePic;
    Button createEventButton;
    Context context;
    Button editProfile;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            currentUserProfile = bundle.getParcelable("currentUserProfile");
            if (currentUserProfile!=null){
                Log.i("KaunH in PF", currentUserProfile.toString());
            }

        }

        context= this.getActivity();
        findViewsById(view);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Recent Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Interest"));
        tabLayout.addTab(tabLayout.newTab().setText("Connections"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),currentUserProfile);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (currentUserProfile!=null){
            updateUI();
        }
        else {
            Log.i("Kaun","Tatti kuch nai aya hain vahans e");
        }


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,editEvent.class);
                startActivity(intent);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),EditProfile.class);
                i.putExtra("fromProfileFragment",true);
                Gson gson = new Gson();
                String profileStringData = gson.toJson(currentUserProfile);
                i.putExtra("dataFromProfileFragment",profileStringData);
                startActivity(i);
            }
        });




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



    public void findViewsById(View view){
        View view1 = view.findViewById(R.id.photoHeader);
        age = view1.findViewById(R.id.tvAge);
        name = view.findViewById(R.id.tvName);
        handle = view.findViewById(R.id.handle);
        work = view.findViewById(R.id.tvTitle);
       // school = view.findViewById(R.id.tvEducation);
        email = view.findViewById(R.id.tvEmail);
        city = view.findViewById(R.id.tvAddress);
        bio = view.findViewById(R.id.tvSummary);
        profilePic = view.findViewById(R.id.civic_profile_pic);
        createEventButton = view.findViewById(R.id.create_event_button);
        editProfile = view.findViewById(R.id.edit_profile);



    }


    public void updateUI(){
        name.setText(currentUserProfile.getName());
        handle.setText(currentUserProfile.getHandle());
        Calendar c = Calendar.getInstance();
        if (currentUserProfile.getDOB()!=null){
            c.setTimeInMillis(Long.valueOf(currentUserProfile.getDOB()));
        }
        if (currentUserProfile.getProfilePic()!=null){
            Picasso.get().load(currentUserProfile.getProfilePic()).into(profilePic);
        }

        int a = Calendar.getInstance().get(Calendar.YEAR) -  c.get(Calendar.YEAR);
        age.setText(String.valueOf(a));
        email.setText(currentUserProfile.getEmail());
        city.setText(currentUserProfile.getCity());
        bio.setText(currentUserProfile.getBio());
        work.setText(currentUserProfile.getTagline());
        Picasso.get().load(currentUserProfile.getProfilePic()).into(profilePic);

    }
}
