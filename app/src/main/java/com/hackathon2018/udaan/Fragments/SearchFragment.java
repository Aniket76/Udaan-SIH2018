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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hackathon2018.udaan.Activities.MainActivity;
import com.hackathon2018.udaan.Adapters.SearchListAdapter;
import com.hackathon2018.udaan.Models.SearchUsers;
import com.hackathon2018.udaan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText mSearchField;
    private ImageView mSearchBtn;

    private RecyclerView mResultList;

    private FirebaseFirestore mFirestore;

    private List<SearchUsers> searchUsersList;

    private SearchListAdapter searchListAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchField = (EditText)getActivity().findViewById(R.id.search_field);
        mSearchBtn = (ImageView) getActivity().findViewById(R.id.search_btn);

        mFirestore = FirebaseFirestore.getInstance();

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchUsersList = new ArrayList<>();
                searchListAdapter = new SearchListAdapter(getContext(),searchUsersList);

                mResultList = (RecyclerView)getActivity().findViewById(R.id.search_rv);
                mResultList.setHasFixedSize(true);
                mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
                mResultList.setAdapter(searchListAdapter);

                String searchText = mSearchField.getText().toString();

                if (searchText.equals("")){
                    Toast.makeText(getContext(),"Write the city name",Toast.LENGTH_LONG).show();
                }else {

                    mFirestore.collection("Users").whereEqualTo("City",searchText).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (e != null){
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            for (DocumentChange documentChange: documentSnapshots.getDocumentChanges()){

                                if (documentChange.getType() == DocumentChange.Type.ADDED){

                                    SearchUsers searchUsers = documentChange.getDocument().toObject(SearchUsers.class)
                                            .withId(documentChange.getDocument().getId());
                                    searchUsersList.add(searchUsers);

                                    searchListAdapter.notifyDataSetChanged();

                                }

                            }

                        }
                    });

                }

                mSearchField.setText("");
                mSearchField.setHint(searchText);

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

    }


}
