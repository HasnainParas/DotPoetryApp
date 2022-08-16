package com.dotaustere.dotpoetry.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dotaustere.dotpoetry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MoreActivity extends AppCompatActivity {

    FirebaseUser fireBaseuser;
    DatabaseReference likesRef;
    FirebaseDatabase database;

    String name;
    TextView poetname, fav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        getSupportActionBar().hide();
        poetname = findViewById(R.id.userName);
        fav = findViewById(R.id.favM);



        String poetryID = getIntent().getStringExtra("poetryid");


        fireBaseuser = FirebaseAuth.getInstance().getCurrentUser();
        String user = fireBaseuser.getUid();
        name = fireBaseuser.getDisplayName();

        database = FirebaseDatabase.getInstance();
        likesRef = database.getReference("Likes");


        poetname.setText(name);



//        likesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild(name)) {
//                    int likecount = (int) snapshot.child(poetryID).getChildrenCount();
//                    fav.setText(likecount + " Favourite");
//
//                } else {
//                    int likecount = (int) snapshot.child(poetryID).getChildrenCount();
//                    fav.setText(likecount + " Favourite");
////                    holder.binding.unFavBtn.setImageResource(R.drawable.ic_unfav);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        poetname.setOnClickListener(view -> {

            Toast.makeText(MoreActivity.this, name + " Logged in", Toast.LENGTH_SHORT).show();

        });


    }
}