package com.getmate.demo181201.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getmate.demo181201.MessageActivity;
import com.getmate.demo181201.Model.User;
import com.getmate.demo181201.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        private Context mContext;
        private List<User> mUsers;


        public UserAdapter(Context context, List<User> users){
            this.mContext = context;
            this.mUsers = users;
        }






    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final User user = mUsers.get(position);
    holder.username.setText(user.getUsername());
    if (user.getImageUrl().equals("default")){
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);
    }
    else {
        //Load Profile Image here
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext,MessageActivity.class);
            intent.putExtra("userid",user.getId());
            mContext.startActivity(intent);
        }
    });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;



        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_name);
            profile_image = itemView.findViewById(R.id.profile_image);

        }


    }




}
