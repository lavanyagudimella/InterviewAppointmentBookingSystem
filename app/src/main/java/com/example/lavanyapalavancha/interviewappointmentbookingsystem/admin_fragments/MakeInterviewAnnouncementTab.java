package com.example.lavanyapalavancha.interviewappointmentbookingsystem.admin_fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.InterviewAnnouncementData;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MakeInterviewAnnouncementTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MakeInterviewAnnouncementTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeInterviewAnnouncementTab extends Fragment implements View.OnClickListener{

    private EditText unitCode, unitGrade, interviewAnnouncement, interviewDate, interviewTimings, interviewRoom;
    private Button adminloginButton;
    private DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MakeInterviewAnnouncementTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeInterviewAnnouncementTab.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeInterviewAnnouncementTab newInstance(String param1, String param2) {
        MakeInterviewAnnouncementTab fragment = new MakeInterviewAnnouncementTab();
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
        View view = inflater.inflate(R.layout.fragment_make_interview_announcement_tab, container, false);

        unitCode = (EditText) view.findViewById(R.id.unitCodeEditText);
        unitGrade = (EditText) view.findViewById(R.id.unitGradeEditText);
        interviewAnnouncement = (EditText) view.findViewById(R.id.announcementEditText);
        interviewDate = (EditText) view.findViewById(R.id.interviewDateEditText);
        interviewTimings = (EditText) view.findViewById(R.id.interviewTimingsEditText);;
        interviewRoom = (EditText) view.findViewById(R.id.roomEditText);

        adminloginButton = (Button) view.findViewById(R.id.announcementButton);
        adminloginButton.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("InterviewAnnouncements");

        setInterviewDate();
        setInterviewTime();
        checkConnection();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == adminloginButton) {
            MakeInterviewAnnouncement();
        }
    }

    private void MakeInterviewAnnouncement() {
        String code = unitCode.getText().toString().trim();
        String grade = unitGrade.getText().toString().trim();
        String date = interviewDate.getText().toString().trim();
        String timings = interviewTimings.getText().toString().trim();
        String room = interviewRoom.getText().toString().trim();
        String announcement = interviewAnnouncement.getText().toString().trim();

        TextUtils(code,grade,date,timings,room,announcement);

        if(!code.equals("") && !grade.equals("") && !date.equals("") && !timings.equals("") && !room.equals("") && !announcement.equals("")) {
            InterviewAnnouncementData interviewAnnouncementData = new InterviewAnnouncementData(code,grade,date,timings, room, announcement);
            databaseReference.push().setValue(interviewAnnouncementData);
            Toast.makeText(getContext(), "Announcement made successfully", Toast.LENGTH_LONG).show();
            resetEditTextFields(unitCode,unitGrade,interviewDate,interviewTimings,interviewRoom,interviewAnnouncement);
        } else {
            Toast.makeText(getContext(), "Fields can't be blank", Toast.LENGTH_LONG).show();
        }
    }

    private void setInterviewDate() {
        final Calendar calender = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calender.set(Calendar.YEAR, year);
                calender.set(Calendar.MONTH, monthOfYear);
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                interviewDate.setText(sdf.format(calender.getTime()));
            }
        };

        interviewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                        calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setInterviewTime() {
        interviewTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        interviewTimings.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false); //24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    private void TextUtils(String unitCode, String unitGrade, String date, String timings, String room, String announcement) {
        if(TextUtils.isEmpty(unitCode)) {
            Toast.makeText(getContext(), "Please enter unit code, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(unitGrade)) {
            Toast.makeText(getContext(), "Please enter unit grade, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(date)) {
            Toast.makeText(getContext(), "Please enter date, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(timings)) {
            Toast.makeText(getContext(), "Please enter interview 'From' time, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(room)) {
                Toast.makeText(getContext(), "Please enter interview place, Fields can't be blank", Toast.LENGTH_LONG).show();
                return;
        } else if(TextUtils.isEmpty(announcement)) {
            Toast.makeText(getContext(), "Please enter interview announcement, Fields can't be blank", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void resetEditTextFields(EditText unitCode, EditText unitGrade, EditText date, EditText timings, EditText room, EditText announcement) {
        unitCode.setText("");
        unitGrade.setText("");
        date.setText("");
        timings.setText("");
        room.setText("");
        announcement.setText("");
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
