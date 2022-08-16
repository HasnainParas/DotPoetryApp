package com.dotaustere.dotpoetry.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.dotaustere.dotpoetry.Adapters.FavAdapter;
import com.dotaustere.dotpoetry.Models.FavModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.FragmentFavouriteBinding;
import com.dotaustere.dotpoetry.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    FragmentFavouriteBinding binding;

    DatabaseReference likesRef, favCount, favProfileRef;
    FirebaseDatabase database;
    FirebaseUser fireBaseuser;

    //RecyclerView recyclerView;
    ArrayList<FavModel> favModelList;
    FavAdapter adapter;
//    CircleImageView fvProfilepic;
//    TextView fvUsername, fvCounts;


    String name, iMgUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

//        recyclerView = view.findViewById(R.id.favRecyclerView);
//        fvProfilepic = view.findViewById(R.id.fvCircleView);
//        fvUsername = view.findViewById(R.id.usernamefv);
//        fvCounts = view.findViewById(R.id.fvCount);

        fireBaseuser = FirebaseAuth.getInstance().getCurrentUser();
        String user = fireBaseuser.getUid();

        name = fireBaseuser.getDisplayName();
        binding.usernamefv.setText(name);

        database = FirebaseDatabase.getInstance();
        likesRef = database.getReference("Likes");
        favCount = database.getReference("Likes");
        favProfileRef = database.getReference("profiles");


        favModelList = new ArrayList<>();


        getData();


        return binding.getRoot();
    }

    public void getData() {

        likesRef.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favModelList.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    FavModel data = s.getValue(FavModel.class);
                    favModelList.add(data);

                }

                if (favModelList.isEmpty()) {
                    binding.favRecyclerView.setVisibility(View.GONE);
                    binding.favnoDataAvailable.setVisibility(View.VISIBLE);
                    binding.favnodataLottie.setVisibility(View.VISIBLE);
                    binding.favnoDataAvailable.setText("Sorry! No Favourite Poetry Available");

                }


                adapter = new FavAdapter(favModelList, getContext());
                binding.favRecyclerView.setAdapter(adapter);
                binding.favRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        favCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(name).exists()) {
                    int likecount = (int) snapshot.child(name).getChildrenCount();
                    binding.fvCount.setText(likecount + " Favourite's");

                } else {
//                    int likecount = (int) snapshot.child(name).getChildrenCount();
                    binding.fvCount.setText( "0 Favourite's");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        favProfileRef.child(fireBaseuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (fireBaseuser.getPhotoUrl() != null) {
                        Glide.with(requireContext()).load(fireBaseuser.getPhotoUrl())
                                .placeholder(R.drawable.placeholder).into(binding.fvCircleView);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}