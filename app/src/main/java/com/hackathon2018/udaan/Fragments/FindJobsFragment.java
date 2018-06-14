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
import com.hackathon2018.udaan.Adapters.FindJobAdapter;
import com.hackathon2018.udaan.Adapters.NewsAdapter;
import com.hackathon2018.udaan.Models.FindJobs;
import com.hackathon2018.udaan.Models.News;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindJobsFragment extends Fragment {

    private RecyclerView mNewsRv;

    private FirebaseFirestore mFirestore;
    private List<FindJobs> JobsList;

    private FindJobAdapter findJobAdapter;

    public FindJobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_jobs, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNewsRv = (RecyclerView)getActivity().findViewById(R.id.fjl_jobs_rv);
        mFirestore = FirebaseFirestore.getInstance();

        JobsList = new ArrayList<>();
        findJobAdapter = new FindJobAdapter(JobsList,getContext());

        mNewsRv.setHasFixedSize(true);
        mNewsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewsRv.setAdapter(findJobAdapter);

        mFirestore.collection("Jobs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

                for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        FindJobs news = documentChange.getDocument().toObject(FindJobs.class);
                        JobsList.add(news);

                        findJobAdapter.notifyDataSetChanged();

                    }

                }

            }
        });

    }

}
