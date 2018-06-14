package com.hackathon2018.udaan.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hackathon2018.udaan.Activities.MainActivity;
import com.hackathon2018.udaan.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {


    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mRetypePassword;
    private Button mCreateBtn;

    private ProgressDialog mRegProgress;

    private FirebaseAuth mAuth;
    private DocumentReference mDocRef;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(getContext());


        mEmail = (TextInputLayout) getActivity().findViewById(R.id.signup_email);
        mPassword = (TextInputLayout) getActivity().findViewById(R.id.signup_password);
        mRetypePassword = (TextInputLayout) getActivity().findViewById(R.id.signup_repassword);
        mCreateBtn = (Button) getActivity().findViewById(R.id.signup_btn_create);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String repassword = mRetypePassword.getEditText().getText().toString();

                if (password.equals(repassword)) {

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                        mRegProgress.setTitle("Registering User");
                        mRegProgress.setMessage("Please wait while we create your Account.");
                        mRegProgress.setCanceledOnTouchOutside(false);
                        mRegProgress.show();

                        register_user(email, password);

                    } else {

                        Toast.makeText(getActivity(), "Please fill all the fields and try again", Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(getActivity(), "Password do not match", Toast.LENGTH_LONG).show();

                }
            }

        });


    }


    private void register_user(final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String uid = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> user = new HashMap<>();
                    user.put("Aadhaar", UidCheckFragment.uidAadhaar);
                    user.put("Name", UidCheckFragment.uidName);
                    user.put("Dob", UidCheckFragment.uidDob);
                    user.put("Phone", UidCheckFragment.uidPhone);
                    user.put("City", UidCheckFragment.uidCity);
                    user.put("State", UidCheckFragment.uidState);
                    user.put("Email", email);
                    user.put("displayName", "Display Name");
                    user.put("status", "Sapno ki Udaan");
                    user.put("image", "");
                    user.put("news", "notSubscribed");

                    DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("Users").document(uid);

                    mDocRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                getActivity().finish();

                                mRegProgress.dismiss();

                            }else {
                                Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                mRegProgress.dismiss();
                            }
                        }
                    });

                } else {

                    mRegProgress.dismiss();
                    Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();

                }

            }
        });
    }

}
