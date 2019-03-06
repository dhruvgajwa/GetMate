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

public class getEventTitle extends AppCompatActivity {


    EditText title;
    Button next,previous;
    int textCount= 80;
    TextView textCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_event_title);


        title = findViewById(R.id.title_age);
        next = findViewById(R.id.next_age);
        previous = findViewById(R.id.previous_age);
        textCountView = findViewById(R.id.text_count);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textCountView.setText(String.valueOf(80- title.getText().toString().trim().length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getEventTitle.this,DescriptionActivity.class);
                i.putExtra("title",title.getText().toString().trim());
                Log.i("Dhruv","B1");
                startActivity(i);
            }
        });
    }
}
