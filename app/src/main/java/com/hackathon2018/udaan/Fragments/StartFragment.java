package com.hackathon2018.udaan.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackathon2018.udaan.Activities.MainActivity;
import com.hackathon2018.udaan.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    private Button mGoogleLoginBtn;
    private Button mMailLoginBtn;


    private static final int RC_SIGN_IN = 1;
    private GoogleSignInApi mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mAuth;
    private DocumentReference mDocRef;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mGoogleApiClient.stopAutoManage(getActivity());
//        mGoogleApiClient.disconnect();
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager_walkThrough);
        dotsLayout = (LinearLayout) getActivity().findViewById(R.id.layoutDots_walkThrough);

        layouts = new int[]{
                R.layout.walkthrough_1,
                R.layout.walkthrough_2,
                R.layout.walkthrough_3,
                R.layout.walkthrough_4,
                R.layout.walkthrough_5};

        // adding bottom dots
        addBottomDots(0);

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        mGoogleLoginBtn = (Button)getActivity().findViewById(R.id.google_login_btn);
        mMailLoginBtn = (Button) getActivity().findViewById(R.id.mail_login_btn);



        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                        Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
//
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();



        mGoogleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                signIn();

                LoginFragment fragment = new LoginFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                transaction.replace(R.id.start_activity_layout,fragment,"AddRestaurantFragment");
                transaction.addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.commit();


            }
        });

        mMailLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UidCheckFragment fragment = new UidCheckFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                transaction.replace(R.id.start_activity_layout,fragment,"AddRestaurantFragment");
                transaction.addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.commit();

            }
        });




    }



//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN){
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()){
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            }else {
//                Toast.makeText(getActivity(),"Auth went wrong",Toast.LENGTH_LONG).show();
//                Log.d("TAG","problem");
//            }
//        }
//
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//
//                            Intent mainIntent = new Intent(getActivity(),MainActivity.class);
//                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(mainIntent);
//                            getActivity().finish();
//
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(getActivity(), task.getException().getMessage(),
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//
//    }




    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == layouts.length - 1) {
//                // last page. make button text to GOT IT
//                btnNext.setText(getString(R.string.start));
//                btnSkip.setVisibility(View.GONE);
//            } else {
//                // still pages are left
//                btnNext.setText(getString(R.string.next));
//                btnSkip.setVisibility(View.VISIBLE);
//            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));
    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }



    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;


        public ViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
