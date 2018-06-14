package com.hackathon2018.udaan.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hackathon2018.udaan.Fragments.ForumFeedFragment;
import com.hackathon2018.udaan.Fragments.ForumMyQuestionsFragment;

/**
 * Created by aniketvishal on 23/12/17.
 */

public class ForumSectionsPagerAdapter extends FragmentPagerAdapter {

    public ForumSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ForumFeedFragment patternsFragment = new ForumFeedFragment();
                return patternsFragment;

            case 1:
                ForumMyQuestionsFragment conversationFragment = new ForumMyQuestionsFragment();
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
                return "Feed";

            case 1:
                return "My Questions";

            default:
                return null;

        }

    }

}
