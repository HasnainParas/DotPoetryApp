package com.dotaustere.dotpoetry.Adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotaustere.dotpoetry.Models.FavModel;
import com.dotaustere.dotpoetry.Models.ShayariModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.PoetryLayoutUserBinding;
import com.google.firebase.appcheck.interop.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShayariAdapter extends RecyclerView.Adapter<ShayariAdapter.viewHolderS> {

    DatabaseReference likesRef;
    FirebaseDatabase database;

    Boolean isClicked = false;

    ArrayList<ShayariModel> shayariModel;
    Context context;

    public ShayariAdapter(ArrayList<ShayariModel> shayariModel, Context context) {
        this.shayariModel = shayariModel;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderS onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.poetry_layout_user, parent, false);
        return new viewHolderS(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderS holder, @SuppressLint("RecyclerView") int position) {


        ShayariModel modelData = shayariModel.get(position);


        String poetryID = modelData.getPoetryId();

        holder.binding.shayariTv.setText(modelData.getPoetry());

        String poeetry = modelData.getPoetry();

//        Intent intent = new Intent(context,MoreActivity.class);
//        intent.putExtra("poetryid",poetryID);
//        context.startActivity(intent);


        FirebaseUser fireBaseuser = FirebaseAuth.getInstance().getCurrentUser();
        String user = fireBaseuser.getUid();

        String name = fireBaseuser.getDisplayName();
        database = FirebaseDatabase.getInstance();
        likesRef = database.getReference("Likes");

        FavModel data = new FavModel(user, poeetry, poetryID);


        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (name != null) {
                    if (snapshot.child(name).hasChild(poetryID)) {
                        //                    int likecount=(int)snapshot.child(name).getChildrenCount();
                        //                    holder.binding.textView4.setText(likecount+" likes");
                        holder.binding.unFavBtn.setImageResource(R.drawable.ic_favorite);
                    } else {
                        //                    int likecount=(int)snapshot.child(name).getChildrenCount();
                        //                    holder.binding.textView4.setText(likecount+" likes");
                        holder.binding.unFavBtn.setImageResource(R.drawable.ic_unfav);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.binding.unFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = true;

                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (isClicked) {
                            if (snapshot.child(name).child(poetryID).exists()) {
                                likesRef.child(name).child(poetryID).removeValue();
                                holder.binding.favanim.setVisibility(View.INVISIBLE);
                                isClicked = false;
                            } else {
                                likesRef.child(name).child(poetryID).setValue(data);
                                holder.binding.favanim.setVisibility(View.VISIBLE);
                                holder.binding.unFavBtn.setVisibility(View.INVISIBLE);
                                holder.binding.unFavBtn.setVisibility(View.VISIBLE);
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

        holder.binding.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.copyBtn.setVisibility(View.INVISIBLE);
                holder.binding.copyanim.setVisibility(View.VISIBLE);
                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", shayariModel.get(position).getPoetry());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
//                holder.binding.copyBtn.setVisibility(View.VISIBLE);


            }
        });

        holder.binding.poetryShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent sharePoetry = new Intent(Intent.ACTION_SEND);
                    sharePoetry.setType("text/plain");
                    sharePoetry.putExtra(Intent.EXTRA_SUBJECT, "Share");
//                    String shareMessage = "https://play.google.com/store/apps/details?=" + BuildConfig.APPLICATION_ID + "\n\n";
                    sharePoetry.putExtra(Intent.EXTRA_TEXT, shayariModel.get(position).getPoetry());
//                    context.startActivity(Intent.createChooser(intent, "Share by Dot Austere"));
                    context.startActivity(sharePoetry);
                } catch (Exception e) {
                    Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return shayariModel.size();
    }

    public class viewHolderS extends RecyclerView.ViewHolder {

        PoetryLayoutUserBinding binding;

        public viewHolderS(@NonNull View itemView) {
            super(itemView);
            binding = PoetryLayoutUserBinding.bind(itemView);


        }
    }

}

