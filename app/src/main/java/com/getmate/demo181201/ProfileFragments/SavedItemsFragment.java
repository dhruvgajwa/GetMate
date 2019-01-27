 package com.getmate.demo181201.ProfileFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.getmate.demo181201.EventListAdapter;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

 /**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavedItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavedItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> savedItems = new ArrayList<>();
     FirebaseFirestore db;
     ArrayList<Event> events = new ArrayList<>();
     ListView listView;

    private OnFragmentInteractionListener mListener;

    public SavedItemsFragment() {
        // Required empty public constructor
    }
    public SavedItemsFragment(ArrayList<String> savedItems){
        this.savedItems = savedItems;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedItemsFragment newInstance(String param1, String param2) {
        SavedItemsFragment fragment = new SavedItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_saved_items, container, false);


        db= FirebaseFirestore.getInstance();
        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(),events);
        listView = view.findViewById(R.id.saved_items_list_view);
        listView.setAdapter(eventListAdapter);


        if (savedItems!=null){
            for (String s:savedItems) {
                db.collection("events").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Event event = new Event();
                            event = task.getResult().toObject(Event.class);
                            events.add(event);
                            eventListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }

        else {
            Toast.makeText(getActivity(),"No saved Items",Toast.LENGTH_LONG).show();
        }











        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
