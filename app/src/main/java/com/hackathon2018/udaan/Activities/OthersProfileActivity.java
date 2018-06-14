package com.hackathon2018.udaan.Activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hackathon2018.udaan.Adapters.SearchListAdapter;
import com.hackathon2018.udaan.AppData.AppStorage;
import com.hackathon2018.udaan.R;
import com.hackathon2018.udaan.asyncTasks.AsyncResponse;
import com.hackathon2018.udaan.asyncTasks.RequestAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class OthersProfileActivity extends AppCompatActivity implements AsyncResponse {


    private TextView mName, mStatus;
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

    private Button mChatRequestBtn, mDeclineBtn, mAcceptBtn;
    private String chatStatus;
    private String requestType;

    private CircleImageView mDisplayPic;

    private ProgressDialog mMainProgress;

    private FirebaseAuth mAuth;
    private StorageReference mImageStorage;

    private static String uid;
    private String currentUserUid;

    private String dbName, dbStatus;
    private String dbDp;
    private String dbSkills;
    private String dbLanguages;


    String to,token,tokenOtherUser,Username,dbImage;
    String currentUserName,currentUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        uid = getIntent().getStringExtra("UsersId");

        mDisplayPic = (CircleImageView) findViewById(R.id.otherProfile_dp_img);

        mName = (TextView) findViewById(R.id.otherProfile_name_txt);
        mStatus = (TextView) findViewById(R.id.otherProfile_about_txt);

        mChatRequestBtn = (Button) findViewById(R.id.op_chatRequest_btn);
        mDeclineBtn = (Button) findViewById(R.id.op_decline_btn);
        mAcceptBtn = (Button) findViewById(R.id.op_accept_btn);

        //**************Education Section******************

        mEd1 = (TextView) findViewById(R.id.otherProfile_education1_txt);
        mEd2 = (TextView) findViewById(R.id.otherProfile_education2_txt);

        mLlEd1 = (LinearLayout) findViewById(R.id.otherProfile_education1_layout);
        mLlEd2 = (LinearLayout) findViewById(R.id.otherProfile_education2_layout);

        //**************WE Section******************

        mWe1 = (TextView) findViewById(R.id.otherProfile_we1_txt);
        mWe2 = (TextView) findViewById(R.id.otherProfile_we2_txt);

        mLlWe1 = (LinearLayout) findViewById(R.id.otherProfile_we1_layout);
        mLlWe2 = (LinearLayout) findViewById(R.id.otherProfile_we2_layout);

        //**************Skills Section******************

        mSkills = (TextView) findViewById(R.id.otherProfile_skills1_txt);

        //**************Certification Section******************

        mCert1 = (TextView) findViewById(R.id.otherProfile_certifications1_txt);
        mCert2 = (TextView) findViewById(R.id.otherProfile_certifications2_txt);

        mLlCert1 = (LinearLayout) findViewById(R.id.otherProfile_certifications1_layout);
        mLlCert2 = (LinearLayout) findViewById(R.id.otherProfile_certifications2_layout);

        //**************Languages Section******************

        mLanguages = (TextView) findViewById(R.id.otherProfile_languages1_txt);

        //**************Tags Section******************

        mTags = (TextView) findViewById(R.id.otherProfile_tags1_txt);

        // **************Retrieving Data from Firestore***************

        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        FirebaseUser current_user = mAuth.getCurrentUser();
        currentUserUid = current_user.getUid();

        chatStatus = "notFriends";


        //****************** Current User Name and Status **********************

        DocumentReference mCurrentNameRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserUid);
        mCurrentNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    currentUserName = documentSnapshot.getString("displayName");
                    currentUserImage = documentSnapshot.getString("image");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        //****************** Name and Status **********************

        DocumentReference mNameRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    System.out.println("Anubhav user data : "+documentSnapshot.toString());
                    dbName = documentSnapshot.getString("displayName");
                    dbStatus = documentSnapshot.getString("status");
                    tokenOtherUser = documentSnapshot.getString("token");
                    dbImage = documentSnapshot.getString("image");

                    setChatStatus();

                    if (!(dbName.equals(""))) {
                        mName.setText(dbName);
                    }
                    if (!(dbStatus.equals(""))) {
                        mStatus.setText(dbStatus);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        //****************** Chat Request Button **********************

//        if (!(currentUserUid.equals(uid))){
//            mChatRequestBtn.setVisibility(View.GONE);
//        }

        mChatRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mChatRequestBtn.setEnabled(false);

                if (chatStatus.equals("friends")) {

                    DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("Chats").document(currentUserUid)
                            .collection("Chats").document(uid);

                    mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("Chats").document(uid)
                                    .collection("Chats").document(currentUserUid);

                            mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(OthersProfileActivity.this, "Deleted", Toast.LENGTH_LONG).show();

                                    mChatRequestBtn.setText("Send Chat Request");
                                    mChatRequestBtn.setVisibility(View.VISIBLE);
                                    mDeclineBtn.setVisibility(View.GONE);
                                    mAcceptBtn.setVisibility(View.GONE);

                                    chatStatus = "notFriends";

                                    mChatRequestBtn.setEnabled(true);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else if (chatStatus.equals("notFriends")) {

//                    String uid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(currentUserUid);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            token = documentSnapshot.getString("token");
                            AppStorage.storeStringInSF(OthersProfileActivity.this,AppStorage.TOKEN,token);
                        }
                    });

                    FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
                    DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(currentUserUid)
                            .collection("ChatRequest").document(uid);
                    Username = AppStorage.getStringFromSF(OthersProfileActivity.this,AppStorage.USERNAME);
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("requestType", "sent");
                    user.put("imageurl",dbImage );
                    user.put("name", dbName);

                    mChatRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            try {



                                JSONObject notification = new JSONObject();
                                notification.put("body","Open Udaan");
                                notification.put("title",Username+" send you a friend request");

                                JSONObject data = new JSONObject();
                                data.put("body","Open Udaan");
                                data.put("title",Username+" send you a friend request");
                                data.put("key_1",dbImage);
                                data.put("key_2","Hellowww");

                                JSONObject params1 = new JSONObject();
                                params1.put("to",tokenOtherUser );
                                params1.put("collapse_key","type_a");
                                params1.put("notification", notification);
                                params1.put("data",data);
                                RequestAPI requestAPI=new RequestAPI();
                                requestAPI.delegate=OthersProfileActivity.this;

                                System.out.println("ANUBHAV REQUEST jSON "+Username +", "+tokenOtherUser);

//                                String jsonData = "{\"to\" : \"dZTLV5-mF5U:APA91bGxTYEz9SENN88H9hWzeRvDKcbHXLKPrPoI99_5vtXWbP70j2151aVY94bXYtyBHsHz94ifzVoMoMKroTEEXuLxc3woHHLysYJ3HJUE3l356RmTpETk2WxlZpIIhI_4odk5h8-j\",\"collapse_key\" : \"type_a\",\"notification\" : {\"body\" : \"body 1\",\"title\": \"title 1\"},\"data\" : {\"body\" : \"body 2\",\"title\": \"title 2\",\"key_1\" : \"Data for key one\",\"key_2\" : \"Hellowww\"}}";

                                requestAPI.execute("https://fcm.googleapis.com/fcm/send", "POST", "key=AAAAj9CkaVk:APA91bFtvc2udrKwo6qbbbYeypu1sO0Vq3tT_A6598cNIp5c1umQxowltVnIke_ZFTY2dvtzkB8sR0pCRKSQO3QjNus1jdJmLvfpOz-HJ_6p6v0QuM9n3nr9dljt5F0bK2oGZN7wH7ar",params1.toString(),"application/json","Udaan");

                            }catch (Exception e) {
                                e.printStackTrace();
                            }

                            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
//                            MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
//                            myFirebaseMessagingService.sendNotification("Anubhav Test","Anubhav Body");
                            DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(uid)
                                    .collection("ChatRequest").document(currentUserUid);

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("requestType", "receive");
                            user.put("imageurl",currentUserImage );
                            user.put("name", currentUserName);

                            mChatRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    Toast.makeText(OthersProfileActivity.this, "Request Sent", Toast.LENGTH_LONG).show();
                                    mChatRequestBtn.setText("Cancel Chat Request");
                                    mChatRequestBtn.setVisibility(View.VISIBLE);
                                    mDeclineBtn.setVisibility(View.GONE);
                                    mAcceptBtn.setVisibility(View.GONE);

                                    chatStatus = "sentRequest";

                                    mChatRequestBtn.setEnabled(true);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else if (chatStatus.equals("sentRequest")) {

                    DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(currentUserUid)
                            .collection("ChatRequest").document(uid);

                    mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(uid)
                                    .collection("ChatRequest").document(currentUserUid);

                            mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(OthersProfileActivity.this, "Canceled", Toast.LENGTH_LONG).show();

                                    mChatRequestBtn.setText("Send Chat Request");
                                    mChatRequestBtn.setVisibility(View.VISIBLE);
                                    mDeclineBtn.setVisibility(View.GONE);
                                    mAcceptBtn.setVisibility(View.GONE);

                                    chatStatus = "notFriends";

                                    mChatRequestBtn.setEnabled(true);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });

        mAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAcceptBtn.setEnabled(false);

                if (chatStatus.equals("receiveRequest")) {

                    DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(currentUserUid)
                            .collection("ChatRequest").document(uid);

                    mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(uid)
                                    .collection("ChatRequest").document(currentUserUid);

                            mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("Chats").document(currentUserUid)
                                            .collection("Chats").document(uid);

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("date", Calendar.getInstance().getTime().toString());
                                    user.put("imageurl",dbImage );
                                    user.put("name", dbName);

                                    mChatRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("Chats").document(uid)
                                                    .collection("Chats").document(currentUserUid);

                                            HashMap<String, Object> user = new HashMap<>();
                                            user.put("date", Calendar.getInstance().getTime().toString());
                                            user.put("imageurl",currentUserImage );
                                            user.put("name", currentUserName);

                                            mChatRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(OthersProfileActivity.this, "Accepted", Toast.LENGTH_LONG).show();
//
                                                    mChatRequestBtn.setText("Delete Chat");
                                                    mChatRequestBtn.setVisibility(View.VISIBLE);
                                                    mDeclineBtn.setVisibility(View.GONE);
                                                    mAcceptBtn.setVisibility(View.GONE);

                                                    chatStatus = "friends";

                                                    mAcceptBtn.setEnabled(true);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
        });

        mDeclineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDeclineBtn.setEnabled(false);

                if (chatStatus.equals("receiveRequest")) {

                    DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(currentUserUid)
                            .collection("ChatRequest").document(uid);

                    mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(uid)
                                    .collection("ChatRequest").document(currentUserUid);

                            mChatRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(OthersProfileActivity.this, "Decline", Toast.LENGTH_LONG).show();

                                    mChatRequestBtn.setText("Send Chat Request");
                                    mChatRequestBtn.setVisibility(View.VISIBLE);
                                    mDeclineBtn.setVisibility(View.GONE);
                                    mAcceptBtn.setVisibility(View.GONE);

                                    chatStatus = "notFriends";

                                    mDeclineBtn.setEnabled(true);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });


        //********************DP**********************

        DocumentReference mDpRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mDpRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    dbDp = documentSnapshot.getString("image");

                    Picasso.with(OthersProfileActivity.this).load(dbDp).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.profile_icon).into(mDisplayPic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(OthersProfileActivity.this).load(dbDp).placeholder(R.drawable.profile_icon).into(mDisplayPic);
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setChatStatus() {

//        Toast.makeText(OthersProfileActivity.this, "In checkChatStatus", Toast.LENGTH_LONG).show();

        DocumentReference mChatRef = FirebaseFirestore.getInstance().collection("ChatRequest").document(currentUserUid)
                .collection("ChatRequest").document(uid);

        mChatRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    requestType = documentSnapshot.getString("requestType");

                    if (requestType.equals("sent")) {

                        chatStatus = "sentRequest";
                        checkChatStatus();

                    } else {

//                        Toast.makeText(OthersProfileActivity.this, "checkChatStatus is Running", Toast.LENGTH_LONG).show();

                        chatStatus = "receiveRequest";
                        checkChatStatus();

                    }

                } else {

                    DocumentReference mChatRequestRef = FirebaseFirestore.getInstance().collection("Chats").document(currentUserUid)
                            .collection("Chats").document(uid);

                    mChatRequestRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()){

                                chatStatus = "friends";
                                checkChatStatus();

                            }else {

                                chatStatus = "notFriends";
                                checkChatStatus();

                            }

                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OthersProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void checkChatStatus(){

        if (chatStatus.equals("friends")) {

            if (currentUserUid.equals(uid)) {
                mChatRequestBtn.setVisibility(View.GONE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);
            } else {

                mChatRequestBtn.setText("Delete Chat");
                mChatRequestBtn.setVisibility(View.VISIBLE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);

            }

        } else if (chatStatus.equals("notFriends")) {

            if (currentUserUid.equals(uid)) {
                mChatRequestBtn.setVisibility(View.GONE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);
            } else {

//                Toast.makeText(OthersProfileActivity.this, "elseif is Running", Toast.LENGTH_LONG).show();

                mChatRequestBtn.setText("Send Chat Request");
                mChatRequestBtn.setVisibility(View.VISIBLE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);

            }

        } else if (chatStatus.equals("sentRequest")) {

            if (currentUserUid.equals(uid)) {
                mChatRequestBtn.setVisibility(View.GONE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);
            } else {

                mChatRequestBtn.setText("Cancel Chat Request");
                mChatRequestBtn.setVisibility(View.VISIBLE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);

            }

        } else if (chatStatus.equals("receiveRequest")) {

            if (currentUserUid.equals(uid)) {
                mChatRequestBtn.setVisibility(View.GONE);
                mDeclineBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);
            } else {

                mChatRequestBtn.setVisibility(View.GONE);
                mDeclineBtn.setVisibility(View.VISIBLE);
                mAcceptBtn.setVisibility(View.VISIBLE);

            }

        }

    }

    @Override
    public void processFinish(String output) {

    }
}
