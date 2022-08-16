package com.dotaustere.adminpaneldotpoetry.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.dotaustere.adminpaneldotpoetry.Models.AdminPoetsModel;
import com.dotaustere.adminpaneldotpoetry.UploadPoetry.UploadActivity;
import com.dotaustere.adminpaneldotpoetry.databinding.ActivityAddingPoetsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class AddingPoetsActivity extends AppCompatActivity {

    ActivityAddingPoetsBinding binding;

    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseStorage storage;
    //    StorageReference storageReference;
    int REQCode = 45;
    Bitmap bitmap;
    Uri uri;
    String poetNAME, poetAgeStart, poetAgeEnd;
    ProgressDialog dialog;
    String filepath;
    String key = UUID.randomUUID().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddingPoetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Add Poetry's");


        dialog = new ProgressDialog(this);
        dialog.setMessage("Adding Poet Please wait...");
        dialog.setCancelable(false);


//        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Poets");

        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
        binding.addPoets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddingPoetsActivity.this, UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.imageaddingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Any Image"), REQCode);

            }
        });

        binding.submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poetNAME = binding.poetname.getText().toString();
                poetAgeStart = binding.poetAgeStart.getText().toString();
                poetAgeEnd = binding.poetAgeEnd.getText().toString();


                if (uri == null) {
                    int b = 10;
                    binding.addedimageV.setBorderColor(Color.RED);
                    binding.addedimageV.setBorderWidth(b);
                    Toasty.error(AddingPoetsActivity.this, "Please Select Poet Picture", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "Please Select Poet Picture", Toast.LENGTH_SHORT).show();
                } else if (poetNAME.isEmpty()) {
                    binding.poetname.setError("Poet Name Required");
                } else if (poetAgeStart.isEmpty()) {
                    binding.poetAgeStart.setError("Poet Born Date");
                } else if (poetAgeEnd.isEmpty()) {
                    binding.poetAgeEnd.setError("Poet Die Date");
                } else {
                    StorageReference reference = storage.getReference()
                            .child("PoetsImages").child(poetNAME);
                    dialog.show();
                    reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        filepath = uri.toString();
                                        uploadData();
                                    }
                                });
                            }
                        }
                    });

                }
//                storageReference.child("poets");

            }
        });


    }

    private void uploadData() {
        AdminPoetsModel poets = new AdminPoetsModel(poetNAME, poetAgeStart, poetAgeEnd, filepath, key);

        databaseReference.child(key).setValue(poets).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.cancel();
                Intent activity = new Intent(AddingPoetsActivity.this, PoetsActivity.class);
                startActivity(activity);
                finish();
                Toasty.success(AddingPoetsActivity.this, "Poet Added Successfully!", Toast.LENGTH_SHORT, true).show();
                finish();

//                Toast.makeText(getApplicationContext(), "Poet Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQCode && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.addedimageV.setImageBitmap(bitmap);
            binding.addedimageV.setBorderColor(Color.BLACK);
            Toasty.success(AddingPoetsActivity.this, "Picture Select Successfully!", Toast.LENGTH_SHORT, true).show();

        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}