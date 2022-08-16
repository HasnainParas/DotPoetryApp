package com.dotaustere.dotpoetry.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dotaustere.dotpoetry.Adapters.PoetAdapter;
import com.dotaustere.dotpoetry.Adapters.SliderAdapterExample;
import com.dotaustere.dotpoetry.Models.PoetModel;
import com.dotaustere.dotpoetry.Models.SliderModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    DatabaseReference reference, sliderRef;
    SliderView sliderView;
    ArrayList<SliderModel> modelArrayList;
    SliderAdapterExample adapter;
    PoetAdapter poetFragmentAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_home, container, false);

        reference = FirebaseDatabase.getInstance().getReference("Poets");
        sliderRef = FirebaseDatabase.getInstance().getReference();

        sliderView = binding.imageSlider;
//        recyclerView = view.findViewById(R.id.poet_recycler_view);

        ArrayList<PoetModel> listPoet = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    listPoet.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        PoetModel data = s.getValue(PoetModel.class);

                        listPoet.add(data);
//                        Arrays.sort(new ArrayList[]{listPoet});

                        poetFragmentAdapter = new PoetAdapter(listPoet, getContext());

                        binding.poetRecyclerView.setAdapter(poetFragmentAdapter);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                        binding.poetRecyclerView.setLayoutManager(gridLayoutManager);
                        binding.loadingFragment.setVisibility(View.GONE);

                    }

                    poetFragmentAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sliderCode();


        return binding.getRoot();
    }

    void sliderCode() {

        modelArrayList = new ArrayList<>();
        sliderRef.child("SlidersImage").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    modelArrayList.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        SliderModel data = s.getValue(SliderModel.class);

                        modelArrayList.add(data);
//                        Arrays.sort(new ArrayList[]{listPoet});

                        adapter = new SliderAdapterExample(getContext(), modelArrayList);
                        sliderView.setSliderAdapter(adapter);
                        binding.sliderLoader.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStart() {

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