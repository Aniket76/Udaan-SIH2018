package com.hackathon2018.udaan.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackathon2018.udaan.Activities.ChatMsgActivity;
import com.hackathon2018.udaan.Activities.GroupChatActivity;
import com.hackathon2018.udaan.Activities.OthersProfileActivity;
import com.hackathon2018.udaan.Adapters.ChatSectionsPagerAdapter;
import com.hackathon2018.udaan.Adapters.EventsSectionsPagerAdapter;
import com.hackathon2018.udaan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    private ViewPager mViewPager;
    private ChatSectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;

    private String city;

    private FloatingActionButton groupChat;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager = (ViewPager)getActivity().findViewById(R.id.chatpage_vp);
        mSectionsPagerAdapter = new ChatSectionsPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout)getActivity().findViewById(R.id.chatpage_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        groupChat = (FloatingActionButton)getActivity().findViewById(R.id.group_chat_fab);

        groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DocumentReference mCurrentNameRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserUid);
                mCurrentNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            city = documentSnapshot.getString("City");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                Intent myintent = new Intent(getActivity(), GroupChatActivity.class);
                myintent.putExtra("city", city);
                getContext().startActivity(myintent);

            }
        });

    }


}
