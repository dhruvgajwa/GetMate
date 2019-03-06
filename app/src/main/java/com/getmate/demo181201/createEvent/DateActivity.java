package com.getmate.demo181201.createEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getmate.demo181201.InterestSelection.InterestSelectionActivity;
import com.getmate.demo181201.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateActivity extends AppCompatActivity {
RelativeLayout fromDate,fromTime,toDate,toTime;
    int mYear,mMonth,mDay,mHour,mMinute;

    Calendar from = Calendar.getInstance();
    Calendar to = Calendar.getInstance();
    RelativeLayout fromR, toR;
    int fYear,fMonth,fDate,fHour,fMin;
    int tYear,tMonth,tDate,tHour,tMin;
    TextView fromText,toText;
    Button next,previous;


    Boolean isFT=false,isFD=false,isTD=false,isTT=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        fromDate = findViewById(R.id.from_date);
        fromTime = findViewById(R.id.from_time);
        toDate =findViewById(R.id.to_date);
        toTime = findViewById(R.id.to_time);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        fromR = findViewById(R.id.d_from);
        toR = findViewById(R.id.d_to);
        fromText = findViewById(R.id.from_text);
        toText = findViewById(R.id.to_text);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour =c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(DateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                from.set(year,month,dayOfMonth);
                                fYear = year;
                                fMonth = month;
                                fDate = dayOfMonth;
                                isFD = true;
                            }

                        },mYear,mMonth,mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(DateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                fHour = hourOfDay;
                                fMin = minute;
                                isFT= true;
                                from.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                from.set(Calendar.MINUTE,minute);
                                fromR.setVisibility(View.VISIBLE);
                                 fromText.setText(simpleDateFormat.format(from.getTime())+" "+String.valueOf(from.
                                         get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(from.get(Calendar.MINUTE)));
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });







        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(DateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                to.set(year,month,dayOfMonth);
                                tYear = year;
                                tMonth = month;
                                tDate = dayOfMonth;
                                isTD = true;
                            }

                        },mYear,mMonth,mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(DateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tHour = hourOfDay;
                                tMin = minute;
                                to.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                to.set(Calendar.MINUTE,minute);
                                isTT = true;
                                toR.setVisibility(View.VISIBLE);

                                toText.setText(simpleDateFormat.format(to.getTime())+" "+String.valueOf(to.
                                        get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(to.get(Calendar.MINUTE)));
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFD && isFT && isTD && isTT){

                    if(to.getTimeInMillis()>from.getTimeInMillis()){
                        Intent i = new Intent(DateActivity.this, InterestSelectionActivity.class);
                        i.putExtra("fromDateActivity",true);
                        i.putExtra("description",getIntent().getStringExtra("description"));
                        i.putExtra("title",getIntent().getStringExtra("title"));
                        i.putExtra("from",from.getTimeInMillis());
                        i.putExtra("to",to.getTimeInMillis());
                        Log.i("Dhruv","B3"+String.valueOf(from.getTimeInMillis())+" "
                                +String.valueOf(to.getTimeInMillis()));
                        Log.i("Dhruv",getIntent().getStringExtra("description")+
                                getIntent().getStringExtra("title"));

                        startActivity(i);
                    }

                    else {
                        Toast.makeText(getApplicationContext(),"TO Date should be After From Date",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
