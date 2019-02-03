package com.getmate.demo181201.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.ProfileFragments.ConnectionsFragment;
import com.getmate.demo181201.ProfileFragments.RecentActivityFragment;
import com.getmate.demo181201.ProfileFragments.SavedItemsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private  Profile profile;



    public ViewPagerAdapter(FragmentManager fm, Profile profile) {
        super(fm);
        this.profile= profile;

    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0){
            fragment = new RecentActivityFragment(profile.getRecentActivities());

        }
        else if (position == 1){
            fragment = new SavedItemsFragment(profile.getSavedEvents());
        }
        else {
            fragment = new ConnectionsFragment(profile.getConnections());
        }
          return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Recent Activity";
        }
        else if (position == 1)
        {
            title = "Saved Items";
        }
        else if (position == 2)
        {
            title = "Connections";
        }
        return title;
    }
}
