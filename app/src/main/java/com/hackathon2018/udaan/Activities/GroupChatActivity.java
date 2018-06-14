package com.hackathon2018.udaan.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hackathon2018.udaan.Adapters.MessageAdapter;
import com.hackathon2018.udaan.Models.Message;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    private String city;

    private EditText mMsgEt;
    private ImageView mSendBtn;

    private String currentUserUid;

    private RecyclerView mResultList;
    private FirebaseFirestore mFirestore;
    private List<Message> MessageList;
    private MessageAdapter messageAdapter;

    private String CurrentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        city = getIntent().getStringExtra("city");

        mMsgEt = (EditText) findViewById(R.id.gca_chat_et);
        mSendBtn = (ImageView) findViewById(R.id.gca_sent_btn);

        mFirestore = FirebaseFirestore.getInstance();

        MessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(MessageList, GroupChatActivity.this);

        mResultList = (RecyclerView) findViewById(R.id.gca_chat_rv);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        mResultList.setAdapter(messageAdapter);

        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirestore.collection("Message").document("Group").collection("Group").orderBy("time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (e != null) {
                            Toast.makeText(GroupChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {

                            if (documentChange.getType() == DocumentChange.Type.ADDED) {

                                Message questions = documentChange.getDocument().toObject(Message.class);
                                MessageList.add(questions);

                                messageAdapter.notifyDataSetChanged();

                            }

                        }

                    }
                });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String msg = mMsgEt.getText().toString();

                HashMap<String, Object> user = new HashMap<>();
                user.put("msg", msg);
                user.put("type", "sent");
                user.put("name", getCurrentName());
                user.put("time", Calendar.getInstance().getTime());

                DocumentReference documentReference = FirebaseFirestore.getInstance()
                        .collection("Message").document("Group").collection("Group").document();

                documentReference.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mMsgEt.setText("");

                        }
                    }
                });


            }
        });


    }

    private String getCurrentName() {
        DocumentReference mCurrentNameRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserUid);
        mCurrentNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    CurrentName = documentSnapshot.getString("displayName");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return CurrentName;
    }

}
