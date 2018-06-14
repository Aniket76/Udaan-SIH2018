package com.hackathon2018.udaan.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hackathon2018.udaan.Adapters.AnswerAdapter;
import com.hackathon2018.udaan.Adapters.ForumAdapter;
import com.hackathon2018.udaan.Models.Answers;
import com.hackathon2018.udaan.Models.Questions;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumAnswerActivity extends AppCompatActivity {

    private FloatingActionButton addAnswer;
    private ProgressDialog mMainProgress;
    private String uid,dbName,dbDp;
    private String quesId;

    private RecyclerView mResultList;
    private FirebaseFirestore mFirestore;
    private List<Answers> AnswerList;
    private AnswerAdapter answerAdapter;

    private TextView mQName,mQQues;
    private CircleImageView mQImage;

    private String dbQQues,dbQName,dbQImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_answer);

        quesId = getIntent().getStringExtra("questionId");
        dbQName = getIntent().getStringExtra("QName");
        dbQImage = getIntent().getStringExtra("QImage");
        dbQQues = getIntent().getStringExtra("QQues");

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();

//        mQName = (TextView)findViewById(R.id.faa_name_txt);
//        mQQues = (TextView)findViewById(R.id.faa_question_txt);
//        mQImage = (CircleImageView)findViewById(R.id.faa_profile_image);


//        DocumentReference mQuesRef = FirebaseFirestore.getInstance().collection("Questions").document(quesId);
//        mQuesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    dbQQues = documentSnapshot.getString("question");
//                    dbQName = documentSnapshot.getString("name");
//                    dbQImage = documentSnapshot.getString("image");
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ForumAnswerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

//        mQName.setText(dbQName);
//        mQQues.setText(dbQQues);
//
//        Picasso.with(ForumAnswerActivity.this).load(dbQImage).networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(R.drawable.profile_icon).into(mQImage, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError() {
//                Picasso.with(ForumAnswerActivity.this).load(dbQImage).placeholder(R.drawable.profile_icon).into(mQImage);
//            }
//        });



        mFirestore = FirebaseFirestore.getInstance();

        AnswerList = new ArrayList<>();
        answerAdapter = new AnswerAdapter(AnswerList,this,dbQName,dbQImage,dbQQues);

        mResultList = (RecyclerView)findViewById(R.id.faa_answer_rv);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        mResultList.setAdapter(answerAdapter);

        mFirestore.collection("Questions").document(quesId).collection("Answers").orderBy("postingTime")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(ForumAnswerActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

                for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        Answers answers = documentChange.getDocument().toObject(Answers.class);
                        AnswerList.add(answers);

                        answerAdapter.notifyDataSetChanged();

                    }

                }

            }
        });




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
                Toast.makeText(ForumAnswerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        addAnswer = (FloatingActionButton)findViewById(R.id.faa_add_fab);

        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                final AlertDialog.Builder mBulider = new AlertDialog.Builder(ForumAnswerActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.update_profile_layout, null);

                final TextView mTitle1 = (TextView) mView.findViewById(R.id.Udate_profile_title_txt);

                final TextInputLayout mTil1 = (TextInputLayout) mView.findViewById(R.id.update_profile_til1);

                mTitle1.setText("Answer");

                mTil1.setVisibility(View.VISIBLE);
                mTil1.setHint("Answer");
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

                                mMainProgress = new ProgressDialog(ForumAnswerActivity.this);
                                mMainProgress.setTitle("Adding Awswer");
                                mMainProgress.setMessage("Please wait while we add your Awswer");
                                mMainProgress.setCanceledOnTouchOutside(false);
                                mMainProgress.show();

                                String st1 = mTil1.getEditText().getText().toString();

                                if (!TextUtils.isEmpty(st1)) {

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("answer", st1);
                                    user.put("postedBy", uid);
                                    user.put("name",dbName);
                                    user.put("image",dbDp);
                                    user.put("postingTime", Calendar.getInstance().getTime().toString());

                                    DocumentReference mSkillsRef = FirebaseFirestore.getInstance().collection("Questions").document(quesId)
                                            .collection("Answers").document();

                                    mSkillsRef.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(ForumAnswerActivity.this, "Added", Toast.LENGTH_LONG).show();
                                                mMainProgress.dismiss();

                                            } else {

                                                mMainProgress.dismiss();
                                                Toast.makeText(ForumAnswerActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                    dialogInterface.dismiss();
                                } else {
                                    mMainProgress.dismiss();
                                    Toast.makeText(ForumAnswerActivity.this, "Fill all the fields and try again", Toast.LENGTH_LONG).show();
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
