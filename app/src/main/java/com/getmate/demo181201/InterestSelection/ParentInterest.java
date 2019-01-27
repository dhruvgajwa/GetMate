package com.getmate.demo181201.InterestSelection;

import java.util.ArrayList;

/**
 * Created by Dhruv on 6/4/2018.
 */

public class ParentInterest {
    public String name;
    public boolean state;

    public boolean isSelected=false;
    public int level;
    public int imageId;

    public boolean isState() {
        return state;
    }


    public ArrayList<ChildInterest> getChildInterests() {
        return childInterests;
    }


     //set state stands on expanded or not
    //selecte  stands on being selected omr not



     public ParentInterest(String name, boolean state, int id){
         this.name = name;
         this.state = state;
         this.imageId =id;
     }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getisState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

public ArrayList<ChildInterest> childInterests = new ArrayList<>();

}
