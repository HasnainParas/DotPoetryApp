package com.dotaustere.adminpaneldotpoetry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotaustere.adminpaneldotpoetry.Models.ShayariModelAdmin;
import com.dotaustere.adminpaneldotpoetry.R;
import com.dotaustere.adminpaneldotpoetry.databinding.ShayariLayoutAdminBinding;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ShayariAdapterAdmin extends RecyclerView.Adapter<ShayariAdapterAdmin.viewHolderS> {

    ArrayList<ShayariModelAdmin> shayariModel;
    Context context;
    String poetName;

    DatabaseReference reference;

    public ShayariAdapterAdmin(ArrayList<ShayariModelAdmin> shayariModel, Context context) {
        this.shayariModel = shayariModel;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolderS onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.shayari_layout_admin, parent, false);
        return new viewHolderS(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderS holder, int position) {

        ShayariModelAdmin data = shayariModel.get(position);


        holder.binding.shayariTv.setText(data.getPoetry());

        holder.binding.copyBtn.setOnClickListener(view -> Toast.makeText(context, "Not Yet Implemented", Toast.LENGTH_SHORT).show());
        holder.binding.deleteBtn.setOnClickListener(view -> Toast.makeText(context, "Not Yet Implemented", Toast.LENGTH_SHORT).show());


    }


    @Override
    public int getItemCount() {
        return shayariModel.size();


    }


    public class viewHolderS extends RecyclerView.ViewHolder {

        ShayariLayoutAdminBinding binding;

        public viewHolderS(@NonNull View itemView) {
            super(itemView);
            binding = ShayariLayoutAdminBinding.bind(itemView);


        }
    }
}
