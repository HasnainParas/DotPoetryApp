package com.dotaustere.dotpoetry.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dotaustere.dotpoetry.R;

public class DevActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        getSupportActionBar().hide();

    }
}