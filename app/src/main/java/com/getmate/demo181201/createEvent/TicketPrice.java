package com.getmate.demo181201.createEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class TicketPrice extends AppCompatActivity {
    MaterialEditText price;
    Button next, previous;
    TextView finalPrice;
    Integer ticketP =0;
    Double finalP =0.00;
    Double ConvenienceFees=30.40;
    Intent intent;
    CheckBox paid,free,paid2,free2,noTicketRequired,noTicketRequired2;
    String TicketType =null;
    LinearLayout getTicketPriceLinLay,checkBoxLinLay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_price);
        intent = getIntent();
        price = findViewById(R.id.price);
        finalPrice = findViewById(R.id.final_price);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        free = findViewById(R.id.free);
        paid = findViewById(R.id.paid);
        free2 = findViewById(R.id.free2);
        paid2 = findViewById(R.id.paid2);
        getTicketPriceLinLay = findViewById(R.id.lay_2);
        checkBoxLinLay = findViewById(R.id.lay_1);
        noTicketRequired = findViewById(R.id.no_ticket_reqired);
        noTicketRequired2 = findViewById(R.id.no_ticket_reqired2);

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paid.setChecked(true);
                free.setChecked(false);
                paid2.setChecked(true);
                free2.setChecked(false);
                noTicketRequired.setChecked(false);
                noTicketRequired2.setChecked(false);
                TicketType = "paid";
                getTicketPriceLinLay.setVisibility(View.VISIBLE);
                checkBoxLinLay.setVisibility(View.GONE);

            }
        });

        paid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paid.setChecked(true);
                free.setChecked(false);
                paid2.setChecked(true);
                free2.setChecked(false);
                noTicketRequired.setChecked(false);
                noTicketRequired2.setChecked(false);
                TicketType = "paid";
                getTicketPriceLinLay.setVisibility(View.VISIBLE);
                checkBoxLinLay.setVisibility(View.GONE);

            }
        });

        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                free.setChecked(true);
                paid.setChecked(false);
                free2.setChecked(true);
                paid2.setChecked(false);
                noTicketRequired.setChecked(false);
                noTicketRequired2.setChecked(false);
                TicketType = "free";
                getTicketPriceLinLay.setVisibility(View.GONE);
                checkBoxLinLay.setVisibility(View.VISIBLE);
            }
        });

        free2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                free.setChecked(true);
                paid.setChecked(false);
                free2.setChecked(true);
                paid2.setChecked(false);
                noTicketRequired.setChecked(false);
                noTicketRequired2.setChecked(false);
                TicketType = "free";
                getTicketPriceLinLay.setVisibility(View.GONE);
                checkBoxLinLay.setVisibility(View.VISIBLE);
            }
        });

        noTicketRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                free.setChecked(false);
                paid.setChecked(false);
                free2.setChecked(false);
                paid2.setChecked(false);
                noTicketRequired.setChecked(true);
                noTicketRequired2.setChecked(true);
                TicketType= "notRequired";
                getTicketPriceLinLay.setVisibility(View.GONE);
                checkBoxLinLay.setVisibility(View.VISIBLE);
            }
        });


        noTicketRequired2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                free.setChecked(false);
                paid.setChecked(false);
                free2.setChecked(false);
                paid2.setChecked(false);
                noTicketRequired.setChecked(true);
                noTicketRequired2.setChecked(true);
                TicketType = "notRequired";
                getTicketPriceLinLay.setVisibility(View.GONE);
                checkBoxLinLay.setVisibility(View.VISIBLE);
            }
        });






        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String p= price.getText().toString().trim();
                if (p.length()>0){



                    try {
                        ticketP = Integer.valueOf(p);
                    } catch (NumberFormatException e) {
                        ticketP = 0; // your default value
                    }
                    finalP = ticketP+ConvenienceFees;
                    String r = "INR ".concat(String.valueOf(finalP));
                    finalPrice.setText(r);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paid.isChecked() || free.isChecked()){
                    Intent i = new Intent( TicketPrice.this,TicketSetting.class);
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
                    i.putExtra("ticketPrice",String.valueOf(ticketP));
                    i.putExtra("ticketType",TicketType);
                    startActivity(i);
                    }
                else if (noTicketRequired.isChecked()){
                    Intent i = new Intent( TicketPrice.this,PreviewEventActivity.class);
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
                    i.putExtra("ticketPrice",String.valueOf(ticketP));
                    i.putExtra("ticketType",TicketType);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Choose One",Toast.LENGTH_LONG).show();
                }
                }
        });



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
