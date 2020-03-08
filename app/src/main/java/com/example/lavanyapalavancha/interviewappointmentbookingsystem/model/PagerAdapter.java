package com.example.lavanyapalavancha.interviewappointmentbookingsystem.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments.MakeBookingsTab;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments.StudentBookingsTab;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments.ViewAnnouncementsTab;

/**
 * Created by lavanyapalavancha on 10/21/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int totalTabs;

    public PagerAdapter(FragmentManager fm, int tabTitles) {
        super(fm);
        this.totalTabs = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ViewAnnouncementsTab viewAnnouncementsTab = new ViewAnnouncementsTab();
                return viewAnnouncementsTab;
            case 1:
                MakeBookingsTab makeBookingsTab = new MakeBookingsTab();
                return makeBookingsTab;
            case 2:
                StudentBookingsTab studentBookingsTab = new StudentBookingsTab();
                return studentBookingsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
