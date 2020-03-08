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
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.CustomArrayAdapter;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.StudentBookingsData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * {@link StudentBookingsTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentBookingsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentBookingsTab extends Fragment {

    private DatabaseReference databaseReference;
    private ArrayList<StudentBookingsData> bookingsDataList;
    private StudentBookingsData studentBookingsData;
    private ListView listView;
    private CustomArrayAdapter customArrayAdapter;
    private FirebaseUser user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StudentBookingsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentBookingsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentBookingsTab newInstance(String param1, String param2) {
        StudentBookingsTab fragment = new StudentBookingsTab();
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

        bookingsDataList = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        extractBookingsData();
        checkConnection();

        return view;
    }

    private void extractBookingsData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("StudentBookings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, String> announcementData = (Map<String, String>) ds.getValue();

                    String loggedEmail = user.getEmail();
                    String contactEmail = announcementData.get("contactEmail");
                    assert loggedEmail != null;
                    if(loggedEmail.matches(contactEmail)) {
                        String studentName = announcementData.get("studentName");
                        String studentNumber = announcementData.get("studentNumber");
                        String workDescription = announcementData.get("workDescription");
                        String interviewTimings = announcementData.get("interviewTimings");

                        studentBookingsData = new StudentBookingsData(studentName, studentNumber, contactEmail, workDescription, interviewTimings);
                        studentBookingsData.setStudentName(studentName);
                        studentBookingsData.setStudentNumber(studentNumber);
                        studentBookingsData.setContactEmail(contactEmail);
                        studentBookingsData.setWorkDescription(workDescription);

                        bookingsDataList.add(studentBookingsData);
                    }
                }
                customListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void customListView() {
        if(getContext() != null) {
            customArrayAdapter = new CustomArrayAdapter(getContext(), R.layout.activity_interview_bookingslist, bookingsDataList);
            listView.setAdapter(customArrayAdapter);
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
