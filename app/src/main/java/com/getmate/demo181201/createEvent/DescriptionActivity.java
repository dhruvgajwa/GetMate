package com.getmate.demo181201.createEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.getmate.demo181201.R;

public class DescriptionActivity extends AppCompatActivity {
    EditText description;
    Button next,previous;

    TextView textCountView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        description = findViewById(R.id.description_ad);
        next = findViewById(R.id.next_age);
        previous = findViewById(R.id.previous_age);
        textCountView = findViewById(R.id.text_count);


        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textCountView.setText(String.valueOf(description.getText().toString().trim().length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DescriptionActivity.this,DateActivity.class);
                i.putExtra("description",description.getText().toString().trim());
                i.putExtra("title",getIntent().getStringExtra("title"));
                Log.i("Dhruv","B2"+description.getText().toString().trim()+getIntent().getStringExtra("title"));
                startActivity(i);
            }
        });
    }
    }

