package com.hackathon2018.udaan.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackathon2018.udaan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UidCheckFragment extends Fragment {

    private TextInputLayout mAadhaarNo, mName, mDob, mPhone, mCity, mState;
    private Button mCheck;

    private ProgressDialog mRegProgress;

    public static String uidAadhaar, uidName, uidDob, uidPhone, uidCity, uidState;


    public UidCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uid_check, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRegProgress = new ProgressDialog(getContext());

        mAadhaarNo = (TextInputLayout)getActivity().findViewById(R.id.uid_aadhaar_til);
        mName = (TextInputLayout)getActivity().findViewById(R.id.uid_name_til);
        mDob = (TextInputLayout)getActivity().findViewById(R.id.uid_dob_til);
        mPhone = (TextInputLayout)getActivity().findViewById(R.id.uid_phone_til);
        mCity = (TextInputLayout)getActivity().findViewById(R.id.uid_city_til);
        mState = (TextInputLayout)getActivity().findViewById(R.id.uid_state_til);

        mCheck = (Button)getActivity().findViewById(R.id.uid_check_btn);

        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRegProgress.setTitle("Checking Information");
                mRegProgress.setMessage("Please wait while we check your Information.");
                mRegProgress.setCanceledOnTouchOutside(false);
                mRegProgress.show();

                uidAadhaar = mAadhaarNo.getEditText().getText().toString();
                uidName = mName.getEditText().getText().toString();
                uidDob = mDob.getEditText().getText().toString();
                uidPhone = mPhone.getEditText().getText().toString();
                uidCity = mCity.getEditText().getText().toString();
                uidState = mState.getEditText().getText().toString();

                DocumentReference mCheckRef = FirebaseFirestore.getInstance().collection("Uid").document(uidAadhaar);

                mCheckRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){

                            String dbName = documentSnapshot.getString("name");
                            String dbGender = documentSnapshot.getString("gender");
                            String dbDob = documentSnapshot.getString("dob");

                            if (dbGender.equals("Female")){

                                if (dbName.equals(uidName) && dbDob.equals(uidDob)){

                                    SignupFragment fragment = new SignupFragment();
                                    FragmentManager manager = getFragmentManager();
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                                    transaction.replace(R.id.start_activity_layout,fragment,"AddRestaurantFragment");
                                    transaction.addToBackStack(null);
                                    transaction.setReorderingAllowed(true);
                                    transaction.commit();

                                    mRegProgress.dismiss();

                                }else {
                                    Toast.makeText(getContext(),"Incorrect Data",Toast.LENGTH_LONG).show();
                                    mRegProgress.dismiss();
                                }

                            }else {
                                Toast.makeText(getContext(),"Only FEMALE users are allowed.",Toast.LENGTH_LONG).show();
                                mRegProgress.dismiss();
                            }

                        }else {
                            Toast.makeText(getContext(),"User do not exist",Toast.LENGTH_LONG).show();
                            mRegProgress.dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        mRegProgress.dismiss();
                    }
                });

            }
        });

    }
}
