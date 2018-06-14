package com.hackathon2018.udaan.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hackathon2018.udaan.Adapters.EventsSectionsPagerAdapter;
import com.hackathon2018.udaan.Adapters.ForumSectionsPagerAdapter;
import com.hackathon2018.udaan.R;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private ViewPager mViewPager;
    private EventsSectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    private ProgressDialog mMainProgress;

    private FloatingActionButton addEvent;

    private String uid,dbName,dbDp;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager = (ViewPager)getActivity().findViewById(R.id.eventspage_vp);
        mSectionsPagerAdapter = new EventsSectionsPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout)getActivity().findViewById(R.id.eventspage_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();

        DocumentReference mNameRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    dbName = documentSnapshot.getString("displayName");
                    dbDp = documentSnapshot.getString("image");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        addEvent = (FloatingActionButton)getActivity().findViewById(R.id.event_add_fab);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.update_profile_layout, null);

                final TextView mTitle1 = (TextView) mView.findViewById(R.id.Udate_profile_title_txt);
                final Button mBtn1 = (Button) mView.findViewById(R.id.update_profile_btn1);

                final TextInputLayout mTil1 = (TextInputLayout) mView.findViewById(R.id.update_profile_til1);
                final TextInputLayout mTil2 = (TextInputLayout) mView.findViewById(R.id.update_profile_til2);
                final TextInputLayout mTil3 = (TextInputLayout) mView.findViewById(R.id.update_profile_til3);
                final TextInputLayout mTil4 = (TextInputLayout) mView.findViewById(R.id.update_profile_til4);
                final TextInputLayout mTil5 = (TextInputLayout) mView.findViewById(R.id.update_profile_til5);
                final TextInputLayout mTil6 = (TextInputLayout) mView.findViewById(R.id.update_profile_til6);

                final TextView mTxt1 = (TextView) mView.findViewById(R.id.update_profile_txt1);
                final TextView mTxt2 = (TextView) mView.findViewById(R.id.update_profile_txt2);
                final TextView mTxt3 = (TextView) mView.findViewById(R.id.update_profile_txt3);
                final TextView mTxt4 = (TextView) mView.findViewById(R.id.update_profile_txt4);
                final TextView mTxt5 = (TextView) mView.findViewById(R.id.update_profile_txt5);
                final TextView mTxt6 = (TextView) mView.findViewById(R.id.update_profile_txt6);
                final TextView mMsgTxt = (TextView) mView.findViewById(R.id.update_profile_msg_txt);

                mTitle1.setText("Add Event");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Event Title");
                mTil1.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Max. Length 30");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Description");
                mTil1.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                mTxt2.setVisibility(View.VISIBLE);
                mTxt2.setText("Max. Length 120");

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Timings");
                mTil3.getEditText().setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: 03:00pm");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("Date");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("DD/MM/YYYY");

                mTil5.setVisibility(View.VISIBLE);
                mTil5.setHint("Venue");
                mTil5.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTil6.setVisibility(View.VISIBLE);
                mTil6.setHint("City");
                mTil6.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

//                mMsgTxt.setVisibility(View.VISIBLE);
//                mMsgTxt.setText("** Keep the SKILLS Comma(,) seprated. **");

                mBulider.setView(view)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mMainProgress = new ProgressDialog(getContext());
                                mMainProgress.setTitle("Adding Event");
                                mMainProgress.setMessage("Please wait while we add your Event");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();
                                String st2 = mTil2.getEditText().getText().toString();
                                String st3 = mTil3.getEditText().getText().toString();
                                String st4 = mTil4.getEditText().getText().toString();
                                String st5 = mTil5.getEditText().getText().toString();
                                String st6 = mTil6.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1) && !TextUtils.isEmpty(st2) && !TextUtils.isEmpty(st3) && !TextUtils.isEmpty(st4) && !TextUtils.isEmpty(st5) && !TextUtils.isEmpty(st6)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("title", st1);
                                    user.put("desc", st2);
                                    user.put("time", st3);
                                    user.put("date", st4);
                                    user.put("venue", st5);
                                    user.put("city", st6);
                                    user.put("postedBy", uid);
                                    user.put("name",dbName);
                                    user.put("image",dbDp);
                                    user.put("postingTime", Calendar.getInstance().getTime().toString());

                                    DocumentReference mSkillsRef = FirebaseFirestore.getInstance().collection("Events").document();

                                    mSkillsRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(getActivity(), "Added", Toast.LENGTH_LONG).show();
                                                mMainProgress.dismiss();

                                            } else {

                                                mMainProgress.dismiss();
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                    dialogInterface.dismiss();
                                } else {
                                    mMainProgress.dismiss();
                                    Toast.makeText(getActivity(), "Fill all the fields and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                mBulider.setView(mView);
                AlertDialog dialog = mBulider.create();
                dialog.show();

            }
        });

    }

}
