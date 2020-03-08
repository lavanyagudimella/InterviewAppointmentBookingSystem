package com.example.lavanyapalavancha.interviewappointmentbookingsystem.model;

import android.content.Context;
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
 * Created by lavanyapalavancha on 10/21/17.
 */

public class CustomAdapter extends ArrayAdapter<String> {

    private ArrayList<InterviewAnnouncementData> announcementsArray;
    private Context mContext;
    private int resource;


    public CustomAdapter(Context context, int resource, ArrayList<InterviewAnnouncementData> announcementsData) {
        super(context, R.layout.activity_interview_announcementslist);
        this.announcementsArray = announcementsData;
        this.mContext = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return announcementsArray.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater layoutinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutinflater.inflate(R.layout.activity_interview_announcementslist, parent, false);

            viewholder.announcement = (TextView) convertView.findViewById(R.id.announcementTextView);
            viewholder.unitCode = (TextView) convertView.findViewById(R.id.unicodeTextView);
            viewholder.unitGrade = (TextView) convertView.findViewById(R.id.unitGradeTextView);
            viewholder.date = (TextView) convertView.findViewById(R.id.dateTextView);
            viewholder.timings = (TextView) convertView.findViewById(R.id.interviewTimings);
            viewholder.room = (TextView) convertView.findViewById(R.id.roomTextView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.announcement.setText(announcementsArray.get(position).getAnnouncement());
        viewholder.unitCode.setText("Unit Code :" +announcementsArray.get(position).getUnitcode());
        viewholder.unitGrade.setText("Unit Grade :" +announcementsArray.get(position).getUnitGrade());
        viewholder.date.setText("Interview Date :" +announcementsArray.get(position).getDate());
        viewholder.timings.setText("Interview Timings" + "\n" +announcementsArray.get(position).getTimings());
        viewholder.room.setText("Interview Room :" +announcementsArray.get(position).getRoom());

        return convertView;
    }

    private static class ViewHolder {
        TextView unitCode;
        TextView unitGrade;
        TextView date;
        TextView timings;
        TextView room;
        TextView announcement;
    }
}
