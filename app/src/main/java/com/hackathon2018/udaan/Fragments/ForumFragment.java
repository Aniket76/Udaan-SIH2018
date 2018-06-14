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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.hackathon2018.udaan.Adapters.ForumSectionsPagerAdapter;
import com.hackathon2018.udaan.R;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    private ViewPager mViewPager;
    private ForumSectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;

    private FloatingActionButton addQuestion;
    private ProgressDialog mMainProgress;
    private String uid,dbName,dbDp;

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager = (ViewPager)getActivity().findViewById(R.id.forumpage_vp);
        mSectionsPagerAdapter = new ForumSectionsPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout)getActivity().findViewById(R.id.forumpage_tabs);
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

        addQuestion = (FloatingActionButton)getActivity().findViewById(R.id.forum_add_fab);

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.update_profile_layout, null);

                final TextView mTitle1 = (TextView) mView.findViewById(R.id.Udate_profile_title_txt);

                final TextInputLayout mTil1 = (TextInputLayout) mView.findViewById(R.id.update_profile_til1);

                mTitle1.setText("Ask Question");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Question");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

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
                                mMainProgress.setTitle("Adding Question");
                                mMainProgress.setMessage("Please wait while we add your Question");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("question", st1);
                                    user.put("postedBy", uid);
                                    user.put("name",dbName);
                                    user.put("image",dbDp);
                                    user.put("postingTime", Calendar.getInstance().getTime().toString());

                                    final DocumentReference mSkillsRef = FirebaseFirestore.getInstance().collection("Questions").document();

                                    mSkillsRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            String quesId = mSkillsRef.getId();

                                            HashMap<String, Object> user = new HashMap<>();
                                            user.put("answer", "");
                                            user.put("postedBy", "");
                                            user.put("name",dbName);
                                            user.put("image",dbDp);
                                            user.put("postingTime", Calendar.getInstance().getTime().toString());

                                            DocumentReference mSkillsRef1 = FirebaseFirestore.getInstance().collection("Questions").document(quesId)
                                                    .collection("Answers").document();

                                            mSkillsRef1.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        }
                                    });
                                    mMainProgress.dismiss();
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
