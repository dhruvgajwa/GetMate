package com.getmate.demo181201.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.getmate.demo181201.R;
import com.google.firebase.Timestamp;

import java.util.Date;

public class Settings extends AppCompatActivity {
    Button editProfile;
    Switch ShowMeOnFindMate;
    CardView HelpAndSupport;
    TextView privacyPolicy;
    TextView TermsAndCondition;
    TextView WorkWithCohortso;
    TextView reportABug;
    TextView logout;
    TextView delete;


    Timestamp timestamp= new Timestamp(new Date());

    private void findViewsById() {
        editProfile = findViewById(R.id.edit_profile_s);
        ShowMeOnFindMate = findViewById(R.id.show_me_on_findmate_s);
        HelpAndSupport = findViewById(R.id.help_and_support);
        privacyPolicy = findViewById(R.id.privacy_policy);
        TermsAndCondition = findViewById(R.id.terms_and_condition);
        WorkWithCohortso= findViewById(R.id.work_with_cohortso);
        reportABug = findViewById(R.id.report_a_bug);
        logout = findViewById(R.id.logout_s);
        delete = findViewById(R.id.delete_account_s);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewsById();


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Settings.this,EditProfile.class);
                i.putExtra("fromProfileFragment",true);
                i.putExtra("dataFromProfileFragment",getIntent().getStringExtra("dataFromProfileFragment"));
                startActivity(i);
            }
        });


        ShowMeOnFindMate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (ShowMeOnFindMate.isChecked()){
                //Add me to FIndMate feature
            }
            else {
                //Don't add me to findMate feature
            }
            }
        });



        HelpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        WorkWithCohortso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        reportABug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       }



}
