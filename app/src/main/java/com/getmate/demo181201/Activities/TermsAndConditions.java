package com.getmate.demo181201.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.getmate.demo181201.R;

public class TermsAndConditions extends AppCompatActivity {
CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        checkBox = findViewById(R.id.t_n_c_checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    Intent i = new Intent();
                    setResult(RESULT_OK,i);
                    finish();
                }
            }
        });

    }
}
