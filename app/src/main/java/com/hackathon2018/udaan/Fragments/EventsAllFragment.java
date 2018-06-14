package com.hackathon2018.udaan.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hackathon2018.udaan.Adapters.EventsAdapter;
import com.hackathon2018.udaan.Adapters.SearchListAdapter;
import com.hackathon2018.udaan.Models.Events;
import com.hackathon2018.udaan.Models.SearchUsers;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsAllFragment extends Fragment {

    private RecyclerView mResultList;
    private FirebaseFirestore mFirestore;
    private List<Events> EventsList;

    private EventsAdapter eventsAdapter;

    public EventsAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_all, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        EventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(EventsList,getContext());

        mResultList = (RecyclerView)getActivity().findViewById(R.id.all_events_rv);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultList.setAdapter(eventsAdapter);

            mFirestore.collection("Events").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (e != null){
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                        if (documentChange.getType() == DocumentChange.Type.ADDED){

                            Events events = documentChange.getDocument().toObject(Events.class);
                            EventsList.add(events);

                            eventsAdapter.notifyDataSetChanged();

                        }

                    }

                }
            });

    }
}
