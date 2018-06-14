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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hackathon2018.udaan.Adapters.ForumAdapter;
import com.hackathon2018.udaan.Models.Questions;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumMyQuestionsFragment extends Fragment {

    private RecyclerView mResultList;
    private FirebaseFirestore mFirestore;
    private List<Questions> QuestionList;

    private ForumAdapter forumAdapter;

    private String uid;

    public ForumMyQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum_my_questions, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();

        QuestionList = new ArrayList<>();
        forumAdapter = new ForumAdapter(QuestionList,getContext());

        mResultList = (RecyclerView)getActivity().findViewById(R.id.myQuestion_forum_rv);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultList.setAdapter(forumAdapter);

        mFirestore.collection("Questions").whereEqualTo("postedBy",uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

                for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        Questions questions = documentChange.getDocument().toObject(Questions.class)
                                .withQuestionId(documentChange.getDocument().getId());
                        QuestionList.add(questions);

                        forumAdapter.notifyDataSetChanged();

                    }

                }

            }
        });

    }


}
