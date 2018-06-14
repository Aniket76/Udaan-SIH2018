package com.hackathon2018.udaan.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hackathon2018.udaan.Activities.MainActivity;
import com.hackathon2018.udaan.Activities.StartActivity;
import com.hackathon2018.udaan.AppData.AppStorage;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Ref;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.APP_OPS_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private ImageView mChangeDpBtn, mProfileDetailsBtn, mEducationBtn, mWEBtn, mSkillsBtn, mCertificationsImg, mLanguagesImg, mTagsImg;

    private TextView mName, mStatus;
    private TextView mAadhaarNo, mEmain, mPhoneNo, mDob, mRealName, mCity;
    private TextView mEd1, mEd2;
    private TextView mWe1, mWe2;
    private TextView mSkills;
    private TextView mCert1, mCert2;
    private TextView mLanguages;
    private TextView mTags;

    private String dbDegree1, dbSchool1, dbFieldOfStudy1, dbGrade1, dbStartedIn1, dbCompletedIn1;
    private String dbDegree2, dbSchool2, dbFieldOfStudy2, dbGrade2, dbStartedIn2, dbCompletedIn2;
    private String dbTitle1, dbCompany1, dbLocation1, dbFrom1, dbTo1, dbIndustry1;
    private String dbTitle2, dbCompany2, dbLocation2, dbFrom2, dbTo2, dbIndustry2;
    private String dbCName1, dbCEvent1, dbCAuthority1, dbYear1;
    private String dbCName2, dbCEvent2, dbCAuthority2, dbYear2;

    private Boolean mEduTag = false, mTechTag = false, mSportTag = false;

    private LinearLayout mLlEd1, mLlEd2;
    private LinearLayout mLlWe1, mLlWe2;
    private LinearLayout mLlCert1, mLlCert2;

    private CircleImageView mDisplayPic;

    private ProgressDialog mMainProgress;

    private FirebaseAuth mAuth;
    private StorageReference mImageStorage;
    private String uid;

    private String dbName, dbStatus;
    private String dbAadhaarNo, dbEmail, dbPhoneNo, dbDob, dbDp, dbCity, dbRealName, dbState;
    private String dbSkills;
    private String dbLanguages;
    private String dbTags;

    private int EdCount = 1;
    private int WeCount = 1;
    private int CertCount = 1;

    private static final int GALLEY_PICK = 1;


    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDisplayPic = (CircleImageView) getActivity().findViewById(R.id.userProfile_dp_img);
        mChangeDpBtn = (ImageView) getActivity().findViewById(R.id.urerProfile_changeDp_btn);

        mName = (TextView) getActivity().findViewById(R.id.urerProfile_name_txt);
        mStatus = (TextView) getActivity().findViewById(R.id.urerProfile_about_txt);

        mProfileDetailsBtn = (ImageView) getActivity().findViewById(R.id.urerProfile_personalDetails_btn);
        mAadhaarNo = (TextView) getActivity().findViewById(R.id.urerProfile_aadhaarNo_txt);
        mEmain = (TextView) getActivity().findViewById(R.id.urerProfile_email_txt);
        mPhoneNo = (TextView) getActivity().findViewById(R.id.urerProfile_phoneNo_txt);
        mDob = (TextView) getActivity().findViewById(R.id.urerProfile_dob_txt);
        mCity = (TextView) getActivity().findViewById(R.id.urerProfile_city_txt);
        mRealName = (TextView) getActivity().findViewById(R.id.urerProfile_realName_txt);


        //**************Education Section******************

        mEducationBtn = (ImageView) getActivity().findViewById(R.id.urerProfile_education_btn);
        mEd1 = (TextView) getActivity().findViewById(R.id.urerProfile_education1_txt);
        mEd2 = (TextView) getActivity().findViewById(R.id.urerProfile_education2_txt);

        mLlEd1 = (LinearLayout) getActivity().findViewById(R.id.urerProfile_education1_layout);
        mLlEd2 = (LinearLayout) getActivity().findViewById(R.id.urerProfile_education2_layout);


        //**************WE Section******************

        mWEBtn = (ImageView) getActivity().findViewById(R.id.userProfile_we_btn);
        mWe1 = (TextView) getActivity().findViewById(R.id.userProfile_we1_txt);
        mWe2 = (TextView) getActivity().findViewById(R.id.userProfile_we2_txt);

        mLlWe1 = (LinearLayout) getActivity().findViewById(R.id.userProfile_we1_layout);
        mLlWe2 = (LinearLayout) getActivity().findViewById(R.id.userProfile_we2_layout);


        //**************Skills Section******************

        mSkillsBtn = (ImageView) getActivity().findViewById(R.id.userProfile_skills_btn);
        mSkills = (TextView) getActivity().findViewById(R.id.userProfile_skills1_txt);


        //**************Certification Section******************

        mCertificationsImg = (ImageView) getActivity().findViewById(R.id.userProfile_certifications_btn);
        mCert1 = (TextView) getActivity().findViewById(R.id.userProfile_certifications1_txt);
        mCert2 = (TextView) getActivity().findViewById(R.id.userProfile_certifications2_txt);

        mLlCert1 = (LinearLayout) getActivity().findViewById(R.id.userProfile_certifications1_layout);
        mLlCert2 = (LinearLayout) getActivity().findViewById(R.id.userProfile_certifications2_layout);


        //**************Languages Section******************

        mLanguagesImg = (ImageView) getActivity().findViewById(R.id.userProfile_languages_btn);
        mLanguages = (TextView) getActivity().findViewById(R.id.userProfile_languages1_txt);


        //**************Tags Section******************

        mTagsImg = (ImageView) getActivity().findViewById(R.id.userProfile_tags_btn);
        mTags = (TextView) getActivity().findViewById(R.id.userProfile_tags1_txt);


        // **************Retrieving Data from Firestore***************

        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        FirebaseUser current_user = mAuth.getCurrentUser();
        uid = current_user.getUid();


        //****************** Get Count **********************

        DocumentReference mCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Count").document("Count");
        mCountRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String EdC = documentSnapshot.getString("EdCount");
                    String WeC = documentSnapshot.getString("WeCount");
                    String CertC = documentSnapshot.getString("CertCount");

                    try {
                        EdCount = Integer.parseInt(EdC);
                        WeCount = Integer.parseInt(WeC);
                        CertCount = Integer.parseInt(CertC);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //****************** Name and Status **********************

        DocumentReference mNameRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    dbName = documentSnapshot.getString("displayName");
                    dbStatus = documentSnapshot.getString("status");

                    if (!(dbName.equals(""))) {
                        mName.setText(dbName);
                        AppStorage.storeStringInSF(getActivity(),AppStorage.USERNAME,dbName);
                    }
                    if (!(dbStatus.equals(""))) {
                        mStatus.setText(dbStatus);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //********************DP**********************

        DocumentReference mDpRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mDpRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    dbDp = documentSnapshot.getString("image");

                    if (!(dbDp.equals(""))) {

                        Picasso.with(getContext()).load(dbDp).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profile_icon).into(mDisplayPic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getContext()).load(dbDp).placeholder(R.drawable.profile_icon).into(mDisplayPic);
                            }
                        });

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //********************** Personal Details **********************

        DocumentReference mPersonalRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mPersonalRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    dbAadhaarNo = documentSnapshot.getString("Aadhaar");
                    dbEmail = documentSnapshot.getString("Email");
                    dbPhoneNo = documentSnapshot.getString("Phone");
                    dbDob = documentSnapshot.getString("Dob");
                    dbRealName = documentSnapshot.getString("Name");
                    dbCity = documentSnapshot.getString("City");
                    dbState = documentSnapshot.getString("State");


                    mAadhaarNo.setText(dbAadhaarNo);
                    mEmain.setText(dbEmail);
                    mPhoneNo.setText(dbPhoneNo);
                    mDob.setText(dbDob);
                    mCity.setText(dbCity + ", " + dbState);
                    mRealName.setText(dbRealName);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //******************** Education *********************

        final DocumentReference mEd1Ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Education").document("Education1");
        mEd1Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    dbSchool1 = documentSnapshot.getString("School");
                    dbDegree1 = documentSnapshot.getString("Degree");
                    dbFieldOfStudy1 = documentSnapshot.getString("FieldOfStudy");
                    dbGrade1 = documentSnapshot.getString("Grade");
                    dbStartedIn1 = documentSnapshot.getString("StartedIn");
                    dbCompletedIn1 = documentSnapshot.getString("CompletedIn");

                    String ed1 = dbDegree1 + " in " + dbFieldOfStudy1 + " from " + dbSchool1 + " with " + dbGrade1 + " (" + dbStartedIn1 + "-" + dbCompletedIn1 + ").";
                    mEd1.setText(ed1);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DocumentReference mEd2Ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Education").document("Education2");
        mEd2Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    mLlEd2.setVisibility(View.VISIBLE);
                    dbSchool2 = documentSnapshot.getString("School");
                    dbDegree2 = documentSnapshot.getString("Degree");
                    dbFieldOfStudy2 = documentSnapshot.getString("FieldOfStudy");
                    dbGrade2 = documentSnapshot.getString("Grade");
                    dbStartedIn2 = documentSnapshot.getString("StartedIn");
                    dbCompletedIn2 = documentSnapshot.getString("CompletedIn");

                    String ed1 = dbDegree2 + " in " + dbFieldOfStudy2 + " from " + dbSchool2 + " with " + dbGrade2 + " (" + dbStartedIn2 + "-" + dbCompletedIn2 + ").";
                    mEd2.setText(ed1);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //****************** Work Experience *********************

        DocumentReference mWe1Ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("WorkExperience").document("WorkExperience1");
        mWe1Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
//                    mLlWe2.setVisibility(View.VISIBLE);
                    dbTitle1 = documentSnapshot.getString("Title");
                    dbCompany1 = documentSnapshot.getString("Company");
                    dbLocation1 = documentSnapshot.getString("Location");
                    dbFrom1 = documentSnapshot.getString("From");
                    dbTo1 = documentSnapshot.getString("To");
                    dbIndustry1 = documentSnapshot.getString("Industry");

                    String workExperience = dbTitle1 + " at " + dbCompany1 + ", " + dbLocation1 + " from " + dbFrom1 + " to " + dbTo1 + " (" + dbIndustry1 + ").";
                    mWe1.setText(workExperience);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DocumentReference mWe2Ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("WorkExperience").document("WorkExperience2");
        mWe2Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    mLlWe2.setVisibility(View.VISIBLE);
                    dbTitle2 = documentSnapshot.getString("Title");
                    dbCompany2 = documentSnapshot.getString("Company");
                    dbLocation2 = documentSnapshot.getString("Location");
                    dbFrom2 = documentSnapshot.getString("From");
                    dbTo2 = documentSnapshot.getString("To");
                    dbIndustry2 = documentSnapshot.getString("Industry");

                    String workExperience = dbTitle2 + " at " + dbCompany2 + ", " + dbLocation2 + " from " + dbFrom2 + " to " + dbTo2 + " (" + dbIndustry2 + ").";
                    mWe2.setText(workExperience);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //********************* Skills *******************

        DocumentReference mSkillsRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Skills").document("Skills");
        mSkillsRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    dbSkills = documentSnapshot.getString("Skills");
                    mSkills.setText(dbSkills);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //******************** Certifications **************

        DocumentReference mCert1Ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Certifications").document("Certification1");
        mCert1Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
//                    mLlCert2.setVisibility(View.VISIBLE);
                    dbCName1 = documentSnapshot.getString("CName");
                    dbCEvent1 = documentSnapshot.getString("CEvent");
                    dbCAuthority1 = documentSnapshot.getString("CAuthority");
                    dbYear1 = documentSnapshot.getString("Year");

                    String certification = dbCName1 + " in " + dbCEvent1 + " Organised by " + dbCAuthority1 + " in " + dbYear1 + ".";
                    mCert1.setText(certification);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DocumentReference mCert2Ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Certifications").document("Certification2");
        mCert2Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    mLlCert2.setVisibility(View.VISIBLE);
                    dbCName2 = documentSnapshot.getString("CName");
                    dbCEvent2 = documentSnapshot.getString("CEvent");
                    dbCAuthority2 = documentSnapshot.getString("CAuthority");
                    dbYear2 = documentSnapshot.getString("Year");

                    String certification = dbCName2 + " in " + dbCEvent2 + " Organised by " + dbCAuthority2 + " in " + dbYear2 + ".";
                    mCert2.setText(certification);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //***************** Languages **************

        DocumentReference mLanguageRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Languages").document("Languages");
        mLanguageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    dbLanguages = documentSnapshot.getString("Languages");
                    mLanguages.setText(dbLanguages);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //******************** Tags *****************

        DocumentReference mTagsRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                .collection("Tags").document("Tags");
        mTagsRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mEduTag = documentSnapshot.getBoolean("EduTag");
                    mTechTag = documentSnapshot.getBoolean("TechTag");
                    mSportTag = documentSnapshot.getBoolean("SportTag");

                    String tag = "";

                    if (mEduTag) {
                        tag = tag + "Education | ";
                    }
                    if (mTechTag) {
                        tag = tag + "Technology | ";
                    }
                    if (mSportTag) {
                        tag = tag + "Sports | ";
                    }

                    mTags.setText(tag);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //***************************************************
        //************ Adding Data To Firestore *************
        //***************************************************


        //****************Dp name and Status*****************

        mChangeDpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                mTitle1.setText("Update Info");
                mBtn1.setVisibility(View.VISIBLE);

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Name");
                mTil1.getEditText().setText(dbName);

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Status");
                mTil2.getEditText().setText(dbStatus);

                mBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(getContext(), UserProfileFragment.this);
                    }
                });

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String name = mTil1.getEditText().getText().toString();
                                String status = mTil2.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(status)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("displayName", name);
                                    user.put("status", status);

                                    DocumentReference mNameRef = FirebaseFirestore.getInstance().collection("Users").document(uid);

                                    mNameRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

                                                mMainProgress.dismiss();
                                            } else {
                                                mMainProgress.dismiss();
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                    dialogInterface.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Fill all the fields and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                mBulider.setView(mView);
                AlertDialog dialog = mBulider.create();
                dialog.show();

            }
        });


        //****************Personal Details*****************

        mProfileDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                mTitle1.setText("Update Personal Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("City");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                mTil1.getEditText().setText(dbCity);

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("State");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                mTil2.getEditText().setText(dbState);

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Phone Number");
                mTil3.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
                mTil3.getEditText().setText(dbPhoneNo);

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();
                                String st2 = mTil2.getEditText().getText().toString();
                                String st3 = mTil3.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1) && !TextUtils.isEmpty(st2) && !TextUtils.isEmpty(st3)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("Phone", st3);
                                    user.put("City", st1);
                                    user.put("State", st2);

                                    DocumentReference mPersonalRef = FirebaseFirestore.getInstance().collection("Users").document(uid);

                                    mPersonalRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


        //****************Education Details*****************

        mEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EdCount > 2) {

                    Toast.makeText(getContext(), "No more Educational field can be added.", Toast.LENGTH_LONG).show();

                } else {

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


                    mTitle1.setText("Education Info");

                    mTil1.setVisibility(View.VISIBLE);
                    mTil1.setHint("School");
                    mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt1.setVisibility(View.VISIBLE);
                    mTxt1.setText("Ex.: STCET, Kolkata");

                    mTil2.setVisibility(View.VISIBLE);
                    mTil2.setHint("Degree");
                    mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt2.setVisibility(View.VISIBLE);
                    mTxt2.setText("Ex.: B.Tech");

                    mTil3.setVisibility(View.VISIBLE);
                    mTil3.setHint("Field of Study");
                    mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt3.setVisibility(View.VISIBLE);
                    mTxt3.setText("Ex.: Information Technology");

                    mTil4.setVisibility(View.VISIBLE);
                    mTil4.setHint("Grade");
                    mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                    mTxt4.setVisibility(View.VISIBLE);
                    mTxt4.setText("Ex.: 7.5 CGPA");

                    mTil5.setVisibility(View.VISIBLE);
                    mTil5.setHint("Starting Year");
                    mTil5.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                    mTxt5.setVisibility(View.VISIBLE);
                    mTxt5.setText("Ex.: 2012");

                    mTil6.setVisibility(View.VISIBLE);
                    mTil6.setHint("Finishing Year");
                    mTil6.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                    mTxt6.setVisibility(View.VISIBLE);
                    mTxt6.setText("Ex.: 2016");

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
                                    mMainProgress.setTitle("Uploading Info");
                                    mMainProgress.setMessage("Please wait while we upload your Info");
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
                                        user.put("School", st1);
                                        user.put("Degree", st2);
                                        user.put("FieldOfStudy", st3);
                                        user.put("Grade", st4);
                                        user.put("StartedIn", st5);
                                        user.put("CompletedIn", st6);

                                        if (EdCount == 1) {

                                            DocumentReference mEdRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                    .collection("Education").document("Education1");

                                            mEdRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        String c1 = String.valueOf(EdCount + 1);
                                                        String c2 = String.valueOf(WeCount);
                                                        String c3 = String.valueOf(CertCount);

                                                        HashMap<String, Object> count = new HashMap<>();
                                                        count.put("EdCount", c1);
                                                        count.put("WeCount", c2);
                                                        count.put("CertCount", c3);

                                                        DocumentReference mEdCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                                .collection("Count").document("Count");
                                                        mEdCountRef.set(count, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    UserProfileFragment fragment = new UserProfileFragment();
                                                                    FragmentManager manager = getFragmentManager();
                                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                                    transaction.detach(fragment);
                                                                    transaction.attach(fragment);
                                                                    transaction.commit();

                                                                    mMainProgress.hide();

                                                                } else {
                                                                    mMainProgress.hide();
                                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        mMainProgress.hide();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        } else if (EdCount == 2) {

                                            DocumentReference mEdRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                    .collection("Education").document("Education2");

                                            mEdRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        String c1 = String.valueOf(EdCount + 1);
                                                        String c2 = String.valueOf(WeCount);
                                                        String c3 = String.valueOf(CertCount);

                                                        HashMap<String, Object> count = new HashMap<>();
                                                        count.put("EdCount", c1);
                                                        count.put("WeCount", c2);
                                                        count.put("CertCount", c3);

                                                        DocumentReference mEdCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                                .collection("Count").document("Count");
                                                        mEdCountRef.set(count, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    UserProfileFragment fragment = new UserProfileFragment();
                                                                    FragmentManager manager = getFragmentManager();
                                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                                    transaction.detach(fragment);
                                                                    transaction.attach(fragment);
                                                                    transaction.commit();

                                                                    mMainProgress.hide();

                                                                } else {
                                                                    mMainProgress.hide();
                                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        mMainProgress.hide();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }

                                        dialogInterface.dismiss();
                                    } else {
                                        mMainProgress.hide();
                                        Toast.makeText(getActivity(), "Fill all the fields and try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    mBulider.setView(mView);
                    AlertDialog dialog = mBulider.create();
                    dialog.show();

                }

            }
        });


        //****************Work Experience Details*****************

        mWEBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (WeCount>2){

                    Toast.makeText(getContext(), "No more Work Experience field can be added.", Toast.LENGTH_LONG).show();

                }else {

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


                    mTitle1.setText("Work Experience Info");

                    mTil1.setVisibility(View.VISIBLE);
                    mTil1.setHint("Title*");
                    mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt1.setVisibility(View.VISIBLE);
                    mTxt1.setText("Ex.: Manager");

                    mTil2.setVisibility(View.VISIBLE);
                    mTil2.setHint("Company*");
                    mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

//                mTxt2.setVisibility(View.VISIBLE);
//                mTxt2.setText("Ex.:");

                    mTil3.setVisibility(View.VISIBLE);
                    mTil3.setHint("Location*");
                    mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt3.setVisibility(View.VISIBLE);
                    mTxt3.setText("Ex.: Kolkata, WB");

                    mTil4.setVisibility(View.VISIBLE);
                    mTil4.setHint("From*");
                    mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                    mTxt4.setVisibility(View.VISIBLE);
                    mTxt4.setText("Ex.: 2011");

                    mTil5.setVisibility(View.VISIBLE);
                    mTil5.setHint("To*");
                    mTil5.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                    mTxt5.setVisibility(View.VISIBLE);
                    mTxt5.setText("Ex.: 2015");

                    mTil6.setVisibility(View.VISIBLE);
                    mTil6.setHint("Industry");
                    mTil6.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                    mTxt6.setVisibility(View.VISIBLE);
                    mTxt6.setText("Ex.: IT Sector");

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
                                    mMainProgress.setTitle("Uploading Info");
                                    mMainProgress.setMessage("Please wait while we upload your Info");
                                    mMainProgress.setCanceledOnTouchOutside(false);
                                    mMainProgress.show();

                                    String st1 = mTil1.getEditText().getText().toString();
                                    String st2 = mTil2.getEditText().getText().toString();
                                    String st3 = mTil3.getEditText().getText().toString();
                                    String st4 = mTil4.getEditText().getText().toString();
                                    String st5 = mTil5.getEditText().getText().toString();
                                    String st6 = mTil6.getEditText().getText().toString();

                                    if (!TextUtils.isEmpty(st1) && !TextUtils.isEmpty(st2) && !TextUtils.isEmpty(st3) && !TextUtils.isEmpty(st4) && !TextUtils.isEmpty(st5)) {

                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put("Title", st1);
                                        user.put("Company", st2);
                                        user.put("Location", st3);
                                        user.put("From", st4);
                                        user.put("To", st5);
                                        user.put("Industry", st6);

                                        if (WeCount == 1) {

                                            DocumentReference mWeRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                    .collection("WorkExperience").document("WorkExperience1");

                                            mWeRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        String c1 = String.valueOf(EdCount);
                                                        String c2 = String.valueOf(WeCount + 1);
                                                        String c3 = String.valueOf(CertCount);

                                                        HashMap<String, Object> count = new HashMap<>();
                                                        count.put("EdCount", c1);
                                                        count.put("WeCount", c2);
                                                        count.put("CertCount", c3);

                                                        DocumentReference mEdCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                                .collection("Count").document("Count");
                                                        mEdCountRef.set(count, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    UserProfileFragment fragment = new UserProfileFragment();
                                                                    FragmentManager manager = getFragmentManager();
                                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                                    transaction.detach(fragment);
                                                                    transaction.attach(fragment);
                                                                    transaction.commit();

                                                                    mMainProgress.dismiss();

                                                                } else {
                                                                    mMainProgress.dismiss();
                                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        mMainProgress.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        } else if (WeCount == 2) {

                                            DocumentReference mWeRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                    .collection("WorkExperience").document("WorkExperience2");

                                            mWeRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        String c1 = String.valueOf(EdCount);
                                                        String c2 = String.valueOf(WeCount + 1);
                                                        String c3 = String.valueOf(CertCount);

                                                        HashMap<String, Object> count = new HashMap<>();
                                                        count.put("EdCount", c1);
                                                        count.put("WeCount", c2);
                                                        count.put("CertCount", c3);

                                                        DocumentReference mEdCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                                .collection("Count").document("Count");
                                                        mEdCountRef.set(count, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    UserProfileFragment fragment = new UserProfileFragment();
                                                                    FragmentManager manager = getFragmentManager();
                                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                                    transaction.detach(fragment);
                                                                    transaction.attach(fragment);
                                                                    transaction.commit();

                                                                    mMainProgress.dismiss();

                                                                } else {
                                                                    mMainProgress.dismiss();
                                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        mMainProgress.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(getContext(), "No more Work Experience field can be added.", Toast.LENGTH_LONG).show();
                                        }

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

            }
        });

        //****************Skills Details*****************

        mSkillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                mTitle1.setText("Add Skills Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Skills");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                mTil1.getEditText().setText(dbSkills);

                mMsgTxt.setVisibility(View.VISIBLE);
                mMsgTxt.setText("** Keep the SKILLS Comma(,) seprated. **");

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("Skills", st1);

                                    DocumentReference mSkillsRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("Skills").document("Skills");

                                    mSkillsRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


        //****************Certifications Details*****************

        mCertificationsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CertCount>2){

                    Toast.makeText(getContext(), "No more Work Experience field can be added.", Toast.LENGTH_LONG).show();

                }else {

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


                    mTitle1.setText("Certifications Info");

                    mTil1.setVisibility(View.VISIBLE);
                    mTil1.setHint("Position*");
                    mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt1.setVisibility(View.VISIBLE);
                    mTxt1.setText("Ex.: 1st Position");

                    mTil2.setVisibility(View.VISIBLE);
                    mTil2.setHint("Event*");
                    mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt2.setVisibility(View.VISIBLE);
                    mTxt2.setText("Ex.: Smart India Hackathon");

                    mTil3.setVisibility(View.VISIBLE);
                    mTil3.setHint("Organised By*");
                    mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                    mTxt3.setVisibility(View.VISIBLE);
                    mTxt3.setText("Ex.: Govetnment Of India");

                    mTil4.setVisibility(View.VISIBLE);
                    mTil4.setHint("In*");
                    mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                    mTxt4.setVisibility(View.VISIBLE);
                    mTxt4.setText("Ex.: 2018");

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
                                    mMainProgress.setTitle("Uploading Info");
                                    mMainProgress.setMessage("Please wait while we upload your Info");
                                    mMainProgress.setCanceledOnTouchOutside(false);
                                    mMainProgress.show();

                                    String st1 = mTil1.getEditText().getText().toString();
                                    String st2 = mTil2.getEditText().getText().toString();
                                    String st3 = mTil3.getEditText().getText().toString();
                                    String st4 = mTil4.getEditText().getText().toString();

                                    if (!TextUtils.isEmpty(st1) && !TextUtils.isEmpty(st2) && !TextUtils.isEmpty(st3) && !TextUtils.isEmpty(st4)) {

                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put("CName", st1);
                                        user.put("CEvent", st2);
                                        user.put("CAuthority", st3);
                                        user.put("Year", st4);

                                        if (CertCount == 1) {

                                            DocumentReference mCertRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                    .collection("Certifications").document("Certification1");

                                            mCertRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        String c1 = String.valueOf(EdCount);
                                                        String c2 = String.valueOf(WeCount);
                                                        String c3 = String.valueOf(CertCount + 1);

                                                        HashMap<String, Object> count = new HashMap<>();
                                                        count.put("EdCount", c1);
                                                        count.put("WeCount", c2);
                                                        count.put("CertCount", c3);

                                                        DocumentReference mEdCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                                .collection("Count").document("Count");
                                                        mEdCountRef.set(count, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    UserProfileFragment fragment = new UserProfileFragment();
                                                                    FragmentManager manager = getFragmentManager();
                                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                                    transaction.detach(fragment);
                                                                    transaction.attach(fragment);
                                                                    transaction.commit();

                                                                    mMainProgress.dismiss();

                                                                } else {
                                                                    mMainProgress.dismiss();
                                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        mMainProgress.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        } else if (CertCount == 2) {

                                            DocumentReference mCertRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                    .collection("Certifications").document("Certification2");

                                            mCertRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        String c1 = String.valueOf(EdCount);
                                                        String c2 = String.valueOf(WeCount);
                                                        String c3 = String.valueOf(CertCount + 1);

                                                        HashMap<String, Object> count = new HashMap<>();
                                                        count.put("EdCount", c1);
                                                        count.put("WeCount", c2);
                                                        count.put("CertCount", c3);

                                                        DocumentReference mEdCountRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                                                .collection("Count").document("Count");
                                                        mEdCountRef.set(count, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    UserProfileFragment fragment = new UserProfileFragment();
                                                                    FragmentManager manager = getFragmentManager();
                                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                                    transaction.detach(fragment);
                                                                    transaction.attach(fragment);
                                                                    transaction.commit();

                                                                    mMainProgress.dismiss();

                                                                } else {
                                                                    mMainProgress.dismiss();
                                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        mMainProgress.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(getContext(), "No more Work Experience field can be added.", Toast.LENGTH_LONG).show();
                                        }

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

            }
        });


        //****************Languages Details*****************

        mLanguagesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.update_profile_layout, null);

                final TextView mTitle1 = (TextView) mView.findViewById(R.id.Udate_profile_title_txt);
                final Button mBtn1 = (Button) mView.findViewById(R.id.update_profile_btn1);

                final TextInputLayout mTil1 = (TextInputLayout) mView.findViewById(R.id.update_profile_til1);

                final TextView mTxt1 = (TextView) mView.findViewById(R.id.update_profile_txt1);
                final TextView mMsgTxt = (TextView) mView.findViewById(R.id.update_profile_msg_txt);


                mTitle1.setText("Add Languages");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Languages");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                mTil1.getEditText().setText(dbLanguages);

                mMsgTxt.setVisibility(View.VISIBLE);
                mMsgTxt.setText("** Keep the LANGUAGES Comma(,) seprated. **");


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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("Languages", st1);

                                    DocumentReference mLanguageRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("Languages").document("Languages");

                                    mLanguageRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


        //****************Tags Details*****************

        mTagsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.update_profile_layout, null);

                final TextView mTitle1 = (TextView) mView.findViewById(R.id.Udate_profile_title_txt);
                final TextView mMsgTxt = (TextView) mView.findViewById(R.id.update_profile_msg_txt);

                final CheckBox mCb1 = (CheckBox) mView.findViewById(R.id.update_profile_checkBox1);
                final CheckBox mCb2 = (CheckBox) mView.findViewById(R.id.update_profile_checkBox2);
                final CheckBox mCb3 = (CheckBox) mView.findViewById(R.id.update_profile_checkBox3);

                mTitle1.setText("Add Tags");

                mCb1.setVisibility(View.VISIBLE);
                mCb2.setVisibility(View.VISIBLE);
                mCb3.setVisibility(View.VISIBLE);

                mMsgTxt.setVisibility(View.VISIBLE);
                mMsgTxt.setText("** Add your fields of Intrest**");

                if (mEduTag) {
                    mCb1.setChecked(true);
                }

                if (mTechTag) {
                    mCb2.setChecked(true);
                }

                if (mSportTag) {
                    mCb3.setChecked(true);
                }

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                Boolean mETag;
                                Boolean mTTag;
                                Boolean mSTag;

                                if (mCb1.isChecked()) {
                                    mETag = true;
                                } else {
                                    mETag = false;
                                }

                                if (mCb2.isChecked()) {
                                    mTTag = true;
                                } else {
                                    mTTag = false;
                                }

                                if (mCb3.isChecked()) {
                                    mSTag = true;
                                } else {
                                    mSTag = false;
                                }

                                HashMap<String, Object> user = new HashMap<>();
                                user.put("EduTag", mETag);
                                user.put("TechTag", mTTag);
                                user.put("SportTag", mSTag);

                                DocumentReference mTagRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                        .collection("Tags").document("Tags");

                                mTagRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            UserProfileFragment fragment = new UserProfileFragment();
                                            FragmentManager manager = getFragmentManager();
                                            FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.detach(fragment);
                                            transaction.attach(fragment);
                                            transaction.commit();

                                            mMainProgress.dismiss();
                                        } else {
                                            mMainProgress.dismiss();
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                dialogInterface.dismiss();
                            }
                        });

                mBulider.setView(mView);
                AlertDialog dialog = mBulider.create();
                dialog.show();

            }
        });


        //**************************************
        //************ Long Press **************
        //**************************************

        mEd1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


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


                mTitle1.setText("Education Info");

//                mBtn1.setVisibility(View.VISIBLE);
//                mBtn1.setText("Delete");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("School");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Ex.: STCET, Kolkata");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Degree");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt2.setVisibility(View.VISIBLE);
                mTxt2.setText("Ex.: B.Tech");

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Field of Study");
                mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: Information Technology");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("Grade");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("Ex.: 7.5 CGPA");

                mTil5.setVisibility(View.VISIBLE);
                mTil5.setHint("Starting Year");
                mTil5.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt5.setVisibility(View.VISIBLE);
                mTxt5.setText("Ex.: 2012");

                mTil6.setVisibility(View.VISIBLE);
                mTil6.setHint("Finishing Year");
                mTil6.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt6.setVisibility(View.VISIBLE);
                mTxt6.setText("Ex.: 2016");

                mTil1.getEditText().setText(dbSchool1);
                mTil2.getEditText().setText(dbDegree1);
                mTil3.getEditText().setText(dbFieldOfStudy1);
                mTil4.getEditText().setText(dbGrade1);
                mTil5.getEditText().setText(dbStartedIn1);
                mTil6.getEditText().setText(dbCompletedIn1);


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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
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
                                    user.put("School", st1);
                                    user.put("Degree", st2);
                                    user.put("FieldOfStudy", st3);
                                    user.put("Grade", st4);
                                    user.put("StartedIn", st5);
                                    user.put("CompletedIn", st6);


                                    DocumentReference mEdRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("Education").document("Education1");

                                    mEdRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


                return false;
            }
        });


        mEd2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


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


                mTitle1.setText("Education Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("School");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Ex.: STCET, Kolkata");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Degree");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt2.setVisibility(View.VISIBLE);
                mTxt2.setText("Ex.: B.Tech");

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Field of Study");
                mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: Information Technology");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("Grade");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("Ex.: 7.5 CGPA");

                mTil5.setVisibility(View.VISIBLE);
                mTil5.setHint("Starting Year");
                mTil5.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt5.setVisibility(View.VISIBLE);
                mTxt5.setText("Ex.: 2012");

                mTil6.setVisibility(View.VISIBLE);
                mTil6.setHint("Finishing Year");
                mTil6.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt6.setVisibility(View.VISIBLE);
                mTxt6.setText("Ex.: 2016");

                mTil1.getEditText().setText(dbSchool2);
                mTil2.getEditText().setText(dbDegree2);
                mTil3.getEditText().setText(dbFieldOfStudy2);
                mTil4.getEditText().setText(dbGrade2);
                mTil5.getEditText().setText(dbStartedIn2);
                mTil6.getEditText().setText(dbCompletedIn2);

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
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
                                    user.put("School", st1);
                                    user.put("Degree", st2);
                                    user.put("FieldOfStudy", st3);
                                    user.put("Grade", st4);
                                    user.put("StartedIn", st5);
                                    user.put("CompletedIn", st6);


                                    DocumentReference mEdRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("Education").document("Education2");

                                    mEdRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


                return false;
            }
        });


        mWe1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


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


                mTitle1.setText("Work Experience Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Title*");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Ex.: Manager");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Company*");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Location*");
                mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: Kolkata, WB");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("From*");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("Ex.: 2011");

                mTil5.setVisibility(View.VISIBLE);
                mTil5.setHint("To*");
                mTil5.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt5.setVisibility(View.VISIBLE);
                mTxt5.setText("Ex.: 2015");

                mTil6.setVisibility(View.VISIBLE);
                mTil6.setHint("Industry");
                mTil6.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt6.setVisibility(View.VISIBLE);
                mTxt6.setText("Ex.: IT Sector");

                mTil1.getEditText().setText(dbTitle1);
                mTil2.getEditText().setText(dbCompany1);
                mTil3.getEditText().setText(dbLocation1);
                mTil4.getEditText().setText(dbFrom1);
                mTil5.getEditText().setText(dbTo1);
                mTil6.getEditText().setText(dbIndustry1);

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
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
                                    user.put("Title", st1);
                                    user.put("Company", st2);
                                    user.put("Location", st3);
                                    user.put("From", st4);
                                    user.put("To", st5);
                                    user.put("Industry", st6);

                                    DocumentReference mWeRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("WorkExperience").document("WorkExperience1");

                                    mWeRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


                return false;
            }
        });


        mWe2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


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


                mTitle1.setText("Work Experience Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Title*");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Ex.: Manager");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Company*");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Location*");
                mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: Kolkata, WB");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("From*");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("Ex.: 2011");

                mTil5.setVisibility(View.VISIBLE);
                mTil5.setHint("To*");
                mTil5.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt5.setVisibility(View.VISIBLE);
                mTxt5.setText("Ex.: 2015");

                mTil6.setVisibility(View.VISIBLE);
                mTil6.setHint("Industry");
                mTil6.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

                mTxt6.setVisibility(View.VISIBLE);
                mTxt6.setText("Ex.: IT Sector");

                mTil1.getEditText().setText(dbTitle2);
                mTil2.getEditText().setText(dbCompany2);
                mTil3.getEditText().setText(dbLocation2);
                mTil4.getEditText().setText(dbFrom2);
                mTil5.getEditText().setText(dbTo2);
                mTil6.getEditText().setText(dbIndustry2);

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
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
                                    user.put("Title", st1);
                                    user.put("Company", st2);
                                    user.put("Location", st3);
                                    user.put("From", st4);
                                    user.put("To", st5);
                                    user.put("Industry", st6);

                                    DocumentReference mWeRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("WorkExperience").document("WorkExperience2");

                                    mWeRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


                return false;
            }
        });


        mCert1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


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

                mTitle1.setText("Certifications Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Position*");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Ex.: 1st Position");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Event*");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt2.setVisibility(View.VISIBLE);
                mTxt2.setText("Ex.: Smart India Hackathon");

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Organised By*");
                mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: Govetnment Of India");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("In*");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("Ex.: 2018");

                mTil1.getEditText().setText(dbCName1);
                mTil2.getEditText().setText(dbCEvent1);
                mTil3.getEditText().setText(dbCAuthority1);
                mTil4.getEditText().setText(dbYear1);

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();
                                String st2 = mTil2.getEditText().getText().toString();
                                String st3 = mTil3.getEditText().getText().toString();
                                String st4 = mTil4.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1) && !TextUtils.isEmpty(st2) && !TextUtils.isEmpty(st3) && !TextUtils.isEmpty(st4)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("CName", st1);
                                    user.put("CEvent", st2);
                                    user.put("CAuthority", st3);
                                    user.put("Year", st4);

                                    DocumentReference mCertRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("Certifications").document("Certification1");

                                    mCertRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

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


                return false;
            }
        });


        mCert2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                final AlertDialog.Builder mBulider = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.update_profile_layout, null);

                final TextView mTitle1 = (TextView) mView.findViewById(R.id.Udate_profile_title_txt);
                final Button mBtn1 = (Button) mView.findViewById(R.id.update_profile_btn1);

                final TextInputLayout mTil1 = (TextInputLayout) mView.findViewById(R.id.update_profile_til1);
                final TextInputLayout mTil2 = (TextInputLayout) mView.findViewById(R.id.update_profile_til2);
                final TextInputLayout mTil3 = (TextInputLayout) mView.findViewById(R.id.update_profile_til3);
                final TextInputLayout mTil4 = (TextInputLayout) mView.findViewById(R.id.update_profile_til4);

                final TextView mTxt1 = (TextView) mView.findViewById(R.id.update_profile_txt1);
                final TextView mTxt2 = (TextView) mView.findViewById(R.id.update_profile_txt2);
                final TextView mTxt3 = (TextView) mView.findViewById(R.id.update_profile_txt3);
                final TextView mTxt4 = (TextView) mView.findViewById(R.id.update_profile_txt4);


                mTitle1.setText("Certifications Info");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Position*");
                mTil1.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt1.setVisibility(View.VISIBLE);
                mTxt1.setText("Ex.: 1st Position");

                mTil2.setVisibility(View.VISIBLE);
                mTil2.setHint("Event*");
                mTil2.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt2.setVisibility(View.VISIBLE);
                mTxt2.setText("Ex.: Smart India Hackathon");

                mTil3.setVisibility(View.VISIBLE);
                mTil3.setHint("Organised By*");
                mTil3.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                mTxt3.setVisibility(View.VISIBLE);
                mTxt3.setText("Ex.: Govetnment Of India");

                mTil4.setVisibility(View.VISIBLE);
                mTil4.setHint("In*");
                mTil4.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);

                mTxt4.setVisibility(View.VISIBLE);
                mTxt4.setText("Ex.: 2018");

                mTil1.getEditText().setText(dbCName2);
                mTil2.getEditText().setText(dbCEvent2);
                mTil3.getEditText().setText(dbCAuthority2);
                mTil4.getEditText().setText(dbYear2);

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
                                mMainProgress.setTitle("Uploading Info");
                                mMainProgress.setMessage("Please wait while we upload your Info");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();
                                String st2 = mTil2.getEditText().getText().toString();
                                String st3 = mTil3.getEditText().getText().toString();
                                String st4 = mTil4.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1) && !TextUtils.isEmpty(st2) && !TextUtils.isEmpty(st3) && !TextUtils.isEmpty(st4)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("CName", st1);
                                    user.put("CEvent", st2);
                                    user.put("CAuthority", st3);
                                    user.put("Year", st4);

                                    DocumentReference mCertRef = FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("Certifications").document("Certification2");

                                    mCertRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                UserProfileFragment fragment = new UserProfileFragment();
                                                FragmentManager manager = getFragmentManager();
                                                FragmentTransaction transaction = manager.beginTransaction();
                                                transaction.detach(fragment);
                                                transaction.attach(fragment);
                                                transaction.commit();

                                                mMainProgress.hide();

                                            } else {
                                                mMainProgress.hide();
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                    dialogInterface.dismiss();
                                } else {
                                    mMainProgress.hide();
                                    Toast.makeText(getActivity(), "Fill all the fields and try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                mBulider.setView(mView);
                AlertDialog dialog = mBulider.create();
                dialog.show();


                return false;
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mMainProgress = new ProgressDialog(getContext());
                mMainProgress.setTitle("Uploading Image");
                mMainProgress.setMessage("Please wait while we upload the image");
                mMainProgress.setCanceledOnTouchOutside(false);
                mMainProgress.show();

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(getContext())
                            .setMaxHeight(400)
                            .setMaxWidth(400)
                            .setQuality(10)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = mImageStorage.child("Profile_Image").child(uid + ".jpg");

                UploadTask uploadTask = filepath.putBytes(thumb_byte);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            String downloadUrl = task.getResult().getDownloadUrl().toString();

                            Map<String, Object> users = new HashMap<>();
                            users.put("image", downloadUrl);

                            DocumentReference mDoc = FirebaseFirestore.getInstance().collection("Users").document(uid);

                            mDoc.set(users, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {


                                        UserProfileFragment fragment = new UserProfileFragment();
                                        FragmentManager manager = getFragmentManager();
                                        FragmentTransaction transaction = manager.beginTransaction();
                                        transaction.detach(fragment);
                                        transaction.attach(fragment);
                                        transaction.commit();

                                        Toast.makeText(getActivity(), "Successfully uplodad the image", Toast.LENGTH_LONG).show();

                                        mMainProgress.dismiss();

                                    } else {

                                        mMainProgress.dismiss();
                                        Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();

                                    }

                                }
                            });

                        } else {

                            Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

                //Toast.makeText(getActivity(),resultUri.toString(),Toast.LENGTH_LONG).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


}
