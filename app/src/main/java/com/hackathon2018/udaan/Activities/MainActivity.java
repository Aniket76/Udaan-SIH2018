package com.hackathon2018.udaan.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hackathon2018.udaan.AppData.AppStorage;
import com.hackathon2018.udaan.Fragments.FindJobsFragment;
import com.hackathon2018.udaan.Fragments.MainFragment;
import com.hackathon2018.udaan.Fragments.SSPIndia;
import com.hackathon2018.udaan.Fragments.UidCheckFragment;
import com.hackathon2018.udaan.Fragments.UserProfileFragment;
import com.hackathon2018.udaan.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private android.support.v7.widget.Toolbar mToolbar;

    private BottomNavigationView mBottomNavigationItemView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(startIntent);
            finish();

        }else {

            System.out.println("anubhav "+ FirebaseInstanceId.getInstance().getToken());

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(uid);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String username = documentSnapshot.getString("displayName");
                    AppStorage.storeStringInSF(MainActivity.this,AppStorage.USERNAME,username);
                }
            });

            HashMap<String, Object> user = new HashMap<>();
            user.put("token", FirebaseInstanceId.getInstance().getToken());

            documentReference.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        MainFragment mainFragment = new MainFragment();
                        android.support.v4.app.FragmentManager mainManager = getSupportFragmentManager();
                        FragmentTransaction mainTransaction = mainManager.beginTransaction();
                        mainTransaction.add(R.id.main_activity_layout, mainFragment, "MainFragment");
                        mainTransaction.commit();
                    }
                }
            });

        }


        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setLogo(R.drawable.chat_icon);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView)findViewById(R.id.main_side_nav);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser == null){
//
//            Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
//            startActivity(startIntent);
//            finish();
//
//        }else {
//
//            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch (id){

            case R.id.main_menu_search:

                Toast.makeText(MainActivity.this,"Panic Button",Toast.LENGTH_SHORT).show();

//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Common English Vocabulary App");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.side_nav_btn0:

                UserProfileFragment fragment = new UserProfileFragment();
                android.support.v4.app.FragmentManager mainManager = getSupportFragmentManager();
                FragmentTransaction transaction = mainManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                transaction.replace(R.id.main_activity_layout, fragment, "UserProfile");
                transaction.addToBackStack(null);
                transaction.setReorderingAllowed(true);
                transaction.commit();

                mDrawerLayout.closeDrawers();

                break;

            case R.id.side_nav_btn1:

                Toast.makeText(MainActivity.this,"Share",Toast.LENGTH_SHORT).show();

//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Common English Vocabulary App");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.side_nav_btn2:

                Toast.makeText(MainActivity.this,"RateUs",Toast.LENGTH_SHORT).show();

//                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//                        .parse("https://play.google.com/store/apps/details?id=com.aniketvishal.commonindianwords")));
                break;

            case R.id.side_nav_btn3:

                Toast.makeText(MainActivity.this,"Feedback",Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "contact@aniketvishal.com"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Common English Vocabulary App - Feedback");
//                startActivity(intent);
                break;

            case R.id.side_nav_otherweb:
                SSPIndia fragment1 = new SSPIndia();
                android.support.v4.app.FragmentManager mainManager1 = getSupportFragmentManager();
                FragmentTransaction transaction1 = mainManager1.beginTransaction();
                transaction1.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                transaction1.replace(R.id.main_activity_layout, fragment1, "UserProfile");
                transaction1.addToBackStack(null);
                transaction1.setReorderingAllowed(true);
                transaction1.commit();

                mDrawerLayout.closeDrawers();
                    break;


            case R.id.side_nav_jobs:
                FindJobsFragment fragment2 = new FindJobsFragment();
                android.support.v4.app.FragmentManager mainManager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = mainManager2.beginTransaction();
                transaction2.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
                transaction2.replace(R.id.main_activity_layout, fragment2, "UserProfile");
                transaction2.addToBackStack(null);
                transaction2.setReorderingAllowed(true);
                transaction2.commit();

                mDrawerLayout.closeDrawers();
                break;

            case R.id.side_nav_btn4:

                // 1. Instantiate an AlertDialog.Builder with its constructor
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Are you sure you want to logout?")
                        .setTitle("Logout");

                // 3. Add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked YES button

                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this,StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();

                    }
                });

                // 4. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }

        return false;
    }
}
