package com.getmate.demo181201.Activities;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.SearchView;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    CircleImageView profileImage;
    TextView username;
    private Menu menu;
    ExampleAdapter exampleAdapter;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Profile currentUserProfile;
    ArrayList<String> items = new ArrayList<>();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_share_chatactivity, menu);

        this.menu = menu;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.i("Dhruv","querry submitted0");
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Log.i("Dhruv","querry textChanges");

                    loadHistory(s);
                    exampleAdapter.getFilter().filter(s);
                    exampleAdapter.notifyDataSetChanged();
                    return true;
                }
            });
            search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    Cursor cursor= search.getSuggestionsAdapter().getCursor();
                    cursor.moveToPosition(position);
                    String suggestion =cursor.getString(1);//2 is the index of col containing suggestion name.
                    search.setQuery(suggestion,true);//setting suggestion
                    Log.i("Dhruv",suggestion);
                    return true;
                }
            });


        }

        return true;
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



    public class ExampleAdapter extends CursorAdapter {

        private List<String> items;

        private TextView text;

        public ExampleAdapter(Context context, Cursor cursor, List<String> items) {

            super(context, cursor, false);

            this.items = items;

        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {

            text.setText(items.get(cursor.getPosition()));

        }

        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.item_for_search_in_chat_activity, parent, false);

            text = (TextView) view.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Dhruv",items.get(cursor.getPosition()));
                }
            });

            return view;

        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadHistory(String query) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor
            String[] columns = new String[] { "_id", "text" };
            Object[] temp = new Object[] { 0, "default" };

            MatrixCursor cursor = new MatrixCursor(columns);

            for(int i = 0; i < items.size(); i++) {

                temp[0] = i;
                temp[1] = items.get(i);//replaced s with i as s not used anywhere.

                cursor.addRow(temp);

            }

            // SearchView
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            exampleAdapter =  new ExampleAdapter(this, cursor, items);
            search.setFilterTouchesWhenObscured(true);

            exampleAdapter.setFilterQueryProvider(new FilterQueryProvider() {

                @Override
                public Cursor runQuery(CharSequence constraint) {

                    String strItemCode = constraint.toString();
                    return search.getSuggestionsAdapter().getCursor();
                }
            });
            search.setSuggestionsAdapter(exampleAdapter);


        }

    }
}
