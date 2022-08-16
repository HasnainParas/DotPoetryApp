package com.dotaustere.adminpaneldotpoetry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dotaustere.adminpaneldotpoetry.Adapters.ShayariAdapterAdmin;
import com.dotaustere.adminpaneldotpoetry.Models.ShayariModelAdmin;
import com.dotaustere.adminpaneldotpoetry.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PoetryActivity extends AppCompatActivity {
    ArrayList<ShayariModelAdmin> list;
    ShayariAdapterAdmin adapter;
    RecyclerView recyclerView;
    DatabaseReference poetryRef, mRef, poetry;
        String poetName;
//    String uukey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poetry);

        poetName = getIntent().getStringExtra("poet");

        recyclerView = findViewById(R.id.recyclerViewShayari);
        poetryRef = FirebaseDatabase.getInstance().getReference().child("Poetry");
        poetry = FirebaseDatabase.getInstance().getReference();


        data();


    }

    private void data() {
        mRef = poetryRef.child(poetName);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        ShayariModelAdmin data = s.getValue(ShayariModelAdmin.class);
                        list.add(data);
                    }
                    adapter = new ShayariAdapterAdmin(list, PoetryActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PoetryActivity.this));

                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}