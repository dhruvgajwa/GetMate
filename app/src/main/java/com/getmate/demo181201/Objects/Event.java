package com.getmate.demo181201.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable,Parcelable
{    private double ticketPrice=0.00;
     private double convenienceFees= 0.00;
     private Long timestamp =Long.valueOf(0);
    private String title = null;
    private String creatorName = null;
    private String imageUrl = null;
    private String creatorHandle = null;
    private String creatorEmail = null;
    private  String privacy = null;
    private String creatorPhoneNo = null;
    private double lat = 0;
    private String creatorProfilePic=null;
    private double lon = 0;
    private String city = null;
    private ArrayList<Organisers> organisers = new ArrayList<>();
    private String description = null;
    private String  Time = null;
    private String creatorFirebaseId= null;
    private String Address =null;
    private String firebaseId = null;
    private String url =null;
    private int savedCount =0;
    private int goingCount = 0;
    private ArrayList<String> goingProfile = null;
    private ArrayList<String> savedBy = null;
    private ArrayList<String> tags = null;
    private ArrayList<String> allParentTags = null;
    private  Long from=Long.valueOf(0);
    private Long to =Long.valueOf(0);
    private String ticketType=null;
    private  String eventType = null;
    private ArrayList<ConnectionObject> goingProfiles = new ArrayList<>();

    private String salesStartAt = "";
    private  String salesStopAt = "";
    private int maxNoOfTicket = 0;
    private  int ticketsLeft = 0;

    protected Event(Parcel in) {
        salesStartAt = in.readString();
        salesStopAt =in.readString();
        maxNoOfTicket = in.readInt();
        ticketsLeft = in.readInt();
        privacy = in.readString();
        to = in.readLong();
        from = in.readLong();
        ticketType= in.readString();
        timestamp = in.readLong();
        convenienceFees = in.readDouble();
        ticketPrice = in.readDouble();
        creatorEmail = in.readString();
        creatorHandle = in.readString();
        creatorPhoneNo = in.readString();
        title = in.readString();
        creatorName = in.readString();
        imageUrl = in.readString();
        lat = in.readDouble();
        creatorProfilePic = in.readString();
        lon = in.readDouble();
        city = in.readString();
        description = in.readString();
        Time = in.readString();
        creatorFirebaseId = in.readString();
        Address = in.readString();
        url = in.readString();
        savedCount = in.readInt();
        goingCount = in.readInt();
        goingProfile = in.createStringArrayList();
        savedBy = in.createStringArrayList();
        tags = in.createStringArrayList();
        firebaseId = in.readString();
        eventType = in.readString();
        allParentTags = in.createStringArrayList();}

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getSalesStartAt() {
        return salesStartAt;
    }

    public void setSalesStartAt(String salesStartAt) {
        this.salesStartAt = salesStartAt;
    }

    public String getSalesStopAt() {
        return salesStopAt;
    }

    public void setSalesStopAt(String salesStopAt) {
        this.salesStopAt = salesStopAt;
    }

    public int getMaxNoOfTicket() {
        return maxNoOfTicket;
    }

    public void setMaxNoOfTicket(int maxNoOfTicket) {
        this.maxNoOfTicket = maxNoOfTicket;
    }

    public int getTicketsLeft() {
        return ticketsLeft;
    }

    public void setTicketsLeft(int ticketsLeft) {
        this.ticketsLeft = ticketsLeft;
    }

    public ArrayList<Organisers> getOrganisers() {
        return organisers;
    }

    public void setOrganisers(ArrayList<Organisers> organisers) {
        this.organisers = organisers;
    }

    public Event() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public ArrayList<ConnectionObject> getGoingProfiles() {
        return goingProfiles;
    }
    public void setGoingProfiles(ArrayList<ConnectionObject> goingProfiles) {
        this.goingProfiles = goingProfiles;
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
        parcel.writeLong(to);
        parcel.writeString(salesStartAt);
        parcel.writeString(salesStopAt);
        parcel.writeInt(maxNoOfTicket);
        parcel.writeInt(ticketsLeft);
        parcel.writeString(privacy);
        parcel.writeLong(from);
        parcel.writeString(ticketType);
        parcel.writeLong(timestamp);
        parcel.writeDouble(convenienceFees);
        parcel.writeDouble(ticketPrice);
        parcel.writeString(title);
        parcel.writeString(creatorName);
        parcel.writeString(imageUrl);
        parcel.writeDouble(lat);
        parcel.writeString(creatorProfilePic);
        parcel.writeDouble(lon);
        parcel.writeString(eventType);
        parcel.writeString(city);
        parcel.writeString(description);
        parcel.writeString(Time);
        parcel.writeString(creatorFirebaseId);
        parcel.writeString(Address);
        parcel.writeString(url);
        parcel.writeInt(savedCount);
        parcel.writeInt(goingCount);
        parcel.writeStringList(goingProfile);
        parcel.writeStringList(savedBy);
        parcel.writeStringList(tags);
        parcel.writeString(creatorEmail);
        parcel.writeString(creatorHandle);
        parcel.writeString(creatorPhoneNo);
        parcel.writeStringList(allParentTags);
        parcel.writeString(firebaseId);
    }


    public static class Organisers implements Serializable{
        private String name = null;
        private String email =null;
        private String contactNumber= null;
        private String photoUrl =null;
        public Organisers(String name,String email,String phone){
            this.name =name;
            this.email=email;
            this.contactNumber=phone;
        }
        public Organisers(){

        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }
    }

    public String getTicketType() {
        return ticketType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getConvenienceFees() {
        return convenienceFees;
    }

    public void setConvenienceFees(double convenienceFees) {
        this.convenienceFees = convenienceFees;
    }

    public ArrayList<String> getAllParentTags() {
        return allParentTags;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public void setAllParentTags(ArrayList<String> allParentTags) {
        this.allParentTags = allParentTags;
    }

    public String getCreatorProfilePic() {
        return creatorProfilePic;
    }

    public void setCreatorProfilePic(String creatorProfilePic) {
        this.creatorProfilePic = creatorProfilePic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return Time;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCreatorFirebaseId() {
        return creatorFirebaseId;
    }

    public void setCreatorFirebaseId(String creatorFirebaseId) {
        this.creatorFirebaseId = creatorFirebaseId;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getCreatorHandle() {
        return creatorHandle;
    }

    public void setCreatorHandle(String creatorHandle) {
        this.creatorHandle = creatorHandle;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCreatorPhoneNo() {
        return creatorPhoneNo;
    }

    public void setCreatorPhoneNo(String creatorPhoneNo) {
        this.creatorPhoneNo = creatorPhoneNo;
    }

    public static Creator<Event> getCREATOR() {
        return CREATOR;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(int savedCount) {
        this.savedCount = savedCount;
    }

    public int getGoingCount() {
        return goingCount;
    }

    public void setGoingCount(int goingCount) {
        this.goingCount = goingCount;
    }

    public ArrayList<String> getGoingProfile() {
        return goingProfile;
    }

    public void setGoingProfile(ArrayList<String> goingProfile) {
        this.goingProfile = goingProfile;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getSavedBy() {
        return savedBy;
    }

    public void setSavedBy(ArrayList<String> savedBy) {
        this.savedBy = savedBy;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }


}
