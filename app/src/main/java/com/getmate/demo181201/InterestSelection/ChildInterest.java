package com.getmate.demo181201.InterestSelection;

/**
 * Created by Dhruv on 6/4/2018.
 */

public class ChildInterest {
    String name, pname;
    Boolean state,isSelected=false;
    int getParentPos,getChildPos;


    public ChildInterest(String name, Boolean state, int id){
        this.name = name;
        this.imageId = id;
        this.state = state;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selectd) {
        isSelected = selectd;
    }

    public int getGetParentPos() {
        return getParentPos;
    }

    public void setGetParentPos(int getParentPos) {
        this.getParentPos = getParentPos;
    }

    public int getGetChildPos() {
        return getChildPos;
    }

    public void setGetChildPos(int getChildPos) {
        this.getChildPos = getChildPos;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    int imageId;
    int level=1;
}
