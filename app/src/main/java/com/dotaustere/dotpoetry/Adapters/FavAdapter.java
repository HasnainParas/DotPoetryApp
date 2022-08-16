package com.dotaustere.dotpoetry.Adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotaustere.dotpoetry.Models.FavModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.FavSampleLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.favViewHolder> {

    DatabaseReference likesRef;

    ArrayList<FavModel> favModel;
    Context context;
    FirebaseAuth auth;

    Boolean isClicked = false;

    public FavAdapter(ArrayList<FavModel> favModel, Context context) {
        this.favModel = favModel;
        this.context = context;
    }

    @NonNull
    @Override
    public favViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_sample_layout, parent, false);
        return new FavAdapter.favViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull favViewHolder holder, @SuppressLint("RecyclerView") int position) {

        FavModel modelData = favModel.get(position);

        String poetryID = modelData.getPoetryId();

        holder.binding.favshayaritvv.setText(modelData.getPoetry());


        FirebaseUser fireBaseuser = FirebaseAuth.getInstance().getCurrentUser();


        String name = fireBaseuser.getDisplayName();

        likesRef = FirebaseDatabase.getInstance().getReference("Likes");


        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (name != null) {
                    if (snapshot.child(name).hasChild(poetryID)) {
                        //                    int likecount=(int)snapshot.child(name).getChildrenCount();
                        //                    holder.binding.textView4.setText(likecount+" likes");
                        holder.binding.imageView3.setImageResource(R.drawable.ic_favorite);
                    } else {
                        //                    int likecount=(int)snapshot.child(name).getChildrenCount();
                        //                    holder.binding.textView4.setText(likecount+" likes");
                        holder.binding.imageView3.setImageResource(R.drawable.ic_unfav);
                    }
                }



//                if (snapshot.exists())
//                if (snapshot.child(fireBaseuser.getUid()).hasChild(poetryID)) {
////                    int likecount=(int)snapshot.child(name).getChildrenCount();
////                    holder.binding.textView4.setText(likecount+" likes");
//                    holder.binding.imageView3.setImageResource(R.drawable.ic_favorite);
//                } else {
////                    int likecount=(int)snapshot.child(name).getChildrenCount();
////                    holder.binding.textView4.setText(likecount+" likes");
//                    holder.binding.imageView3.setImageResource(R.drawable.ic_unfav);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = true;
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isClicked) {
                            if (snapshot.child(name).child(poetryID).exists()) {
                                likesRef.child(name).child(poetryID).removeValue();
                                Toast.makeText(context, "Poetry Removed From Favourite", Toast.LENGTH_SHORT).show();
                                isClicked = false;
                            } else {
//                                likesRef.child(name).child(poetryID).setValue(data);
                                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
                                isClicked = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.binding.favcopybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.animationcopy.setVisibility(View.VISIBLE);
                holder.binding.favcopybtn.setVisibility(View.INVISIBLE);
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", favModel.get(position).getPoetry());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
//                holder.binding.favcopybtn.setVisibility(View.VISIBLE);


            }
        });


    }

    @Override
    public int getItemCount() {
        return favModel.size();
    }

    public class favViewHolder extends RecyclerView.ViewHolder {

        FavSampleLayoutBinding binding;

        public favViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = FavSampleLayoutBinding.bind(itemView);

        }
    }

}
