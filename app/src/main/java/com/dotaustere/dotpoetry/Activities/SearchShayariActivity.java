package com.dotaustere.dotpoetry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.dotaustere.dotpoetry.Adapters.ShayariAdapter;
import com.dotaustere.dotpoetry.Models.ShayariModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.ActivitySearchShayariBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchShayariActivity extends AppCompatActivity {

    ActivitySearchShayariBinding binding;

    RecyclerView recyclerView;
    DatabaseReference poetryRef, mReff;
    ArrayList<ShayariModel> list;
    ShayariAdapter adapter;

    String sPoet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchShayariBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.searchRecyclerV);


        if (getIntent().getStringExtra("sPoet") != null) {
            sPoet = getIntent().getStringExtra("sPoet");
        }

        getSupportActionBar().setTitle(sPoet);


        poetryRef = FirebaseDatabase.getInstance().getReference().child("Poetry");

        dataFirebase();


    }

    public void dataFirebase() {
        mReff = poetryRef.child(sPoet);
        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    ShayariModel data = s.getValue(ShayariModel.class);
                    list.add(data);

                }

                if (list.isEmpty()) {
                    binding.searchRecyclerV.setVisibility(View.GONE);
                    binding.searchnoDataAvailable.setVisibility(View.VISIBLE);
                    binding.searchnodataLottie.setVisibility(View.VISIBLE);

                }

                adapter = new ShayariAdapter(list, SearchShayariActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchShayariActivity.this));
//                binding.loading.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}