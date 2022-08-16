package com.dotaustere.adminpaneldotpoetry.UploadPoetry;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dotaustere.adminpaneldotpoetry.Models.GetData;
import com.dotaustere.adminpaneldotpoetry.Models.PoetryData;
import com.dotaustere.adminpaneldotpoetry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    Spinner spinner;
    EditText writtenBox;
    Button submitButton;
    DatabaseReference reference;
    DatabaseReference poetryRef;
    FirebaseDatabase database;
    ArrayList<String> spinnerarrayList;
    ArrayAdapter<String> spinadapter;
    String poetSelected;
    String uuiIDD;
    String selectedID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadactivity_admin);

        spinner = findViewById(R.id.spinner2);
        writtenBox = findViewById(R.id.box);
        submitButton = findViewById(R.id.btn);

        database = FirebaseDatabase.getInstance();

        reference = database.getReference();

        poetryRef = database.getReference().child("Poetry");


//        mRef =FirebaseDatabase.getInstance().getReference("Poetry");
        reference.child("Poets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    GetData data = item.getValue(GetData.class);
                    String poetName = data.getPoetName();
                    spinnerarrayList.add(poetName);

                }
                spinadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                poetSelected = spinnerarrayList.get(i);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerarrayList = new ArrayList<>();

        spinadapter = new ArrayAdapter<String>(UploadActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerarrayList);

        spinner.setAdapter(spinadapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                uploadData();

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void uploadData() {

        String poetrybox = writtenBox.getText().toString();

        String uniqueKey = UUID.randomUUID().toString();
//        mRef.setValue(poetrybox);
//        String poetryID = database.getReference().getRef().getKey();


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();


        PoetryData poetryData = new PoetryData(poetSelected, uniqueKey, poetrybox, strDate);

        if (poetrybox.isEmpty()) {
            writtenBox.setError(" Please Write Any Poetry ");
        } else {

            poetryRef.child(poetSelected)
                    .child(uniqueKey)
                    .setValue(poetryData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Poetry Added Successfully", Toast.LENGTH_SHORT).show();
                                writtenBox.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}