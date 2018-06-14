package com.hackathon2018.udaan.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.hackathon2018.udaan.Adapters.EventsAdapter;
import com.hackathon2018.udaan.Adapters.NewsAdapter;
import com.hackathon2018.udaan.Models.Events;
import com.hackathon2018.udaan.Models.News;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private String sub ;
    private Button mSubBtn;
    private RecyclerView mNewsRv;

    private FirebaseFirestore mFirestore;
    private List<News> NewsList;

    private NewsAdapter newsAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSubBtn = (Button)getActivity().findViewById(R.id.nf_sub_btn);
        mNewsRv = (RecyclerView)getActivity().findViewById(R.id.nf_news_rv);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                sub = documentSnapshot.getString("news");

                if (sub.equals("notSubscribed")){

                    mNewsRv.setVisibility(View.GONE);
                    mSubBtn.setVisibility(View.VISIBLE);

                }else {

                    mSubBtn.setVisibility(View.GONE);
                    mNewsRv.setVisibility(View.VISIBLE);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        mSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSubBtn.setEnabled(false);

                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(uid);

                HashMap<String, Object> user = new HashMap<>();
                user.put("news", "subscribed");

                documentReference.set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            mSubBtn.setVisibility(View.GONE);
                            mNewsRv.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                        }
                    }
                });

            }
        });


        mFirestore = FirebaseFirestore.getInstance();

        NewsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(NewsList,getContext());

        mNewsRv.setHasFixedSize(true);
        mNewsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewsRv.setAdapter(newsAdapter);

        mFirestore.collection("News").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

                for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        News news = documentChange.getDocument().toObject(News.class);
                        NewsList.add(news);

                        newsAdapter.notifyDataSetChanged();

                    }

                }

            }
        });

    }
}
