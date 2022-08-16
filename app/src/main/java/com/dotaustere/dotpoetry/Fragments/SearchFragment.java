package com.dotaustere.dotpoetry.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotaustere.dotpoetry.Adapters.SearchPoetsAdapter;
import com.dotaustere.dotpoetry.Models.PoetModel;
import com.dotaustere.dotpoetry.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference reference, poetryRef;
    ArrayList<PoetModel> poetList;
    SearchPoetsAdapter adapter;
    RecyclerView recyclerView;
    EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        recyclerView = view.findViewById(R.id.searchRecyclerView);
        searchBar = view.findViewById(R.id.searchBar);

//        poetName = getActivity().getIntent().getStringExtra("poet");


        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Poets");
        poetryRef = database.getReference().child("Poetry");


        poetList = new ArrayList<>();


        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    poetList.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        PoetModel data = s.getValue(PoetModel.class);

                        poetList.add(data);

                        adapter = new SearchPoetsAdapter(poetList, getContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);


                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());


            }
        });


        return view;
    }

    private void filter(String text) {

        ArrayList<PoetModel> filterList = new ArrayList<>();

        for (PoetModel items : poetList) {
            if (items.getPoetName().toLowerCase().contains(text.toLowerCase())){

                filterList.add(items);

            }
        }
        adapter.filterList(filterList);



    }

}