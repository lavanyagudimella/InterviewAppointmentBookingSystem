package com.example.lavanyapalavancha.interviewappointmentbookingsystem.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.admin_fragments.AdminAnnouncementsTab;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.admin_fragments.MakeInterviewAnnouncementTab;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.admin_fragments.ViewBookingsTab;

/**
 * Created by lavanyapalavancha on 10/20/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int totalTabs;

    public ViewPagerAdapter(FragmentManager fm, int tabtitles) {
        super(fm);
        this.totalTabs = tabtitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ViewBookingsTab viewBookingsTab = new ViewBookingsTab();
                return viewBookingsTab;
            case 1:
                MakeInterviewAnnouncementTab makeInterviewAnnouncementTab = new MakeInterviewAnnouncementTab();
                return makeInterviewAnnouncementTab;
            case 2:
                AdminAnnouncementsTab adminAnnouncementsTab = new AdminAnnouncementsTab();
                return adminAnnouncementsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
