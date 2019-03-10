package com.getmate.demo181201.createEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getmate.demo181201.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TicketSetting extends AppCompatActivity {
    RelativeLayout fromDate,fromTime,toDate,toTime;
    Calendar from = Calendar.getInstance();
    Calendar to = Calendar.getInstance();
    int mYear,mMonth,mDay,mHour,mMinute;
    RelativeLayout fromR, toR;
    int fYear,fMonth,fDate,fHour,fMin;
    int tYear,tMonth,tDate,tHour,tMin;
    TextView fromText,toText;
    Button next,previous;
    MaterialEditText noOfTickets;
    boolean isTo=false,isFrom=false;
Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_setting);
        fromDate = findViewById(R.id.from_date);
        fromTime = findViewById(R.id.from_time);
        toDate =findViewById(R.id.to_date);
        toTime = findViewById(R.id.to_time);
        fromR = findViewById(R.id.d_from);
        toR = findViewById(R.id.d_to);
        fromText = findViewById(R.id.from_text);
        toText = findViewById(R.id.to_text);
        noOfTickets = findViewById(R.id.no_of_tickets);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);


        //Setting current date and time
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour =c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        intent = getIntent();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(TicketSetting.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                from.set(year,month,dayOfMonth);
                                fYear = year;
                                fMonth = month;
                                fDate = dayOfMonth;
                                TimePickerDialog timePickerDialog =
                                        new TimePickerDialog(TicketSetting.this, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                fHour = hourOfDay;
                                                fMin = minute;

                                                from.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                                from.set(Calendar.MINUTE,minute);
                                                fromR.setVisibility(View.VISIBLE);
                                                fromText.setText(simpleDateFormat.format(from.getTime())+" "+String.valueOf(from.
                                                        get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(from.get(Calendar.MINUTE)));
                                                isFrom =true;
                                            }
                                        },mHour,mMinute,false);
                                timePickerDialog.show();
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
                        new TimePickerDialog(TicketSetting.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                fHour = hourOfDay;
                                fMin = minute;

                                from.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                from.set(Calendar.MINUTE,minute);
                                fromR.setVisibility(View.VISIBLE);
                                fromText.setText(simpleDateFormat.format(from.getTime())+" "+String.valueOf(from.
                                        get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(from.get(Calendar.MINUTE)));
                                isFrom= true;
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(TicketSetting.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                to.set(year,month,dayOfMonth);
                                tYear = year;
                                tMonth = month;
                                tDate = dayOfMonth;
                                TimePickerDialog timePickerDialog =
                                        new TimePickerDialog(TicketSetting.this, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                tHour = hourOfDay;
                                                tMin = minute;
                                                to.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                                to.set(Calendar.MINUTE,minute);
                                                toR.setVisibility(View.VISIBLE);

                                                toText.setText(simpleDateFormat.format(to.getTime())+" "+String.valueOf(to.
                                                        get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(to.get(Calendar.MINUTE)));
                                                isTo=true;
                                            }
                                        },mHour,mMinute,false);
                                timePickerDialog.show();
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
                        new TimePickerDialog(TicketSetting.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tHour = hourOfDay;
                                tMin = minute;
                                to.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                to.set(Calendar.MINUTE,minute);
                                toR.setVisibility(View.VISIBLE);

                                toText.setText(simpleDateFormat.format(to.getTime())+" "+String.valueOf(to.
                                        get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(to.get(Calendar.MINUTE)));
                                isTo=true;
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (noOfTickets.getText().toString().trim().isEmpty()){


                    //Tickets Not selected
                    Toast.makeText(getApplicationContext(),"No of tickets is not seleced",Toast.LENGTH_LONG).show();
                }
                else  if (Integer.valueOf(noOfTickets.getText().toString().trim())==0){
                    Toast.makeText(getApplicationContext(),"No of tickets is not seleced",Toast.LENGTH_LONG).show();

                }
                else if(from.getTimeInMillis()>to.getTimeInMillis()){
                    //Ticket sales start date is after stops date
                    Toast.makeText(getApplicationContext(),"Ticket sales start date is after stops date",Toast.LENGTH_LONG).show();

                }
                else if (to.getTimeInMillis()>getIntent().getLongExtra("to",0)){
                    // To date is after the event; not possible
                    Toast.makeText(getApplicationContext(),"Ticket sales start date is after event stops",Toast.LENGTH_LONG).show();

                }
                else if(!(isFrom && isTo)){
                    //if both are true the pass
                    Toast.makeText(getApplicationContext(),"Date not set",Toast.LENGTH_LONG).show();

                }

                else {
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                    Intent i = new Intent( TicketSetting.this,PreviewEventActivity.class);
                    i.putExtra("description",intent.getStringExtra("description"));
                    i.putExtra("title",intent.getStringExtra("title"));
                    i.putExtra("from",intent.getLongExtra("from",0));
                    i.putExtra("to",intent.getLongExtra("to",0));
                    i.putStringArrayListExtra("interestB",intent.getStringArrayListExtra("interestB"));
                    i.putStringArrayListExtra("interestI",intent.getStringArrayListExtra("interestI"));
                    i.putStringArrayListExtra("interestE",intent.getStringArrayListExtra("interestE"));
                    i.putStringArrayListExtra("AllParentInterests",intent.
                            getStringArrayListExtra("AllParentInterests"));
                    i.putExtra("lat",intent.getDoubleExtra("lat",0.00));
                    i.putExtra("lon",intent.getDoubleExtra("lon",0.00));
                    i.putExtra("address",intent.getStringExtra("address"));
                    i.putExtra("city",intent.getStringExtra("city"));
                    i.putExtra("imageUri",intent.getStringExtra("imageUri"));
                    i.putExtra("organisers",intent.getStringArrayListExtra("organisers"));
                    i.putExtra("link",intent.getStringExtra("link"));
                    i.putExtra("eventType",intent.getStringExtra("eventType"));
                    i.putExtra("privacy",intent.getStringExtra("privacy"));
                    i.putExtra("ticketPrice",intent.getStringExtra("ticketPrice"));
                    i.putExtra("ticketType",intent.getStringExtra("ticketType"));
                    i.putExtra("salesStartfrom",String.valueOf(from.getTimeInMillis()));
                    i.putExtra("salesStopAt",String.valueOf(to.getTimeInMillis()));
                    i.putExtra("maxNoOfTickets",Integer.valueOf(noOfTickets.getText().toString().trim()));
                    startActivity(i);

                }
            }
        });
    }
}
