package com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.R;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.CustomAdapter;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.InterviewAnnouncementData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewAnnouncementsTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewAnnouncementsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAnnouncementsTab extends Fragment {

    private DatabaseReference databaseReference;
    ArrayList<InterviewAnnouncementData> announcementDataList;
    InterviewAnnouncementData interviewAnnouncementData;
    private ListView listView;
    CustomAdapter customAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewAnnouncementsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAnnouncementsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAnnouncementsTab newInstance(String param1, String param2) {
        ViewAnnouncementsTab fragment = new ViewAnnouncementsTab();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_announcements_tab, container, false);

        announcementDataList = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listView);
        extractAnnouncementsData();
        checkConnection();

        return view;
    }

    private void extractAnnouncementsData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("InterviewAnnouncements");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, String> announcementData = (Map<String, String>) ds.getValue();

                    String unitCode = announcementData.get("unitcode");
                    String unitGrade = announcementData.get("unitGrade");
                    String date = announcementData.get("date");
                    String timings = announcementData.get("timings");
                    String room = announcementData.get("room");
                    String announcement = announcementData.get("announcement");


                    interviewAnnouncementData = new InterviewAnnouncementData(unitCode, unitGrade, date, timings, room, announcement);
                    interviewAnnouncementData.setUnitcode(unitCode);
                    interviewAnnouncementData.setUnitGrade(unitGrade);
                    interviewAnnouncementData.setTimings(timings);
                    interviewAnnouncementData.setDate(date);
                    interviewAnnouncementData.setAnnouncement(announcement);

                    announcementDataList.add(interviewAnnouncementData);
                    customListView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void customListView() {
        if(getContext() != null) {
            customAdapter = new CustomAdapter(getContext(), R.layout.activity_interview_announcementslist, announcementDataList);
            listView.setAdapter(customAdapter);
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if(activeNetwork.isConnectedOrConnecting()) {
                //Toast.makeText(MainActivity.this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                if(activeNetwork.isConnectedOrConnecting()) {
                    return true;
                }
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if(activeNetwork.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkConnection(){
        if(!checkNetworkConnection()){
            Toast.makeText(getContext(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
