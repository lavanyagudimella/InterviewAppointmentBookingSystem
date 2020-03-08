package com.example.lavanyapalavancha.interviewappointmentbookingsystem.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.R;

import java.util.ArrayList;

/**
 * Created by lavanyapalavancha on 10/22/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<StudentBookingsData> bookingsArray;
    private Context mContext;
    private int resource;

    public CustomArrayAdapter(Context context, int resource, ArrayList<StudentBookingsData> bookingsData) {
        super(context, R.layout.activity_interview_bookingslist);
        this.bookingsArray = bookingsData;
        this.mContext = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return bookingsArray.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CustomArrayAdapter.ViewHolder viewholder = new CustomArrayAdapter.ViewHolder();
        if(convertView == null) {
            LayoutInflater layoutinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutinflater.inflate(R.layout.activity_interview_bookingslist, parent, false);

            viewholder.studentName = (TextView) convertView.findViewById(R.id.studentNameTextView);
            viewholder.studentNumber = (TextView) convertView.findViewById(R.id.studentNumberTextView);
            viewholder.contactEmail = (TextView) convertView.findViewById(R.id.contactEmailTextView);
            viewholder.workDescription = (TextView) convertView.findViewById(R.id.workDescriptionTextView);
            viewholder.interviewTimings = (TextView) convertView.findViewById(R.id.interviewTimingsTextView);

            convertView.setTag(viewholder);
        } else {
            viewholder = (CustomArrayAdapter.ViewHolder) convertView.getTag();
        }

        viewholder.studentName.setText("Student Name :" +bookingsArray.get(position).getStudentName());
        viewholder.studentNumber.setText("Student Number :" +bookingsArray.get(position).getStudentNumber());
        viewholder.contactEmail.setText("Contact Email :" +bookingsArray.get(position).getContactEmail());
        viewholder.workDescription.setText("Work Description :" + "\n" +bookingsArray.get(position).getWorkDescription());
        viewholder.interviewTimings.setText("Interview Timings :" +bookingsArray.get(position).getInterviewTimings());

        return convertView;
    }

    private static class ViewHolder {
        TextView studentName;
        TextView studentNumber;
        TextView contactEmail;
        TextView workDescription;
        TextView interviewTimings;
    }
}
