package com.hackathon2018.udaan.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.hackathon2018.udaan.Activities.MainActivity;
import com.hackathon2018.udaan.Activities.StartActivity;
import com.hackathon2018.udaan.BottomNavigationViewHelper;
import com.hackathon2018.udaan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private BottomNavigationView mBottomNavigationItemView;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mBottomNavigationItemView = (BottomNavigationView)getActivity().findViewById(R.id.bottom_nav);
        mBottomNavigationItemView.setItemIconTintList(null);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationItemView);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_cont, new ForumFragment()).commit();
        mBottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){

                    case R.id.nav_forum_btn:
                        transaction.replace(R.id.nav_cont, new ForumFragment());
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        transaction.commit();
                        return true;

                    case R.id.nav_chat_btn:
                        transaction.replace(R.id.nav_cont, new ChatFragment());
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        transaction.commit();
                        return true;

                    case R.id.nav_search_btn:
                        transaction.replace(R.id.nav_cont, new SearchFragment());
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        transaction.commit();
                        return true;

                    case R.id.nav_news_btn:
                        transaction.replace(R.id.nav_cont, new NewsFragment());
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        transaction.commit();
                        return true;

                    case R.id.nav_events_btn:
                        transaction.replace(R.id.nav_cont, new EventsFragment());
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        transaction.commit();
                        return true;
                }

                return false;
            }
        });


    }
}
