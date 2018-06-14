package com.hackathon2018.udaan.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.HorizontalMovingStyle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hackathon2018.udaan.Activities.DatabaseHandler;
import com.hackathon2018.udaan.Adapters.ChatRequestAdapter;
import com.hackathon2018.udaan.Adapters.ForumAdapter;
import com.hackathon2018.udaan.Models.ChatRequest;
import com.hackathon2018.udaan.Models.Questions;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatRequestFragment extends Fragment {

    private RecyclerView mResultList;
    private FirebaseFirestore mFirestore;
    private List<ChatRequest> ChatRequestList;
    private ChatRequestAdapter chatRequestAdapter;

    private String currentUserUid;

    DatabaseHandler db,db1;
    List<String> items;
    List<String> items1;
    RecyclerView rvMoving;
    private ArrayList<String> userData;
    private ArrayList<String> userData1;

    public ChatRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_request, container, false);

//        db = new DatabaseHandler(getActivity().getApplicationContext());
//        db1 = new DatabaseHandler(getActivity().getApplicationContext());
//        userData = new ArrayList<String>();
//        userData1 = new ArrayList<String>();
////        db.deleteDuplicates();
//        userData = db.fetchDatafromPushNotification();
//        userData1 = db.fetchDatafromPushNotification2();
//
//        System.out.println("ANUBHAV ANUBHAV "+userData);
//        rvMoving = (RecyclerView)view.findViewById(R.id.chat_request_rv);
//        rvMoving.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        System.out.println("ANUBHAV URL :" +userData.toString());
//        if(userData.size() > 0){
//            String newData1 = userData1.toString().substring(1, userData1.toString().length()-1);
//            String newData = userData.toString().substring(1, userData.toString().length()-1);
//            items = Arrays.asList(newData.split("\\s*,\\s*"));
//            items1 = Arrays.asList(newData1.split("\\s*,\\s*"));
//            System.out.println("ANUBHAV ITEMS : "+items.toString());
//            rvMoving.setAdapter(new MyAdapter(new HorizontalMovingStyle()));
//        }else{
////            messagetxt.setVisibility(View.VISIBLE);
//        }

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        ChatRequestList = new ArrayList<>();
        chatRequestAdapter = new ChatRequestAdapter(ChatRequestList,getContext());

        mResultList = (RecyclerView)getActivity().findViewById(R.id.chat_request_rv);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultList.setAdapter(chatRequestAdapter);

        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirestore.collection("ChatRequest").document(currentUserUid)
                .collection("ChatRequest").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

                for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        ChatRequest questions = documentChange.getDocument().toObject(ChatRequest.class)
                                .withOtherUserId(documentChange.getDocument().getId());
                        ChatRequestList.add(questions);
                        System.out.println("AnubhaV chat "+ChatRequestList.get(0).getImageurl());
                        chatRequestAdapter.notifyDataSetChanged();

                    }

                }

            }
        });


//
    }

//    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
//        private ScrollParallaxImageView.ParallaxStyle parallaxStyle;
//
//        MyAdapter(ScrollParallaxImageView.ParallaxStyle parallaxStyle) {
//            this.parallaxStyle = parallaxStyle;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View view = inflater.inflate(R.layout.item_img_horizontal, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.iv.setParallaxStyles(parallaxStyle);
//
//            int size = items.size();
//            if(size > 4){
//                size = 4;
//            }
//
//            int size1 = items1.size();
//            if(size > 4){
//                size = 4;
//            }
//
//
//            for(int i = 0; i < size; i++){
//                Picasso.with(getActivity().getApplicationContext())
//                        .load(items.get(i))
//                        .fit().into(holder.iv);
//                holder.name.setText(items1.get(position));
//            }
//
//
//
//
////            switch (position % 5) {
////                case 0 :
////                    Picasso.with(AnotherActivity.this.getApplicationContext())
////                            .load(items.get(0))
////                            .fit().into(holder.iv); break;
////                case 1 : Picasso.with(AnotherActivity.this.getApplicationContext())
////                        .load(items.get(1))
////                        .fit().into(holder.iv); break;
////                case 2 : Picasso.with(AnotherActivity.this.getApplicationContext())
////                        .load(items.get(2))
////                        .fit().into(holder.iv); break;
////            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            ScrollParallaxImageView iv;
//            TextView name;
//            Button accept, decline;
//            ViewHolder(View itemView) {
//                super(itemView);
//                iv = (ScrollParallaxImageView) itemView.findViewById(R.id.img);
//                name = (TextView)itemView.findViewById(R.id.name);
//            }
//        }
//    }

}
