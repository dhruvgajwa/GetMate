package com.getmate.demo181201.createEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.InterestSelection.RoundedImageView;
import com.getmate.demo181201.R;

import java.util.ArrayList;

public class SetTicketTypeAndPrice extends AppCompatActivity {
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ticket_type_and_price);
        intent = getIntent();
        Log.i("KaunHuMein","A4");
       ListView listView = findViewById(R.id.event_type_list_view);
        ArrayList<ETypeObject> allTypes= new ArrayList<>();
        allTypes = GetData();

        ListViewAdapter listViewAdapter= new ListViewAdapter(allTypes,SetTicketTypeAndPrice.this,intent);
        listView.setAdapter(listViewAdapter);

    }

    public ArrayList<ETypeObject> GetData(){
        ArrayList<ETypeObject> allTypes= new ArrayList<>();
        allTypes.add(new ETypeObject("Conference",0));
        allTypes.add(new ETypeObject("Seminar, Talk",0));
        allTypes.add(new ETypeObject("Tradeshow, Expo, Product Launch",0));
        allTypes.add(new ETypeObject("Convention",0));
        allTypes.add(new ETypeObject("Festival, Fair",0));
        allTypes.add(new ETypeObject("Concert, Performance",0));
        allTypes.add(new ETypeObject("Screening",0));
        allTypes.add(new ETypeObject("Diner, Gala",0));
        allTypes.add(new ETypeObject("Class, Training, Workshop",0));
        allTypes.add(new ETypeObject("Meeting, Networking event",0));
        allTypes.add(new ETypeObject("Party, Social Gathering",0));
        allTypes.add(new ETypeObject("Rally",0));
        allTypes.add(new ETypeObject("Tournament",0));
        allTypes.add(new ETypeObject("Game, Competition",0));
        allTypes.add(new ETypeObject("Race, Endurance Event",0));
        allTypes.add(new ETypeObject("Tour",0));
        allTypes.add(new ETypeObject("Camp, Trip",0));
        allTypes.add(new ETypeObject("Appearance, Singing",0));
        allTypes.add(new ETypeObject("Donation",0));
        allTypes.add(new ETypeObject("Other",0));
   return allTypes;
    }


}

class ETypeObject {
   public String name;
    public int imageId;

    public ETypeObject(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public ETypeObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}




 class ListViewAdapter extends BaseAdapter{

    Context mContext;
    ArrayList<ETypeObject> allTypes= new ArrayList<>();
    Intent intent;
    public ListViewAdapter(){}

    public ListViewAdapter(ArrayList<ETypeObject> AllTypes, Context context,Intent intent){
        mContext = context;
        allTypes = AllTypes;
        this.intent = intent;

    }



    @Override
    public int getCount() {
        return allTypes.size();
    }

    @Override
    public Object getItem(int i) {
        return allTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i1, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater!=null){
            view = layoutInflater.inflate(R.layout.parent_interest_with_child,null);

        }
        Log.i("KaunHuMein","A5");
        RoundedImageView icon;
        TextView type;


        icon =view.findViewById(R.id.parentinterest_rimage);
        type = view.findViewById(R.id.parentInteresttext);

        type.setText(allTypes.get(i1).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,allTypes.get(i1).getName(),Toast.LENGTH_LONG).show();


                Intent i = new Intent( ListViewAdapter.this.mContext,EventPrivacyAndTicketPrice.class);

                i.putExtra("description",intent.getStringExtra("description"));
                i.putExtra("title",intent.getStringExtra("title"));
                i.putExtra("from",intent.getLongExtra("from",0));
                i.putExtra("to",intent.getLongExtra("to",0));
                i.putStringArrayListExtra("interestB",intent.getStringArrayListExtra("interestB"));
                i.putStringArrayListExtra("interestI",intent.getStringArrayListExtra("interestI"));
                i.putStringArrayListExtra("interestE",intent.getStringArrayListExtra("interestE"));
                i.putStringArrayListExtra("AllParentInterests",intent.
                        getStringArrayListExtra("AllParentInterests"));
                i.putExtra("lat",intent.getDoubleExtra("lat",0.00));
                i.putExtra("lon",intent.getDoubleExtra("lon",0.00));
                i.putExtra("address",intent.getStringExtra("address"));
                i.putExtra("city",intent.getStringExtra("city"));
                i.putExtra("imageUri",intent.getStringExtra("imageUri"));
                i.putExtra("organisers",intent.getStringArrayListExtra("organisers"));
                i.putExtra("link",intent.getStringExtra("link"));
                i.putExtra("eventType",allTypes.get(i1).getName());
                ListViewAdapter.this.mContext.startActivity(i);
            }
        });
        return view;
    }
}
