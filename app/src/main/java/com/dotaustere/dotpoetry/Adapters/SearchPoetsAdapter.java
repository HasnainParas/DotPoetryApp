package com.dotaustere.dotpoetry.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dotaustere.dotpoetry.Activities.SearchShayariActivity;
import com.dotaustere.dotpoetry.Models.PoetModel;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.SearchLayoutBinding;

import java.util.ArrayList;

public class SearchPoetsAdapter extends RecyclerView.Adapter<SearchPoetsAdapter.viewHolder> {

    ArrayList<PoetModel> poetModel;
    Context context;

    public SearchPoetsAdapter(ArrayList<PoetModel> poetModel, Context context) {
        this.poetModel = poetModel;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_layout, parent, false);
        return new SearchPoetsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PoetModel modelData = poetModel.get(position);

        try {
            Glide.with(context).load(modelData.getPoetPic()).placeholder(R.drawable.logo).into(holder.binding.searchCircleIV);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.binding.searchPoetName.setText(modelData.getPoetName());

        holder.binding.searchPoetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = modelData.getPoetName();
                Intent intent = new Intent(context, SearchShayariActivity.class);
                intent.putExtra("sPoet", name);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return poetModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        SearchLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchLayoutBinding.bind(itemView);
        }
    }

    public void filterList(ArrayList<PoetModel> filterList) {
        poetModel = filterList;


        notifyDataSetChanged();

    }

}
