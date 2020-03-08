package com.example.lavanyapalavancha.interviewappointmentbookingsystem.admin_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lavanyapalavancha.interviewappointmentbookingsystem.R;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.activities_firebase.AdminLoginActivity;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.activities_firebase.MainActivity;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.model.ViewPagerAdapter;
import com.example.lavanyapalavancha.interviewappointmentbookingsystem.student_fragments.StudentFragmentActivity;

/**
 * Created by lavanyapalavancha on 10/20/17.
 */

public class AdminFragmentActivity extends AppCompatActivity implements ViewBookingsTab.OnFragmentInteractionListener,
                                                                        MakeInterviewAnnouncementTab.OnFragmentInteractionListener,
                                                                        AdminAnnouncementsTab.OnFragmentInteractionListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminfragment);

        checkConnection();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Admin Announcements");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.adminTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Student Bookings"));
        tabLayout.addTab(tabLayout.newTab().setText("New Interview Announcement"));
        tabLayout.addTab(tabLayout.newTab().setText("My Announcements"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.setTabTextColors(R.color.darkgrey, R.color.white);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_signout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                Intent studentIntent = new Intent(AdminFragmentActivity.this, MainActivity.class);
                startActivity(studentIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
            Toast.makeText(AdminFragmentActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
