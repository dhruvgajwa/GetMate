package com.getmate.demo181201.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.getmate.demo181201.ChatsFragments.ChatsFragment;
import com.getmate.demo181201.ChatsFragments.UsersFragment;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    CircleImageView profileImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Profile currentUserProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.basil_orange));
        }

        profileImage = findViewById( R.id.profileImage);
        username = findViewById(R.id.userName_ma);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getIntent().getStringExtra("data");
        if (data!=null){
            Gson gson = new Gson();
            currentUserProfile = gson.fromJson(data,Profile.class);
            username.setText(currentUserProfile.getName());
            Picasso.get().load(currentUserProfile.getProfilePic()).into(profileImage);
        }


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        Boolean forSharingEvent = getIntent().getBooleanExtra("forSharingEvent",false);
        if (forSharingEvent){
            String text = getIntent().getStringExtra("text");

            LocalViewPagerAdapter viewPagerAdapter = new LocalViewPagerAdapter(getSupportFragmentManager(),text);
            viewPagerAdapter.addFragment(new ChatsFragment(text),"Chats");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        else {

            LocalViewPagerAdapter viewPagerAdapter = new LocalViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
            viewPagerAdapter.addFragment(new UsersFragment(),"Users");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }



    }


    public class LocalViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments ;
        private ArrayList<String> titles;
        private String message = null;

        public LocalViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }


        public LocalViewPagerAdapter(FragmentManager fm ,String text){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
            this.message = text;
  }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
