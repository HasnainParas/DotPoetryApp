package com.dotaustere.adminpaneldotpoetry.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dotaustere.adminpaneldotpoetry.Adapters.SliderAdapterAdmin;
import com.dotaustere.adminpaneldotpoetry.R;
import com.dotaustere.adminpaneldotpoetry.SliderFirebaseModel;
import com.dotaustere.adminpaneldotpoetry.databinding.ActivitySliderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class SliderActivity extends AppCompatActivity {

    ActivitySliderBinding binding;
    Uri uri;
    Bitmap bitmap;
    int REQCode = 45;
    String filepath;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog dialog;
    String slidersUID = UUID.randomUUID().toString();

    SliderView sliderView;
    ArrayList<SliderFirebaseModel> modelArrayList;
    SliderAdapterAdmin adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference = FirebaseDatabase.getInstance().getReference();


        dialog = new ProgressDialog(this);
        dialog.setTitle("Slider Image");
        dialog.setMessage("Uploading Please Wait...");
        dialog.setCancelable(false);

        binding.uploadLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Any Image"), REQCode);

//                Toast.makeText(SliderActivity.this, "Clicked ;-", Toast.LENGTH_SHORT).show();
            }
        });

        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (uri == null) {
//                    binding.addedimageV.setBorderColor(Color.RED);
//                    binding.addedimageV.setBorderWidth(b);
                    Toasty.error(SliderActivity.this, "Please Select Poet Picture", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(getApplicationContext(), "Please Select Poet Picture", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    storageReference = storage.getReference()
                            .child("SlideImage").child(slidersUID);
                    storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        filepath = uri.toString();
                                        SliderFirebaseModel model = new SliderFirebaseModel(filepath);
                                        reference.child("SlidersImage").child(slidersUID).setValue(model);
                                        dialog.dismiss();
                                        binding.selectedImage.setVisibility(View.INVISIBLE);
                                        binding.textTag.setText("Slider Uploaded Successfully");
                                        binding.textTag.setVisibility(View.VISIBLE);
                                        Toasty.success(SliderActivity.this, "Picture UPLOADED Successfully.....!", Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        sliderView = binding.sliderimage;
        sliderCode();


    }

    void sliderCode() {

        modelArrayList = new ArrayList<>();

        reference.child("SlidersImage").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    modelArrayList.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        SliderFirebaseModel data = s.getValue(SliderFirebaseModel.class);

                        modelArrayList.add(data);
//                        Arrays.sort(new ArrayList[]{listPoet});

                        adapter = new SliderAdapterAdmin(SliderActivity.this, modelArrayList);
                        sliderView.setSliderAdapter(adapter);
                        binding.sliderLoader.setVisibility(View.INVISIBLE);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            binding.textTag.setVisibility(View.INVISIBLE);
            binding.selectedImage.setVisibility(View.VISIBLE);
            binding.selectedImage.setImageBitmap(bitmap);
//            binding.addedimageV.setBorderColor(Color.BLACK);
            Toasty.success(SliderActivity.this, "Picture Select Successfully!", Toast.LENGTH_SHORT, true).show();

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        sliderView.setIndicatorAnimation(IndicatorAnimationType.COLOR); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        super.onStart();
    }
}