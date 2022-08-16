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
import com.dotaustere.dotpoetry.databinding.ActivityShayariBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShayariActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ActivityShayariBinding binding;
    ArrayList<ShayariModel> list;
    ShayariAdapter adapter;
    DatabaseReference reference, mRef;
    DatabaseReference likesRef;
    FirebaseDatabase database;


    String uIDpoet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShayariBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.shayari_recyclerView);

//        String title = getIntent().getStringExtra("poet");

        uIDpoet = getIntent().getStringExtra("poet");

//        getSupportActionBar().setTitle(title);

//
//        if (getIntent().getStringExtra("poet") != null) {
//            poetName = getIntent().getStringExtra("poet");
//        }


        reference = FirebaseDatabase.getInstance().getReference().child("Poetry");


        data();
//        sData();


    }

    private void data() {

        mRef = reference.child(uIDpoet);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    ShayariModel data = s.getValue(ShayariModel.class);
                    list.add(data);

                }

                adapter = new ShayariAdapter(list, ShayariActivity.this);

                if (list.isEmpty()) {
                    binding.shayariRecyclerView.setVisibility(View.GONE);
                    binding.noDataAvailable.setVisibility(View.VISIBLE);
                    binding.nodataLottie.setVisibility(View.VISIBLE);
                }

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ShayariActivity.this));
                binding.loading.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//            mReff = poetryRef.child(sPoet);
//            mReff.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    list = new ArrayList<>();
//                    for (DataSnapshot s : snapshot.getChildren()) {
//                        ShayariModel data = s.getValue(ShayariModel.class);
//                        list.add(data);
//
//                    }
//
//                    adapter = new ShayariAdapter(list, ShayariActivity.this);
//                    recyclerView.setAdapter(adapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(ShayariActivity.this));
//                    binding.loading.setVisibility(View.INVISIBLE);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });


    }

}