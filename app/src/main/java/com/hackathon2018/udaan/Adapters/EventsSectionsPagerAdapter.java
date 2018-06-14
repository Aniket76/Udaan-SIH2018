package com.hackathon2018.udaan.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hackathon2018.udaan.Fragments.ChatChatFragment;
import com.hackathon2018.udaan.Fragments.ChatRequestFragment;
import com.hackathon2018.udaan.Fragments.EventsAllFragment;
import com.hackathon2018.udaan.Fragments.EventsMyEventsFragment;

/**
 * Created by aniketvishal on 23/12/17.
 */

public class EventsSectionsPagerAdapter extends FragmentPagerAdapter {

    public EventsSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                EventsAllFragment patternsFragment = new EventsAllFragment();
                return patternsFragment;

            case 1:
                EventsMyEventsFragment conversationFragment = new EventsMyEventsFragment();
                return conversationFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {

            case 0:
                return "All Events";

            case 1:
                return "My Events";

            default:
                return null;

        }

    }

}
