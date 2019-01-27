package com.getmate.demo181201;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

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

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new RecentActivityFragment(profile.getRecentActivities());
        if (position == 0){

            if (profile!=null){
                fragment = new RecentActivityFragment(profile.getRecentActivities());
            }
            else{
                Log.i("Kaun","profile is null");
            }

        }
        else if (position == 1){
            fragment = new SavedItemsFragment(profile.getSavedEvents());
        }
        else if (position == 2){
            fragment = new ConnectionsFragment(profile.getConnections());
        }
          return fragment;
    }

    /**
     * Return the number of views available.
     */
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
