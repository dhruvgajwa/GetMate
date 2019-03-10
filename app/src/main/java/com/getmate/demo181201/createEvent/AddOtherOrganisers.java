package com.getmate.demo181201.createEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddOtherOrganisers extends AppCompatActivity {
    //create a new Linear Layout
    LinearLayout[] linearLayouts = new LinearLayout[3];
    EditText[] nameOrgs = new EditText[3];
    EditText[] emailOrgs = new EditText[3];
    EditText[] phoneOrgs = new EditText[3];
    LinearLayout mainLin;
    Button orgButton;
    Button next;
    int flag =0;
    ScrollView scrollView;
    EditText link;
    ArrayList<String> organisers = new ArrayList<>();
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_creators);

        next = findViewById(R.id.next);
        orgButton = findViewById(R.id.organisersButton);
        mainLin = findViewById(R.id.orgMainLinLay);
        link = findViewById(R.id.title_age);

        for (int i=0;i<3;i++){
            linearLayouts[i] = new LinearLayout(this);
            linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
            nameOrgs[i] = new EditText(this);
            emailOrgs[i] = new EditText(this);
            phoneOrgs[i]= new EditText(this);
            nameOrgs[i].setHint("Name");
            emailOrgs[i].setHint("email");
            phoneOrgs[i].setHint("phone Number");

            nameOrgs[i].setId(EditText.generateViewId());
            linearLayouts[i].addView(nameOrgs[i]);
            linearLayouts[i].addView(emailOrgs[i]);
            linearLayouts[i].addView(phoneOrgs[i]);
            linearLayouts[i].setBackground(getResources().getDrawable(R.drawable.profile_background));
            linearLayouts[i].setPadding(8,8,8,8);

        }

        orgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag<3){
                    mainLin.addView(linearLayouts[flag]);
                    flag++;
                }
                else {
                    Toast.makeText(getApplicationContext(),"Only three organisers allowed",Toast.LENGTH_LONG).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag>0){
                    for (int i=0;i<flag;i++){
                        Event.Organisers organisers1 = new Event.Organisers(nameOrgs[i].getText().toString().trim(),
                                emailOrgs[i].getText().toString().trim(),
                                phoneOrgs[i].getText().toString().trim());
                        String o = gson.toJson(organisers1);
                        organisers.add(o);
                    }
                }

                Log.i("KaunHuMein","A1");

                Intent i = new Intent( AddOtherOrganisers.this,SetTicketTypeAndPrice.class);

              i.putExtra("description",getIntent().getStringExtra("description"));
                i.putExtra("title",getIntent().getStringExtra("title"));
                i.putExtra("from",getIntent().getLongExtra("from",0));
                i.putExtra("to",getIntent().getLongExtra("to",0));
                i.putStringArrayListExtra("interestB",getIntent().getStringArrayListExtra("interestB"));
                i.putStringArrayListExtra("interestI",getIntent().getStringArrayListExtra("interestI"));
                i.putStringArrayListExtra("interestE",getIntent().getStringArrayListExtra("interestE"));
                i.putStringArrayListExtra("AllParentInterests",getIntent().
                        getStringArrayListExtra("AllParentInterests"));
                i.putExtra("lat",getIntent().getDoubleExtra("lat",0.00));
                i.putExtra("lon",getIntent().getDoubleExtra("lon",0.00));
                i.putExtra("address",getIntent().getStringExtra("address"));
                i.putExtra("city",getIntent().getStringExtra("city"));
                i.putExtra("imageUri",getIntent().getStringExtra("imageUri"));
                String linkText = link.getText().toString().trim();
                if (link.getText().toString().trim().isEmpty()){
                    linkText=null;
                    Log.i("KaunHuMein","A2");
                }
                Log.i("KaunHuMein","A3");
                i.putExtra("link",linkText);
                if (flag==0){
                    organisers=null;
                    i.putStringArrayListExtra("organisers",organisers);
                }
                else {
                    i.putStringArrayListExtra("organisers",organisers);
                }

                startActivity(i);


            }
        });
    }
}
