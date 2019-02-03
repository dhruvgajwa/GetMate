package com.getmate.demo181201.ProfileFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.getmate.demo181201.Adapters.ConnectionsAdapter;
import com.getmate.demo181201.Objects.ConnectionObject;
import com.getmate.demo181201.R;

import java.util.ArrayList;


public class ConnectionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
   ListView listView;
    ConnectionsAdapter connectionsAdapter;

    // TODO: Rename and change types of parameters
    private ArrayList<ConnectionObject> connections= new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public ConnectionsFragment() {
        // Required empty public constructor
    }

    public ConnectionsFragment(ArrayList<ConnectionObject> connections){
        this.connections = connections;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_connections, container, false);
        listView = view.findViewById(R.id.connections_listview);
        connectionsAdapter = new ConnectionsAdapter(getActivity(),connections);
        listView.setAdapter(connectionsAdapter);

    return view;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
