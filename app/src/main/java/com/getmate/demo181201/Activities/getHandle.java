package com.getmate.demo181201.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class getHandle extends AppCompatActivity {
    private EditText fieldHandle;
    private FirebaseFirestore db;
    private boolean isHandleUnique= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_handle);
        db = FirebaseFirestore.getInstance();
        fieldHandle = findViewById(R.id.handle_agh);

        Button check = findViewById(R.id.done_agh);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String handle = fieldHandle.getText().toString();
                Log.i("KaunH","Handle is "+ handle);
                checkHandle(handle);
                }
        });


    }


    public void checkHandle(String handle){
        //run a querry that result in this data

        db.collection("profiles").whereEqualTo("handle",handle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                if (list.size()==0){
                    isHandleUnique = true;
                    Log.i("Kaun","The handle is unique");
                    Intent intent = new Intent();
                    intent.putExtra("handle",handle);
                    setResult(RESULT_OK, intent);
                    Log.d("getHandle","unique handle acquired");
                    finish();

                }
                else {
                    isHandleUnique = false;
                    Log.i("Kaun","The Handle already exists");
                    Toast.makeText(getApplicationContext(),"Handle already Exixits",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Kaun","Failed",e.getCause());
                Toast.makeText(getApplicationContext(),"Not Reachable",Toast.LENGTH_LONG).show();
                isHandleUnique= false;
            }
        });
    }




}
