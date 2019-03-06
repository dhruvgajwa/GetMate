package com.getmate.demo181201.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.getmate.demo181201.CurrentUserData;
import com.getmate.demo181201.Objects.Ticket;
import com.getmate.demo181201.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyTickets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets = CurrentUserData.getInstance().getCurrent().getMyTickets();

        TicketListAdapter adapter = new TicketListAdapter(getApplicationContext(),tickets);
        ListView listView = findViewById(R.id.my_tickets_list);
        listView.setAdapter(adapter);

    }

    public class TicketListAdapter extends BaseAdapter {
        private ImageView eventTimelineImageView;
        private TextView title;
        private TextView date;
        private Context context;
        private ArrayList<Ticket> tickets;
        public TicketListAdapter(Context context,ArrayList<Ticket> tickets){
            this.context = context;
            this.tickets = tickets;
            for (int i=0;i<10;i++){
                this.tickets.addAll(tickets);
            }
        }


        @Override
        public int getCount() {
            return tickets.size();
        }

        @Override
        public Object getItem(int i) {
            return tickets.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (layoutInflater!=null){
                view = layoutInflater.inflate(R.layout.saved_events_small,null);

            }


            final Ticket ticket = tickets.get(i);
            title =view.findViewById(R.id.title_ses);
            date = view.findViewById(R.id.date_ses);
            title.setText(ticket.geteTitle());
            eventTimelineImageView = view.findViewById(R.id.event_image_ses);
            CharSequence time = DateUtils.getRelativeTimeSpanString
                    (Long.parseLong(ticket.geteTimeStamp()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);
            date.setText(time);




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(TicketListAdapter.this.context,TicketActivity.class);
                    Gson gson = new Gson();
                    String  json = gson.toJson(ticket);
                    i.putExtra("ticket",json);
                    TicketListAdapter.this.context.startActivity(i);
                }
            });

            return view;
        }
    }

}
