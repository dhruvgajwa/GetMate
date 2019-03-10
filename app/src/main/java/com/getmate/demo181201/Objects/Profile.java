package com.getmate.demo181201.Objects;


/*
{
"firstName":"Dhruv",
"lastName":"Gajwa",
"firebase_id" = ,
"gender":"male",
"DOB":1565655454554,
"handle":"Random_Nerd",
"profilePic":"some_url",
"email":"dhruvg.dakshana16@gmail.com",
"bio":"My life is dull an I don't know what to do so I am doing nothing at all",
"phone":7092541851,
"range":40,
"Location":"Location object here",
"school":["jnv shajapur","deepti convetnt school"],
"address":"Address Object here",
"work":[{"position":"software developer",
	 "company":"Cognizant"},
	{"position":"consultant",
	 "company":"Wipro"}]
"isEmailVerified":true,
"fb_url":"www.facebook.com/dhruv.gajwa",
"insta_url":"www.instagram.com/random___nerd",
"link_url":"www.linkedin.com/dhruv.gajwa" ,
"interestsB":["php","MySQL","machine learning","calligraphy"],
"interestsI":["Swimming","Running","Cycling","Android"],
"interestsE":["nothing"],
"allInterestList":[],

"wordpress_url":"www.wordpress.com/randomNerd",
"other_url":"www.getmate.in",
"connections":["firebase_id of people"],
"recentActivities":[545,5468,56551,556123,54542,65121,551225,124512,4521221,65323],
"savedItems":[55454,568,6898,98556,985644,5556565,5556555,4545245,155455,455455,12232,5542325,23652],
}
*/

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//By Default set all of them to null
//TODO: ADD ALL privacy related data here
public class Profile implements Parcelable {
    protected Profile(Parcel in) {
        createdAt = in.readString();
        achievements = in.createStringArrayList();
        name = in.readString();
        city = in.readString();
        firebase_id = in.readString();
        gender = in.readString();
        DOB = in.readString();
        handle = in.readString();
        profilePic = in.readString();
        email = in.readString();
        bio = in.readString();
        phoneNo = in.readString();
        rangeInKm = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
        tagline = in.readString();
        createdEvents = in.createStringArrayList();
        sector = in.readString();
        sclComp = in.readString();
        schools = in.createStringArrayList();
        address = in.readString();
        isEmailVerified = in.readByte() != 0;
        interestsB = in.createStringArrayList();
        interestsI = in.createStringArrayList();
        interestsE = in.createStringArrayList();
        allInterests = in.createStringArrayList();
        recentActivities = in.createStringArrayList();
        profilesIswipedRight = in.createStringArrayList();
        profilesSwipedMeRight = in.createStringArrayList();
        savedEvents = in.createStringArrayList();
        isStudent = in.readByte()!=0;
        allParentTags = in.createStringArrayList();

    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public ArrayList<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<String> achievements) {
        this.achievements = achievements;
    }

    ArrayList<Ticket> myTickets = new ArrayList<>();

    public String createdAt=null;
    public ArrayList<String> allParentTags= new ArrayList<>();
    public ArrayList<String> achievements= new ArrayList<>();
    public ArrayList<String> createdEvents= new ArrayList<>();
    private String name=null;
    private String city = null;
    private String firebase_id=null;
    private String gender=null;
    private String DOB=null;
    private String handle=null;
    private String profilePic=null;
    private String email=null;
    private String bio=null;
    private String phoneNo=null;
    private int rangeInKm=0;
    private double lat = 0;
    private double lon = 0;
    private String tagline=null;
    private boolean isStudent = false;
    private String sclComp = null;
    private String sector = null;
    private ArrayList<String> profilesSwipedMeRight = new ArrayList<>();
    private ArrayList<String> profilesIswipedRight = new ArrayList<>();

    private ArrayList<String> schools=new ArrayList<>();
    private String address = null;
    private ArrayList<Work> work=null;
    private boolean isEmailVerified=false;
    private ArrayList<String> interestsB=null, interestsI=null,
            interestsE=null,allInterests=null;
   // private ArrayList<String> connections=null;
    private ArrayList<String> recentActivities=null;
    private ArrayList<String> savedEvents=new ArrayList<>();
    private ArrayList<ConnectionObject> connections = new ArrayList<>();


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(achievements);
        parcel.writeString(name);
        parcel.writeString(createdAt);
        parcel.writeString(city);
        parcel.writeString(firebase_id);
        parcel.writeString(gender);
        parcel.writeString(DOB);
        parcel.writeString(handle);
        parcel.writeString(profilePic);
        parcel.writeStringList(createdEvents);
        parcel.writeString(email);
        parcel.writeString(bio);
        parcel.writeString(phoneNo);
        parcel.writeString(sclComp);
        parcel.writeString(sector);
        parcel.writeInt(rangeInKm);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeString(tagline);
        parcel.writeStringList(schools);
        parcel.writeString(address);
        parcel.writeByte((byte) (isEmailVerified ? 1 : 0));
        parcel.writeByte((byte) (isStudent ? 1 : 0));
        parcel.writeStringList(profilesIswipedRight);
        parcel.writeStringList(profilesSwipedMeRight);
        parcel.writeStringList(interestsB);
        parcel.writeStringList(interestsI);
        parcel.writeStringList(interestsE);
        parcel.writeStringList(allInterests);
        parcel.writeStringList(recentActivities);
        parcel.writeStringList(savedEvents);
        parcel.writeStringList(allParentTags);
    }


    public static class Work{
        private String position=null;
        private String company=null;

        public Work(String position,String company){
            this.position = position;
            this.company = company;

        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }


    public ArrayList<String> getAllParentTags() {
        return allParentTags;
    }

    public void setAllParentTags(ArrayList<String> allParentTags) {
        this.allParentTags = allParentTags;
    }

    public ArrayList<Ticket> getMyTickets() {
        return myTickets;
    }

    public void setMyTickets(ArrayList<Ticket> myTickets) {
        this.myTickets = myTickets;
    }

    public static Creator<Profile> getCREATOR() {
        return CREATOR;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public String getTagline() {
        return tagline;
    }

    public ArrayList<String> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(ArrayList<String> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    //Constrcters
    public Profile(){

    }

    public String getSclComp() {
        return sclComp;
    }

    public void setSclComp(String sclComp) {
        this.sclComp = sclComp;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String  address) {
        this.address = address;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getRangeInKm() {
        return rangeInKm;
    }

    public void setRangeInKm(int rangeInKm) {
        this.rangeInKm = rangeInKm;
    }



    public ArrayList<String> getSchools() {
        return schools;
    }

    public void setSchools(ArrayList<String> schools) {
        this.schools = schools;
    }

    public ArrayList<Work> getWork() {
        return work;
    }

    public void setWork(ArrayList<Work> work) {
        this.work = work;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
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

    public ArrayList<String> getAllInterests() {
        return allInterests;
    }

    public void setAllInterests(ArrayList<String> allInterests) {
        this.allInterests = allInterests;
    }

    public ArrayList<ConnectionObject> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<ConnectionObject> connections) {
        this.connections = connections;
    }

    public ArrayList<String> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(ArrayList<String> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public ArrayList<String> getSavedEvents() {
        return savedEvents;
    }

    public void setSavedEvents(ArrayList<String> savedEvents) {
        this.savedEvents = savedEvents;
    }

    public ArrayList<String> getProfilesSwipedMeRight() {
        return profilesSwipedMeRight;
    }

    public void setProfilesSwipedMeRight(ArrayList<String> profilesSwipedMeRight) {
        this.profilesSwipedMeRight = profilesSwipedMeRight;
    }

    public ArrayList<String> getProfilesIswipedRight() {
        return profilesIswipedRight;
    }

    public void setProfilesIswipedRight(ArrayList<String> profilesIswipedRight) {
        this.profilesIswipedRight = profilesIswipedRight;
    }
}
