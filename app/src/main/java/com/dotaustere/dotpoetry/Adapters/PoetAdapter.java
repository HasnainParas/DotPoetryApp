package com.dotaustere.dotpoetry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dotaustere.dotpoetry.Models.PoetModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.Activities.ShayariActivity;
import com.dotaustere.dotpoetry.databinding.PoetLayoutBinding;

import java.util.ArrayList;

public class PoetAdapter extends RecyclerView.Adapter<PoetAdapter.viewHolder> {

    ArrayList<PoetModel> list;
    Context context;
//    String name;

    public PoetAdapter(ArrayList<PoetModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.poet_layout, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PoetModel model = list.get(position);


        try {
            Glide.with(context).load(model.getPoetPic()).placeholder(R.drawable.logo).into(holder.binding.poetImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.binding.poetNAME.setText(model.getPoetName());
        holder.binding.poetAge.setText(model.getAgeStart());
        holder.binding.poetAgeEnd.setText(model.getAgeEnd());

        holder.binding.poetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String name = model.getPoetName();
                String poetuID = model.getPoetName();
                Intent intent = new Intent(context, ShayariActivity.class);
                intent.putExtra("poet", poetuID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        PoetLayoutBinding binding;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PoetLayoutBinding.bind(itemView);

        }
    }

}
