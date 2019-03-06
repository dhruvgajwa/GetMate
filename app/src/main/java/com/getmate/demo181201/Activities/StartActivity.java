package com.getmate.demo181201.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.getmate.demo181201.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
        Button Login,Signup;
        FirebaseUser firebaseUser;
        ProgressDialog progressDialog;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser!=null){
            Intent i = new Intent(StartActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Login = findViewById(R.id.login);
        Signup = findViewById(R.id.signup);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this,com.getmate.demo181201.Activities.Login.class);
                startActivity(i);
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
