package com.hackathon2018.udaan.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hackathon2018.udaan.Adapters.ChatChatAdapter;
import com.hackathon2018.udaan.Adapters.ChatRequestAdapter;
import com.hackathon2018.udaan.Models.ChatChat;
import com.hackathon2018.udaan.Models.ChatRequest;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatChatFragment extends Fragment {

    private RecyclerView mResultList;
    private FirebaseFirestore mFirestore;
    private List<ChatChat> ChatChatList;
    private ChatChatAdapter chatChatAdapter;

    private String currentUserUid;

    public ChatChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_chat, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        ChatChatList = new ArrayList<>();
        chatChatAdapter = new ChatChatAdapter (ChatChatList,getContext());

        mResultList = (RecyclerView)getActivity().findViewById(R.id.chat_chat_rv);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultList.setAdapter(chatChatAdapter);

        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirestore.collection("Chats").document(currentUserUid)
                .collection("Chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

                for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        ChatChat questions = documentChange.getDocument().toObject(ChatChat.class)
                                .withOtherUserId(documentChange.getDocument().getId());
                        ChatChatList.add(questions);

                        chatChatAdapter.notifyDataSetChanged();

                    }

                }

            }
        });

    }

}
