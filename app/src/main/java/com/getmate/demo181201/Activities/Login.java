package com.getmate.demo181201.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {

    MaterialEditText email, password;
    Button loginBtn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_l);
        password = findViewById(R.id.password_l);
        loginBtn = findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(Login.this);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_password = password.getText().toString();
                String txt_email = email.getText().toString();
                if (TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)||txt_password.length()<6){
                    Toast.makeText(getApplicationContext(),"Fields are required",Toast.LENGTH_LONG).show();
                    password.setError("Length should be greater than 6");
                }
                else{
                    progressDialog.show();
                   auth.signInWithEmailAndPassword(txt_email,txt_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {
                           progressDialog.dismiss();
                           Intent i = new Intent(Login.this,MainActivity.class);
                           i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(i);
                           finish();
                       }
                   });
                }

            }
        });
    }
}
