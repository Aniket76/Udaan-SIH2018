package com.hackathon2018.udaan.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hackathon2018.udaan.Fragments.ChatChatFragment;
import com.hackathon2018.udaan.Fragments.ChatRequestFragment;
import com.hackathon2018.udaan.Fragments.ForumFeedFragment;
import com.hackathon2018.udaan.Fragments.ForumMyQuestionsFragment;

/**
 * Created by aniketvishal on 23/12/17.
 */

public class ChatSectionsPagerAdapter extends FragmentPagerAdapter {

    public ChatSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ChatChatFragment patternsFragment = new ChatChatFragment();
                return patternsFragment;

            case 1:
                ChatRequestFragment conversationFragment = new ChatRequestFragment();
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
                return "Chat";

            case 1:
                return "Requests";

            default:
                return null;

        }

    }

}
