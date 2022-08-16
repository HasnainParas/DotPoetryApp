package com.dotaustere.adminpaneldotpoetry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dotaustere.adminpaneldotpoetry.Adapters.AdminPoetsAdapter;
import com.dotaustere.adminpaneldotpoetry.Models.AdminPoetsModel;
import com.dotaustere.adminpaneldotpoetry.R;
import com.dotaustere.adminpaneldotpoetry.databinding.ActivityPoetsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PoetsActivity extends AppCompatActivity {

    ActivityPoetsBinding binding;

    FloatingActionButton addpoets;
    RecyclerView adminRecyclerView;

    DatabaseReference reference;
    AdminPoetsAdapter adminPoetsAdapterdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPoetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addpoets = findViewById(R.id.floatingActionButton);
        adminRecyclerView = findViewById(R.id.adminRecycler_View);

        getSupportActionBar().setTitle("Add Poets");


        addpoets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Poets");


        ArrayList<AdminPoetsModel> adminlistPoet = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    adminlistPoet.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        AdminPoetsModel data = s.getValue(AdminPoetsModel.class);

                        adminlistPoet.add(data);
//                        Arrays.sort(new ArrayList[]{listPoet});

                        adminPoetsAdapterdapter = new AdminPoetsAdapter(adminlistPoet, PoetsActivity.this);

                        adminRecyclerView.setAdapter(adminPoetsAdapterdapter);
                        binding.animationView.setVisibility(View.GONE);

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PoetsActivity.this);
                        adminRecyclerView.setLayoutManager(layoutManager);

//                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PoetsActivity.this, 3);
//                        adminRecyclerView.setLayoutManager(gridLayoutManager);

                    }
                    adminPoetsAdapterdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void click() {
        Intent intent = new Intent(PoetsActivity.this, AddingPoetsActivity.class);
        startActivity(intent);
    }

}