package com.dotaustere.adminpaneldotpoetry.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dotaustere.adminpaneldotpoetry.Activities.AddingPoetsActivity;
import com.dotaustere.adminpaneldotpoetry.Models.AdminPoetsModel;
import com.dotaustere.adminpaneldotpoetry.Activities.PoetryActivity;
import com.dotaustere.adminpaneldotpoetry.R;
import com.dotaustere.adminpaneldotpoetry.databinding.AdminRecyclerPoetLayoutBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class AdminPoetsAdapter extends RecyclerView.Adapter<AdminPoetsAdapter.viewHolder> {

    DatabaseReference databaseReference, forDeletePoetry;
    FirebaseStorage storage;
    StorageReference storageReference;


    ArrayList<AdminPoetsModel> list;
    Context context;

    public AdminPoetsAdapter(ArrayList<AdminPoetsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.admin_recycler_poet_layout, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        AdminPoetsModel model = list.get(position);

        databaseReference = FirebaseDatabase.getInstance().getReference("Poets");
        forDeletePoetry = FirebaseDatabase.getInstance().getReference("Poetry");
        storage = FirebaseStorage.getInstance();


        try {
            Glide.with(context).load(model.getPoetPic()).into(holder.binding.poetImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.binding.poetName.setText(model.getPoetName());
        holder.binding.poetAge.setText(model.getAgeStart());
        holder.binding.poetAgeEnd.setText(model.getAgeEnd());

        String poetnom = model.getPoetName();

        holder.binding.poetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String poetnom = model.getPoetName();
                Toast.makeText(context, model.getPoetName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PoetryActivity.class);
                intent.putExtra("poet", poetnom);
                context.startActivity(intent);

            }
        });

        String forName = holder.binding.poetName.getText().toString();
        String forUpStart = holder.binding.poetAge.getText().toString();
        String forUpEnd = holder.binding.poetAgeEnd.getText().toString();


        holder.binding.optionBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {

                        case R.id.action_popup_edit:
                            DialogPlus dialogPlus = DialogPlus.newDialog(context)
                                    .setContentHolder(new ViewHolder(R.layout.update_layout))
                                    .setExpanded(true, 1200)
                                    .create();

//
                            View dialogPlusView = dialogPlus.getHolderView();

                            EditText nameUpdate = dialogPlusView.findViewById(R.id.updatepoetname);
                            EditText startAgeUpdate = dialogPlusView.findViewById(R.id.updatepoetAgeStart);
                            EditText endAgeUpdate = dialogPlusView.findViewById(R.id.updatepoetAgeEnd);
                            TextView submitUpdate = dialogPlusView.findViewById(R.id.updatesubmitbtn);
                            CircleImageView upImage = dialogPlusView.findViewById(R.id.upCircleImage);


                            nameUpdate.setText(forName);
                            startAgeUpdate.setText(forUpStart);
                            endAgeUpdate.setText(forUpEnd);

//                            nameUpdate.setEnabled(false);
                            nameUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toasty.error(context, "Poet Name Can't be Edit", Toast.LENGTH_SHORT, true).show();
                                    nameUpdate.setEnabled(false);
                                }
                            });


                            try {
                                Glide.with(context).load(model.getPoetPic()).into(upImage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            submitUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Death Date", endAgeUpdate.getText().toString());
                                    map.put("Born Date", startAgeUpdate.getText().toString());

                                    databaseReference.child(model.getUuID())
                                            .updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    dialogPlus.dismiss();
                                                    Toasty.success(context, "Poet Update Successfully!", Toast.LENGTH_SHORT, true).show();
                                                    notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toasty.success(context, "Error While updating", Toast.LENGTH_SHORT, true).show();
                                            dialogPlus.dismiss();
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                            dialogPlus.show();
                            break;
                        case R.id.action_popup_delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Are you sure?");
                            builder.setMessage("Deleted data can't be undo\n" +
                                    "All pottery data will be DELETE");

                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    databaseReference.child(model.getUuID()).removeValue();
                                    forDeletePoetry.child(model.getPoetName()).removeValue();


                                    storageReference = storage.getReferenceFromUrl(model.getPoetPic());
                                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toasty.success(context, "Deleted Successfully", Toasty.LENGTH_SHORT, true).show();
                                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                                            Log.e("Picture", "#deleted");
                                        }
                                    });


                                    notifyDataSetChanged();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toasty.success(context, "Cancelled", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                            builder.show();


                            break;
                    }


                    return false;
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        AdminRecyclerPoetLayoutBinding binding;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdminRecyclerPoetLayoutBinding.bind(itemView);
        }
    }

//    private void uploadData() {


//    }


}