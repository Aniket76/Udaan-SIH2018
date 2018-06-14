package com.hackathon2018.udaan.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hackathon2018.udaan.Activities.OthersProfileActivity;
import com.hackathon2018.udaan.Models.Answers;
import com.hackathon2018.udaan.Models.ChatRequest;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 27/03/18.
 */

public class ChatRequestAdapter extends RecyclerView.Adapter<ChatRequestAdapter.ChatRequestViewHolder>{

    private String currentUserUid;
    private String uid;
    private String reqType;
    private String currentUserName,currentUserImage,dbName,dbImage;

    public List<ChatRequest> ChatRequestList;
    private Context context;

    public ChatRequestAdapter(List<ChatRequest> chatRequestList, Context context) {
        ChatRequestList = chatRequestList;
        this.context = context;
    }

    @Override
    public ChatRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_request_layout, parent, false);
        return new ChatRequestViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ChatRequestViewHolder holder, int position) {

        uid = ChatRequestList.get(position).chatOtherUserId;

        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        DocumentReference mNameRef = FirebaseFirestore.getInstance().collection("Users").document(uid);
        mNameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    System.out.println("Anubhav user data : "+documentSnapshot.toString());
                    dbName = documentSnapshot.getString("displayName");
                    dbImage = documentSnapshot.getString("image");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (position%2 == 0){
            holder.mCrlChildCardLayout.setBackgroundColor(Color.parseColor("#f9fffe"));
        }else {
            holder.mCrlChildCardLayout.setBackgroundColor(Color.parseColor("#fafff9"));
        }

        reqType = ChatRequestList.get(position).getRequestType();
        String imageUrl= ChatRequestList.get(position).getImageurl();
        Picasso.with(context)
                        .load(ChatRequestList.get(position).getImageurl())
                        .fit().into(holder.mCrlImage);

        holder.mCrlName.setText(ChatRequestList.get(position).getName());

//        holder.setSearchDetails(reqType);

        if (reqType.equals("sent")){

            holder.mCrlChatRequestBtn.setVisibility(View.VISIBLE);
            holder.mCrlChatAcceptBtn.setVisibility(View.GONE);
            holder.mCrlChatDeclineBtn.setVisibility(View.GONE);

        }else {

            holder.mCrlChatRequestBtn.setVisibility(View.GONE);
            holder.mCrlChatAcceptBtn.setVisibility(View.VISIBLE);
            holder.mCrlChatDeclineBtn.setVisibility(View.VISIBLE);

        }

        holder.mCrlChatRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                                Toast.makeText(context, "Canceled", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        holder.mCrlChatAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.mCrlChatAcceptBtn.setEnabled(false);

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

                                                    Toast.makeText(context, "Accepted", Toast.LENGTH_LONG).show();

                                                    holder.mCrlChatAcceptBtn.setEnabled(true);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

        });

        holder.mCrlChatDeclineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.mCrlChatDeclineBtn.setEnabled(false);

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
                                Toast.makeText(context, "Decline", Toast.LENGTH_LONG).show();

                                holder.mCrlChatDeclineBtn.setEnabled(true);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return ChatRequestList.size();
    }



    public class ChatRequestViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView mCrlName,mCrlMsg;
        public CircleImageView mCrlImage;

        public LinearLayout mCrlChildCardLayout;

        public Button mCrlChatRequestBtn,mCrlChatAcceptBtn,mCrlChatDeclineBtn;

        public ChatRequestViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mCrlName = (TextView) mView.findViewById(R.id.crl_name_txt);
            mCrlMsg = (TextView) mView.findViewById(R.id.crl_msg_txt);

            mCrlImage = (CircleImageView) mView.findViewById(R.id.crl_profile_image);

            mCrlChildCardLayout = (LinearLayout)mView.findViewById(R.id.crl_maincard_layout);

            mCrlChatRequestBtn = (Button)mView.findViewById(R.id.crl_chatRequest_btn);
            mCrlChatAcceptBtn = (Button)mView.findViewById(R.id.crl_accept_btn);
            mCrlChatDeclineBtn = (Button)mView.findViewById(R.id.crl_decline_btn);

        }

//        public void setSearchDetails(String requestType) {
//            if (requestType.equals("sent")){
//
//                mCrlChatRequestBtn.setVisibility(View.VISIBLE);
//                mCrlChatAcceptBtn.setVisibility(View.GONE);
//                mCrlChatDeclineBtn.setVisibility(View.GONE);
//
//            }else {
//
//                mCrlChatRequestBtn.setVisibility(View.GONE);
//                mCrlChatAcceptBtn.setVisibility(View.VISIBLE);
//                mCrlChatDeclineBtn.setVisibility(View.VISIBLE);
//
//            }
//
////            Picasso.with(mView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
////                    .placeholder(R.drawable.profile_icon).into(mFllImage, new Callback() {
////                @Override
////                public void onSuccess() {
////
////                }
////
////                @Override
////                public void onError() {
////                    Picasso.with(mView.getContext()).load(image).placeholder(R.drawable.profile_icon).into(mFllImage);
////                }
////            });
//
//        }


    }

}
