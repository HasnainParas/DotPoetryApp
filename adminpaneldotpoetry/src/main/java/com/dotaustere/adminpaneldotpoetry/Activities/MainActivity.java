package com.dotaustere.adminpaneldotpoetry.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dotaustere.adminpaneldotpoetry.UploadPoetry.UploadActivity;
import com.dotaustere.adminpaneldotpoetry.databinding.ActivityMainBinding;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Admin Panel");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");


        binding.forAddingPoets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addclick();
            }
        });

        binding.forUploadingPoetrys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClick();

            }
        });
        binding.slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUploadClick();

            }
        });


    }

    void addclick() {
        intent = new Intent(MainActivity.this, PoetsActivity.class);
        startActivity(intent);
    }

    void uploadClick() {
        intent = new Intent(MainActivity.this, UploadActivity.class);
        startActivity(intent);
    }

    void slideUploadClick() {
        intent = new Intent(MainActivity.this, SliderActivity.class);
        startActivity(intent);
    }

}