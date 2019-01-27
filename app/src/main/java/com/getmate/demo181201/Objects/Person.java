package com.getmate.demo181201.Objects;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dhruv on 5/28/2018.
 *
 *
 */

public class Person implements Serializable, SharedPreferences {
    float r_lat;
    float r_lon;
    int gender;
    int connections[];
    int recentActivities[];
    int[] savedItems;
    ArrayList<String> acheivemets;
    ArrayList<String> school;
    ArrayList<String> interestsB;
    ArrayList<String> interestsI;
    ArrayList<String> interestsE;
    ArrayList<String> work;


    Long DOB;
    String name;
    String handle;
    String profilePic;
    String coverPic;
    String email;
    String bio;
    String fb_url;
    String insta_url;
    String link_url;
    String wordpress_url;

    String address;
    int user_id,range;
    float c_lat;

    float c_lon;
    long phone;

    public String getWordpress_url() {
        return wordpress_url;
    }

    public void setWordpress_url(String wordpress_url) {
        this.wordpress_url = wordpress_url;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }



    public Long getDOB() {
        return DOB;
    }

    public void setDOB(Long DOB) {
        this.DOB = DOB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<String> getWork() {
        return work;
    }

    public void setWork(ArrayList<String> work) {
        this.work = work;
    }


    public String getHandle() {
        return handle;
    }

    public void setHandle(String unq_handle) {
        this.handle = unq_handle;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }  //undone

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFb_url() {
        return fb_url;
    }

    public void setFb_url(String fb_url) {
        this.fb_url = fb_url;
    }

    public String getInsta_url() {
        return insta_url;
    }

    public void setInsta_url(String insta_url) {
        this.insta_url = insta_url;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public float getC_lat() {
        return c_lat;
    }

    public void setC_lat(float c_lat) {
        this.c_lat = c_lat;
    }

    public float getC_lon() {
        return c_lon;
    }

    public void setC_lon(float c_lon) {
        this.c_lon = c_lon;
    }

    public float getR_lat() {
        return r_lat;
    }

    public void setR_lat(float r_lat) {
        this.r_lat = r_lat;
    }

    public float getR_lon() {
        return r_lon;
    }

    public void setR_lon(float r_lon) {
        this.r_lon = r_lon;
    }

    public int[] getConnections() {
        return connections;
    }

    public void setConnections(int[] connections) {
        this.connections = connections;
    } //still unused

    public int[] getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(int[] recentActivities) {
        this.recentActivities = recentActivities;
    }

    public int[] getSavedItems() {
        return savedItems;
    }

    public void setSavedItems(int[] savedItems) {
        this.savedItems = savedItems;
    } //still unused

    public ArrayList<String> getAcheivemets() {
        return acheivemets;
    }

    public void setAcheivemets(ArrayList<String> acheivemets) {
        this.acheivemets = acheivemets;
    }

    public ArrayList<String> getSchool() {
        return school;
    }

    public void setSchool(ArrayList<String> school) {
        this.school = school;
    }

    public ArrayList<String> getInterestsB() {
        return interestsB;
    }

    public void setInterestsB(ArrayList<String> interestsB) {
        this.interestsB = interestsB;
    }

    public ArrayList<String> getInterestsI() {
        return interestsI;
    }

    public void setInterestsI(ArrayList<String> interestsI) {
        this.interestsI = interestsI;
    }

    public ArrayList<String> getInterestsE() {
        return interestsE;
    }

    public void setInterestsE(ArrayList<String> interestsE) {
        this.interestsE = interestsE;
    }

    /**
     * Retrieve all values from the preferences.
     * <p>
     * <p>Note that you <em>must not</em> modify the collection returned
     * by this method, or alter any of its contents.  The consistency of your
     * stored data is not guaranteed if you do.
     *
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @throws NullPointerException
     */
    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     * @throws ClassCastException
     */
    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return null;
    }

    /**
     * Retrieve a set of String values from the preferences.
     * <p>
     * <p>Note that you <em>must not</em> modify the set instance returned
     * by this call.  The consistency of the stored data is not guaranteed
     * if you do, nor is your ability to modify the instance at all.
     *
     * @param key       The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     * Throws ClassCastException if there is a preference with this name
     * that is not a Set.
     * @throws ClassCastException
     */
    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return null;
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * an int.
     * @throws ClassCastException
     */
    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a long.
     * @throws ClassCastException
     */
    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a float.
     * @throws ClassCastException
     */
    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a boolean.
     * @throws ClassCastException
     */
    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     * otherwise false.
     */
    @Override
    public boolean contains(String key) {
        return false;
    }

    /**
     * Create a new Editor for these preferences, through which you can make
     * modifications to the data in the preferences and atomically commit those
     * changes back to the SharedPreferences object.
     * <p>
     * <p>Note that you <em>must</em> call {@link Editor#commit} to have any
     * changes you perform in the Editor actually show up in the
     * SharedPreferences.
     *
     * @return Returns a new instance of the {@link Editor} interface, allowing
     * you to modify the values in this SharedPreferences object.
     */
    @Override
    public Editor edit() {
        return null;
    }

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     * <p>
     * <p class="caution"><strong>Caution:</strong> The preference manager does
     * not currently store a strong reference to the listener. You must store a
     * strong reference to the listener, or it will be susceptible to garbage
     * collection. We recommend you keep a reference to the listener in the
     * instance data of an object that will exist as long as you need the
     * listener.</p>
     *
     * @param listener The callback that will run.
     * @see #unregisterOnSharedPreferenceChangeListener
     */
    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    /**
     * Unregisters a previous callback.
     *
     * @param listener The callback that should be unregistered.
     * @see #registerOnSharedPreferenceChangeListener
     */
    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }
}

