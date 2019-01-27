package com.getmate.demo181201.InterestSelection;
/*
* bugs needed to remove are
* 1.collapse all other group if another one is expanded
* 2.for child views, I am not getting the selectes state once I collapse and again expand the same group
* 3.the list is not expanding on first click but is expanding on the second click*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.demo181201.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.getmate.demo181201.InterestSelection.Constant.BEGINNER;
import static com.getmate.demo181201.InterestSelection.Constant.EXPERT_LEVEL;
import static com.getmate.demo181201.InterestSelection.Constant.INTERMEDIATE;
import static com.getmate.demo181201.InterestSelection.Constant.NONE;
import static com.getmate.demo181201.InterestSelection.Constant.SELECT_INTERESTS;

/**
 * Created by Dhruv on 6/4/2018.
 */

public class InterestAdapter extends BaseExpandableListAdapter {
    ArrayList<String> interestB = new ArrayList<>();
    ArrayList<String> interestI = new ArrayList<>();
    ArrayList<String> interestE = new ArrayList<>();


    ArrayList<ChildInterest> selected_Interest_List = new ArrayList<ChildInterest>();

    private Context context;
    private ArrayList<ParentInterest> interestArrayList,original;
    private LayoutInflater inflater;
    int groupPosition=0;

    public InterestAdapter(Context context, ArrayList<ParentInterest> parentInterests){
        this.context = context;
        this.interestArrayList = parentInterests;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public  class ViewHolder{
        RoundedImageView child_icon;
        CheckBox checkBox;
        TextView childText;
        LinearLayout childLinLay, RadioLinLay;
        RadioButton beginner,intermediate,expert;
        RadioGroup radioGroup;
    }



    @Override
    public int getGroupCount() {
        return interestArrayList.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return interestArrayList.get(groupPosition).childInterests.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return interestArrayList.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return interestArrayList.get(groupPosition).childInterests.get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, final View convertView, ViewGroup parent) {
        View rowView=convertView;

        if (getChildrenCount(groupPosition) != 0) {
            rowView = inflater.inflate(R.layout.parent_interest_with_child, null);
            //rowView.setId(R.id.parentHaveChildView);
            RoundedImageView ParentInterestImage =
                    (RoundedImageView) rowView.findViewById(R.id.parentinterest_rimage);
            TextView ParentInterestText = (TextView) rowView.findViewById(R.id.parentInteresttext);
            ParentInterest ParentInterest = (ParentInterest) getGroup(groupPosition);
            ParentInterestText.setText(ParentInterest.name);
            Bitmap icon = BitmapFactory.decodeResource(rowView.getResources(),
                    interestArrayList.get(groupPosition).getImageId());
            Bitmap roundedicon = RoundedImageView.getRoundedCroppedBitmap(icon, icon.getWidth());
            ParentInterestImage.setImageBitmap(roundedicon);
        }
        else {
            rowView = inflater.inflate(R.layout.parent_interest_without_child, null);
            //rowView.setId(R.id.parentHaveNochildView);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.child_icon = (RoundedImageView) rowView.findViewById(R.id.parentimage2);
            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkbox2);
            viewHolder.childText = (TextView) rowView.findViewById(R.id.parenttext2);
            viewHolder.childLinLay = (LinearLayout) rowView.findViewById(R.id.parentlinearlayout2);
            viewHolder.RadioLinLay = (LinearLayout) rowView.findViewById(R.id.levelpack2);
            viewHolder.expert = (RadioButton) rowView.findViewById(R.id.expert2);
            viewHolder.intermediate = (RadioButton) rowView.findViewById(R.id.intermediate2);
            viewHolder.beginner = (RadioButton) rowView.findViewById(R.id.beginner2);
            viewHolder.radioGroup = (RadioGroup) rowView.findViewById(R.id.radiogroup2);
            rowView.setTag(viewHolder);

            final ViewHolder holder = (ViewHolder) rowView.getTag();

            final ParentInterest parentInterest = (ParentInterest) getGroup(groupPosition);
            holder.childText.setText(parentInterest.getName());
            holder.checkBox.setChecked(parentInterest.getisState());



            if(interestArrayList.get(groupPosition).isState()){    //if the interest is already selected then
               holder.RadioLinLay.setVisibility(View.VISIBLE);
               Toast.makeText(context,"level is "+interestArrayList.get(groupPosition).getLevel(),Toast.LENGTH_SHORT).show();
                switch( interestArrayList.get(groupPosition).getLevel()){

                    case 0:{  holder.beginner.setChecked(false);
                        holder.intermediate.setChecked(false);
                        holder.expert.setChecked(false);

                    }
                    case 1:{
                        holder.beginner.setChecked(true);
                        holder.intermediate.setChecked(false);
                        holder.expert.setChecked(false);
                    }
                    case 2:{
                        holder.beginner.setChecked(false);
                        holder.intermediate.setChecked(true);
                        holder.expert.setChecked(false);
                    }
                    case 3:{
                        holder.beginner.setChecked(false);
                        holder.intermediate.setChecked(false);
                        holder.expert.setChecked(true);
                    }
                }

            }else {
                holder.RadioLinLay.setVisibility(View.GONE);
            }

  //set Constant.pstate = 0 whenever there is opnes state or Radiogroup is visible
            holder.childLinLay.setOnClickListener(new View.OnClickListener() {     //understand the first condition too
                @Override
                public void onClick(View v) {
                    if(Constant.pstate==0){ // when u click elsewhere then popup should show
                        Toast.makeText(context,"Click in checkBox to select another interest \n Or Select any of orange button to finalize",Toast.LENGTH_LONG).show();
                        //set popup window here
                    }

                    else if (!holder.checkBox.isChecked() && !interestArrayList.get(groupPosition).getisState()){      //double checked

                        holder.RadioLinLay.setVisibility(View.VISIBLE);
                        holder.checkBox.setChecked(false);

                        interestArrayList.get(groupPosition).setState(true);

                    }
                    else if(holder.checkBox.isChecked()&&interestArrayList.get(groupPosition).getisState()){
                        holder.RadioLinLay.setVisibility(View.GONE);
                        interestArrayList.get(groupPosition).setState(false);

                    }
                   else if(holder.checkBox.isChecked()&&!interestArrayList.get(groupPosition).getisState()){
                        holder.RadioLinLay.setVisibility(View.VISIBLE);
                        interestArrayList.get(groupPosition).setState(true);
                    }
                    else if(!holder.checkBox.isChecked()&&interestArrayList.get(groupPosition).getisState()){
                       holder.RadioLinLay.setVisibility(View.GONE);
                       interestArrayList.get(groupPosition).setState(false);
                    }


                }

            });

            holder.expert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.pstate =3;


                    interestArrayList.get(groupPosition).setLevel(EXPERT_LEVEL);
                    interestArrayList.get(groupPosition).setSelected(true);
                    interestArrayList.get(groupPosition).setState(true);
                    holder.checkBox.setChecked(true);
                    ParentInterest p = interestArrayList.get(groupPosition);
                    ChildInterest i = new ChildInterest(p.getName(),p.getisState(),p.getImageId());
                    i.setLevel(p.getLevel());
                    i.setGetParentPos(groupPosition);
                    i.setGetChildPos(-1);
                    SELECT_INTERESTS.add(i);
                    interestE.add(interestArrayList.get(groupPosition).getName());
                    //this SELECT_INTEREST is the final list of interest to be passed to [profile
                    selected_Interest_List.add(i);

                    Toast.makeText(context,"clicked on expert",Toast.LENGTH_SHORT).show();
                    holder.RadioLinLay.setClickable(false);
                    holder.RadioLinLay.setEnabled(false);
                    //holder.childLinLay.setClickable(false);
                    //holder.childLinLay.setEnabled(false);
                    //holder.checkBox.setClickable(false);
                   // holder.checkBox.setEnabled(false);
                    holder.radioGroup.setClickable(false);
                    holder.radioGroup.setEnabled(false);
                    holder.intermediate.setClickable(false);
                    holder.beginner.setEnabled(false);
                    holder.expert.setEnabled(false);
                    holder.intermediate.setEnabled(false);
                    holder.expert.setClickable(false);
                    holder.beginner.setClickable(false);
                }

            });
            holder.intermediate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.pstate =2;

                    interestArrayList.get(groupPosition).setLevel(INTERMEDIATE);
                    interestArrayList.get(groupPosition).setSelected(true);
                    interestArrayList.get(groupPosition).setState(true);
                    holder.checkBox.setChecked(true);
                    ParentInterest p = interestArrayList.get(groupPosition);
                    ChildInterest i = new ChildInterest(p.getName(),p.getisState(),p.getImageId());
                    i.setLevel(p.getLevel());
                    i.setGetParentPos(groupPosition);
                    i.setGetChildPos(-1);
                    SELECT_INTERESTS.add(i);
                    interestI.add(interestArrayList.get(groupPosition).getName());//this SELECT_INTEREST is the final list of interest to be passed to [profile
                    selected_Interest_List.add(i);
                    Toast.makeText(context,"clicked on intermediate",Toast.LENGTH_SHORT).show();
                    holder.RadioLinLay.setClickable(false);
                    holder.RadioLinLay.setEnabled(false);
                    //holder.childLinLay.setClickable(false);
                    //holder.childLinLay.setEnabled(false);
                    //holder.checkBox.setClickable(false);
                    //holder.checkBox.setEnabled(false);
                    holder.radioGroup.setClickable(false);
                    holder.radioGroup.setEnabled(false);
                    holder.intermediate.setClickable(false);
                    holder.beginner.setEnabled(false);
                    holder.expert.setEnabled(false);
                    holder.intermediate.setEnabled(false);
                    holder.expert.setClickable(false);
                    holder.beginner.setClickable(false);
                }

            });
            holder.beginner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.pstate =1;

                    interestArrayList.get(groupPosition).setLevel(BEGINNER);
                    interestArrayList.get(groupPosition).setSelected(true);
                    interestArrayList.get(groupPosition).setState(true);
                    holder.checkBox.setChecked(true);
                    ParentInterest p = interestArrayList.get(groupPosition);
                    ChildInterest i = new ChildInterest(p.getName(),p.getisState(),p.getImageId());
                    i.setLevel(p.getLevel());
                    i.setGetParentPos(groupPosition);
                    i.setGetChildPos(-1);
                    SELECT_INTERESTS.add(i);   //this SELECT_INTEREST is the final list of interest to be passed to [profile
                    interestB.add(interestArrayList.get(groupPosition).getName());
                    selected_Interest_List.add(i);
                    Toast.makeText(context,"clicked on beginner",Toast.LENGTH_SHORT).show();
                    holder.RadioLinLay.setClickable(false);
                    holder.RadioLinLay.setEnabled(false);
                    //holder.childLinLay.setClickable(false);
                    //holder.childLinLay.setEnabled(false);
                    //holder.checkBox.setClickable(false);
                    //holder.checkBox.setEnabled(false);
                    holder.radioGroup.setClickable(false);
                    holder.radioGroup.setEnabled(false);
                    holder.intermediate.setClickable(false);
                    holder.beginner.setEnabled(false);
                    holder.expert.setEnabled(false);
                    holder.intermediate.setEnabled(false);
                    holder.expert.setClickable(false);
                    holder.beginner.setClickable(false);
                }

            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if(isChecked){

                        if(!interestArrayList.get(groupPosition).isSelected()){

                            Toast.makeText(context,"U can't click here bro",Toast.LENGTH_SHORT).show();
                            holder.checkBox.setChecked(false);

                        }
                    }
                    else if(!isChecked) {

                         holder.RadioLinLay.setClickable(true);
                        holder.RadioLinLay.setEnabled(true);
                        holder.childLinLay.setClickable(true);
                        holder.childLinLay.setEnabled(true);
                        holder.checkBox.setClickable(true);
                        holder.checkBox.setEnabled(true);
                        holder.radioGroup.setClickable(true);
                        holder.radioGroup.setEnabled(true);
                        holder.intermediate.setClickable(true);
                        holder.beginner.setEnabled(true);
                        holder.expert.setEnabled(true);
                        holder.intermediate.setEnabled(true);
                        holder.expert.setClickable(true);
                        holder.beginner.setClickable(true);
                        interestArrayList.get(groupPosition).setSelected(false);
                        interestArrayList.get(groupPosition).setLevel(NONE);



                        ParentInterest p = interestArrayList.get(groupPosition);
                        ChildInterest q = new ChildInterest(p.getName(),p.getisState(),p.getImageId());
                        String name= p.getName();
                        q.setLevel(p.getLevel());
                        q.setGetParentPos(groupPosition);
                        q.setGetChildPos(-1);

                        removeInterest(name);

                        for(ChildInterest toberemoved: selected_Interest_List){
                            if(toberemoved.getName().equals(q.getName())){
                                selected_Interest_List.remove(toberemoved);
                                Toast.makeText(context,toberemoved.getName()+"is removed",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context,"APKA coe tatti hai ek dum",Toast.LENGTH_SHORT).show();
                            }
                        }



                        if(interestArrayList.get(groupPosition).getisState()){
                            holder.RadioLinLay.setVisibility(View.GONE);
                            interestArrayList.get(groupPosition).setState(false);
                        }
                    }





                     /*   if(holder.checkBox.isChecked()&&interestArrayList.get(groupPosition).isSelected()){
                        //if box is already checked and the system is selected then set setting to default
                        interestArrayList.get(groupPosition).setSelected(false);
                        holder.checkBox.setChecked(false);
                        interestArrayList.get(groupPosition).setLevel(0);

                             if(interestArrayList.get(groupPosition).getisState())
                             {
                               holder.RadioLinLay.setVisibility(View.GONE);
                               interestArrayList.get(groupPosition).setState(false);
                             }


                    }
                    */

                }
            });

            Bitmap icon = BitmapFactory.decodeResource(rowView.getResources(),interestArrayList.get(groupPosition).getImageId());
            Bitmap roundimageicon =RoundedImageView.getRoundedCroppedBitmap(icon,icon.getWidth());
            holder.child_icon.setImageBitmap(icon);




        }
        return rowView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, final View convertView, ViewGroup parent) {

        View rowView = convertView;
        rowView= inflater.inflate(R.layout.child_interest_view,null);
        ViewHolder v = new ViewHolder();
        v.child_icon = (RoundedImageView) rowView.findViewById(R.id.childimage);

        v.checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
        v.childText = (TextView) rowView.findViewById(R.id.childtext);
        v.childLinLay = (LinearLayout) rowView.findViewById(R.id.childlinearlayout);
        v.RadioLinLay = (LinearLayout) rowView.findViewById(R.id.levelpack);
        v.expert = (RadioButton) rowView.findViewById(R.id.expert);
        v.intermediate = (RadioButton) rowView.findViewById(R.id.intermediate);
        v.beginner = (RadioButton) rowView.findViewById(R.id.beginner);
        v.radioGroup = (RadioGroup) rowView.findViewById(R.id.radiogroup);
        rowView.setTag(v);

        final ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.childText.setText(interestArrayList.get(groupPosition).childInterests.get(childPosition).getName());
        holder.checkBox.setChecked(interestArrayList.get(groupPosition).childInterests.get(childPosition).getSelected());

        if(interestArrayList.get(groupPosition).childInterests.get(childPosition).getSelected()){
            holder.RadioLinLay.setClickable(false);
            holder.RadioLinLay.setEnabled(false);
            //holder.childLinLay.setClickable(false);
            //holder.childLinLay.setEnabled(false);
            //holder.checkBox.setClickable(false);
            //holder.checkBox.setEnabled(false);
            holder.radioGroup.setClickable(false);
            holder.radioGroup.setEnabled(false);
            holder.intermediate.setClickable(false);
            holder.beginner.setEnabled(false);
            holder.expert.setEnabled(false);
            holder.intermediate.setEnabled(false);
            holder.expert.setClickable(false);
            holder.beginner.setClickable(false);
        }
        else {
            holder.RadioLinLay.setClickable(true);
            holder.RadioLinLay.setEnabled(true);
            holder.childLinLay.setClickable(true);
            holder.childLinLay.setEnabled(true);
            holder.checkBox.setClickable(true);
            holder.checkBox.setEnabled(true);
            holder.radioGroup.setClickable(true);
            holder.radioGroup.setEnabled(true);
            holder.intermediate.setClickable(true);
            holder.beginner.setEnabled(true);
            holder.expert.setEnabled(true);
            holder.intermediate.setEnabled(true);
            holder.expert.setClickable(true);
            holder.beginner.setClickable(true);

        }

        if(interestArrayList.get(groupPosition).childInterests.get(childPosition).getState()){

// if state is true means the system is open
            holder.RadioLinLay.setVisibility(View.VISIBLE);
            switch(interestArrayList.get(groupPosition).childInterests.get(childPosition).getLevel()){

                case 1:{
                    holder.beginner.setChecked(true);
                    holder.intermediate.setChecked(false);
                    holder.expert.setChecked(false);
                }
                case 2:{
                    holder.beginner.setChecked(false);
                    holder.intermediate.setChecked(true);
                    holder.expert.setChecked(false);
                }
                case 3:{
                    holder.beginner.setChecked(false);
                    holder.intermediate.setChecked(false);
                    holder.expert.setChecked(true);
                }
            }

        }
        else {
            holder.RadioLinLay.setVisibility(View.GONE);
        }

        //why?

        holder.childLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.pstate==0){
                    //show popup here
                }
                else if (!holder.checkBox.isChecked() && !interestArrayList.get(groupPosition).childInterests.get(childPosition).getState()){      //double checked

                    holder.RadioLinLay.setVisibility(View.VISIBLE);
                    holder.checkBox.setChecked(false);

                    interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(true);//Constant.pstate=0;

                }
                else if(holder.checkBox.isChecked()&&interestArrayList.get(groupPosition).childInterests.get(childPosition).getState()){
                    holder.RadioLinLay.setVisibility(View.GONE);
                    interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(false);

                }
                else if(holder.checkBox.isChecked()&&!interestArrayList.get(groupPosition).childInterests.get(childPosition).getState()){
                    holder.RadioLinLay.setVisibility(View.VISIBLE);
                    interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(true);//Constant.pstate=0;
                }
                else if(!holder.checkBox.isChecked()&&interestArrayList.get(groupPosition).childInterests.get(childPosition).getState()){
                    holder.RadioLinLay.setVisibility(View.GONE);
                    interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(false);
                }
            }
        });


        holder.expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.pstate =3;

                interestArrayList.get(groupPosition).childInterests.get(childPosition).setLevel(EXPERT_LEVEL);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setSelected(true);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(true);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setGetParentPos(groupPosition);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setGetChildPos(childPosition);
                holder.checkBox.setChecked(true);
                interestE.add(interestArrayList.get(groupPosition).childInterests.get(childPosition).getName());


                SELECT_INTERESTS.add(interestArrayList.get(groupPosition).childInterests.get(childPosition));   //this SELECT_INTEREST is the final list of interest to be passed to [profile
                selected_Interest_List.add(interestArrayList.get(groupPosition).childInterests.get(childPosition));
                Toast.makeText(context,"clicked on expert",Toast.LENGTH_SHORT).show();
                holder.RadioLinLay.setClickable(false);
                holder.RadioLinLay.setEnabled(false);
                //holder.childLinLay.setClickable(false);
                //holder.childLinLay.setEnabled(false);
                //holder.checkBox.setClickable(false);
                // holder.checkBox.setEnabled(false);
                holder.radioGroup.setClickable(false);
                holder.radioGroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);

            }
        });

        holder.beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.pstate =1;
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setLevel(BEGINNER);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setSelected(true);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(true);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setGetParentPos(groupPosition);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setGetChildPos(childPosition);
                holder.checkBox.setChecked(true);


                interestB.add(interestArrayList.get(groupPosition).childInterests.get(childPosition).getName());
                SELECT_INTERESTS.add(interestArrayList.get(groupPosition).childInterests.get(childPosition));   //this SELECT_INTEREST is the final list of interest to be passed to [profile
                selected_Interest_List.add(interestArrayList.get(groupPosition).childInterests.get(childPosition));
                Toast.makeText(context,"clicked on beginner",Toast.LENGTH_SHORT).show();
                holder.RadioLinLay.setClickable(false);
                holder.RadioLinLay.setEnabled(false);
                //holder.childLinLay.setClickable(false);
                //holder.childLinLay.setEnabled(false);
                //holder.checkBox.setClickable(false);
                // holder.checkBox.setEnabled(false);
                holder.radioGroup.setClickable(false);
                holder.radioGroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);
            }
        });

        holder.intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.pstate=2;
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setLevel(INTERMEDIATE);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setSelected(true);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(true);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setGetParentPos(groupPosition);
                interestArrayList.get(groupPosition).childInterests.get(childPosition).setGetChildPos(childPosition);
                holder.checkBox.setChecked(true);
                interestI.add(interestArrayList.get(groupPosition).childInterests.get(childPosition).getName());


                SELECT_INTERESTS.add(interestArrayList.get(groupPosition).childInterests.get(childPosition));   //this SELECT_INTEREST is the final list of interest to be passed to [profile
                selected_Interest_List.add(interestArrayList.get(groupPosition).childInterests.get(childPosition));
                Toast.makeText(context,"clicked on intermediate",Toast.LENGTH_SHORT).show();
                holder.RadioLinLay.setClickable(false);
                holder.RadioLinLay.setEnabled(false);
                //holder.childLinLay.setClickable(false);
                //holder.childLinLay.setEnabled(false);
                //holder.checkBox.setClickable(false);
                // holder.checkBox.setEnabled(false);
                holder.radioGroup.setClickable(false);
                holder.radioGroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);}
        });



        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              //  if(Constant.pstate==0){
                //    Toast.makeText(context,"Close the previous one bruhh",Toast.LENGTH_SHORT).show();
                //}

                if(isChecked){

                    if(!interestArrayList.get(groupPosition).childInterests.get(childPosition).getSelected()){

                        Toast.makeText(context,"U can't click here bro",Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);

                    }
                }
                else if(!isChecked) {

                    holder.RadioLinLay.setClickable(true);
                    holder.RadioLinLay.setEnabled(true);
                    holder.childLinLay.setClickable(true);
                    holder.childLinLay.setEnabled(true);
                    holder.checkBox.setClickable(true);
                    holder.checkBox.setEnabled(true);
                    holder.radioGroup.setClickable(true);
                    holder.radioGroup.setEnabled(true);
                    holder.intermediate.setClickable(true);
                    holder.beginner.setEnabled(true);
                    holder.expert.setEnabled(true);
                    holder.intermediate.setEnabled(true);
                    holder.expert.setClickable(true);
                    holder.beginner.setClickable(true);
                    interestArrayList.get(groupPosition).childInterests.get(childPosition).setSelected(false);
                    interestArrayList.get(groupPosition).childInterests.get(childPosition).setLevel(NONE);

                    for(ChildInterest toberemoved: selected_Interest_List){
                         if(toberemoved ==interestArrayList.get(groupPosition).childInterests.get(childPosition)){
                             selected_Interest_List.remove(toberemoved);
                             removeInterest(toberemoved.getName());
                             Toast.makeText(context,toberemoved.getName()+"is removed",Toast.LENGTH_SHORT).show();
                         }
                         else {
                             Toast.makeText(context,"APKA coe tatti hai ek dum",Toast.LENGTH_SHORT).show();
                         }
                    }




                    if(interestArrayList.get(groupPosition).childInterests.get(childPosition).getState()){
                        holder.RadioLinLay.setVisibility(View.GONE);
                        Constant.pstate=1;
                        interestArrayList.get(groupPosition).childInterests.get(childPosition).setState(false);
                    }
                }

            }
        });


        Bitmap icon = BitmapFactory.decodeResource(rowView.getResources(), interestArrayList.get(groupPosition).childInterests.get(childPosition).getImageId());
        Bitmap roundedicon = RoundedImageView.getRoundedCroppedBitmap(icon, icon.getWidth());
        holder.child_icon.setImageBitmap(roundedicon);

        return rowView;
    }


    private void removeInterest(String name) {
        for (String toberemod:interestE) {
            if(toberemod.equals(name)){
                interestE.remove(name);
                return;
            }
        }
        for (String toberemod:interestI) {
            if(toberemod.equals(name)){
                interestI.remove(name);
                return;
            }
        }
        for (String toberemod:interestB) {
            if(toberemod.equals(name)){
                interestB.remove(name);
                return;
            }
        }



    }
    public HashMap<String,ArrayList<String>> getData(){
        HashMap<String,ArrayList<String>> data = new HashMap<>();
        data.put("interestB",interestB);
        data.put("interestI",interestI);
        data.put("interestE",interestE);
        return data;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
