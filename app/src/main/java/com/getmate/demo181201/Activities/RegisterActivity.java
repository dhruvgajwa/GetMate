package com.getmate.demo181201.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {
        MaterialEditText email,password,cPassword;
        Button registerBtn;
        FirebaseAuth auth;
        DatabaseReference reference;
        ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cPassword = findViewById(R.id.password_confirm);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.btn_register);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        auth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirm_password = cPassword.getText().toString().trim();
                String txt_password = password.getText().toString().trim();
                String txt_email = email.getText().toString().trim();
                if (TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)||(!confirm_password.equals(txt_password))){
                    Toast.makeText(getApplicationContext(),"Fields are required",Toast.LENGTH_LONG).show();

                }
                else if (txt_password.length()<6){
                    Toast.makeText(getApplicationContext(),"Password is very small",Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog.show();
                    register(txt_email,txt_password);
                }

            }
        });




    }

    private void register( String email, String password){

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                Intent intent = new Intent(RegisterActivity.this,EditProfile.class);
                intent.putExtra("fromRegisterActivity",true);
                intent.putExtra("email",email);
                startActivity(intent);
                finish();

                    /*FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageUrl","default");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this,EditProfile.class);
                                intent.putExtra("fromRegisterActivity",true);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this,EditProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Intent intent = new Intent(RegisterActivity.this,EditProfile.class);
                            Log.i("Kaun","failed to upload second set Of data");
                            intent.putExtra("fromRegisterActivity",true);
                            startActivity(intent);
                            finish();
                        }
                    });*/

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Cannot register with this email Id and password"
                ,Toast.LENGTH_LONG).show();
            }
        });
    }
}
