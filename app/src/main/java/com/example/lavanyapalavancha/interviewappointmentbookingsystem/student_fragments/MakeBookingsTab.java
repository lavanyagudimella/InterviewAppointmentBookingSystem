package com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.R;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.StudentBookingsData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MakeBookingsTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MakeBookingsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeBookingsTab extends Fragment implements View.OnClickListener {

    private DatabaseReference databaseReference, adapterReference, removeTimingsReference, studentBookingsReference;
    private StudentBookingsData studentBookingsData;
    private FirebaseUser user;
    private EditText studentName, studentNumber, workDescription;
    private Spinner bookingTimings;
    private Button makeBookingButton;
    private ArrayAdapter<String> spinnerArrayAdapter;

    private String[] splitTimingsArray;
    private List<String> announcementsTimingsArray;
    private List<String> emailArray;
    private List<String> studentNumberArray;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MakeBookingsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeBookingsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeBookingsTab newInstance(String param1, String param2) {
        MakeBookingsTab fragment = new MakeBookingsTab();
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
        View view = inflater.inflate(R.layout.fragment_make_bookings_tab, container, false);

        splitTimingsArray = new String[1000];
        emailArray = new ArrayList<>();
        studentNumberArray = new ArrayList<>();

        studentName = (EditText) view.findViewById(R.id.studentNameEditText);
        studentNumber = (EditText) view.findViewById(R.id.studentNumberEditText);
        workDescription = (EditText)view.findViewById(R.id.workDescriptionEditText);
        bookingTimings = (Spinner) view.findViewById(R.id.spinner);

        makeBookingButton = (Button) view.findViewById(R.id.bookingsButton);
        makeBookingButton.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("StudentBookings");
        adapterReference = FirebaseDatabase.getInstance().getReference().child("InterviewAnnouncements");
        removeTimingsReference = FirebaseDatabase.getInstance().getReference().child("StudentBookings");
        studentBookingsReference = FirebaseDatabase.getInstance().getReference().child("StudentBookings");
        user = FirebaseAuth.getInstance().getCurrentUser();

        setTimingsToAdapter();
        checkUserBookings();
        checkConnection();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == makeBookingButton) {
            MakeInterviewBooking();
        }
    }

    private void checkUserBookings() {
        studentBookingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, String> bookingData = (Map<String, String>) ds.getValue();

                    String firebaseEmail = bookingData.get("contactEmail");
                    Log.d("TAG", "Firebase Email" +firebaseEmail);
                    emailArray.add(firebaseEmail);
                    Log.d("TAG", "Firebase Email Array" +emailArray);

                    String studentNumber = bookingData.get("studentNumber");
                    Log.d("TAG", "Firebase Student Number" +studentNumber);
                    studentNumberArray.add(studentNumber);
                    Log.d("TAG", "Firebase Student Number Array" +studentNumberArray);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void MakeInterviewBooking() {
        String name = studentName.getText().toString().trim();
        String number = studentNumber.getText().toString().trim();
        String email = user.getEmail();
        String description = workDescription.getText().toString().trim();
        String interviewTimings = bookingTimings.getSelectedItem().toString().trim();

        TextUtils(name,number,description, interviewTimings);

        if(emailArray.contains(email)) {
            Toast.makeText(getContext(), "Already Booked an Interview ! Can't make another Interview Booking", Toast.LENGTH_LONG).show();
        } else if(studentNumberArray.contains(number)) {
            Toast.makeText(getContext(), "Already Booked an Interview ! Can't make another Interview Booking with the same student number", Toast.LENGTH_LONG).show();
        } else {
            if(!name.equals("") && !number.equals("") && !description.equals("") && !interviewTimings.equals("")) {
                studentBookingsData = new StudentBookingsData(name,number,email,description, interviewTimings);
                databaseReference.push().setValue(studentBookingsData);
                Toast.makeText(getContext(), "Booking made successfully", Toast.LENGTH_LONG).show();
                resetEditTextFields(studentName,studentNumber,workDescription);
            } else {
                Toast.makeText(getContext(), "Fields can't be blank", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setTimingsToAdapter() {
        adapterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, String> bookingData = (Map<String, String>) ds.getValue();
                    String interviewTimings = bookingData.get("timings");

                    if (interviewTimings != null) {
                        splitTimingsArray = interviewTimings.split(",");
                        announcementsTimingsArray = new ArrayList<String>(Arrays.asList(splitTimingsArray));

                        spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, announcementsTimingsArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bookingTimings.setAdapter(spinnerArrayAdapter);
                    }

                    removeBookedTimings();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeBookedTimings() {
        removeTimingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, String> bookedData = (Map<String, String>) ds.getValue();

                    String bookedTimings = bookedData.get("interviewTimings");
                    Log.d("TAG", "BOOKED Timings" +bookedTimings);

                    for (Iterator<String> it = announcementsTimingsArray.iterator(); it.hasNext();) {
                        if (it.next().contains(bookedTimings)) {
                            it.remove();
                            spinnerArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void TextUtils(String studentName, String studentNumber, String workDescription, String timings) {
        if(TextUtils.isEmpty(studentName)) {
            Toast.makeText(getContext(), "Please enter Student Name, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(studentNumber)) {
            Toast.makeText(getContext(), "Please enter Student Number, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(workDescription)) {
            Toast.makeText(getContext(), "Please enter research description, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(timings)) {
            Toast.makeText(getContext(), "Please enter research description, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void resetEditTextFields(EditText studentname, EditText studentNumber, EditText workDescription) {
        studentname.setText("");
        studentNumber.setText("");
        workDescription.setText("");
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
