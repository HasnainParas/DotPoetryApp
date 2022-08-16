package com.dotaustere.dotpoetry.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.dotaustere.dotpoetry.Fragments.FavouriteFragment;
import com.dotaustere.dotpoetry.Fragments.HomeFragment;
import com.dotaustere.dotpoetry.Fragments.MoreFragment;
import com.dotaustere.dotpoetry.Fragments.SearchFragment;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    private AdView mAdView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    boolean doubleBackPress = false;

    FirebaseUser fireBaseuser;
    DatabaseReference reference, forpic, favRef;
    FirebaseAuth auth;

    String name;
    TextView userNAME, favTv;
    CircleImageView mPic;
    String iMgUrl;
    AlertDialog alertDialog;
    Snackbar snackbar;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setTitle("Dot Poetry");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                super.onAdLoaded();
                Toast.makeText(MainActivity.this, "Banner Ad Loaded ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    if (isOnline(context)) {
                        binding.conStraintLayoutMain.setVisibility(View.VISIBLE);
                        binding.offlineLayout.setVisibility(View.INVISIBLE);
                        snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "Network Connected", Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getResources().getColor(R.color.appColor));
                        snackbar.show();

//                        Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();

                    } else {
                        binding.offlineLayout.setVisibility(View.VISIBLE);
                        binding.conStraintLayoutMain.setVisibility(View.INVISIBLE);
                        snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "No Internet Available", Snackbar.LENGTH_LONG);
                        snackbar.show();
//                        Toast.makeText(context, "No Internet Available", Toast.LENGTH_SHORT).show();

                    }


                } catch (
                        NullPointerException e) {
                    e.printStackTrace();

                }
//                binding.tryAgainBtn.setOnClickListener(view -> {
//                    recreate();
//                });


            }
        };
        registerNetworkBroadCastReceiver();


        reference = FirebaseDatabase.getInstance().getReference().child("meh");
        forpic = FirebaseDatabase.getInstance().getReference("profiles");
        favRef = FirebaseDatabase.getInstance().getReference("Likes");

        FragmentTransaction hometrans = getSupportFragmentManager().beginTransaction();
        hometrans.replace(R.id.content, new HomeFragment());
        hometrans.commit();

//        bottomBar = findViewById(R.id.bottomNav);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        auth = FirebaseAuth.getInstance();

        fireBaseuser = auth.getCurrentUser();

        name = fireBaseuser.getDisplayName();

        String uid = fireBaseuser.getUid();


        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.start,
                R.string.close);

//        toggle.setDrawerIndicatorEnabled();
//
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//
//        toggle.setHomeAsUpIndicator(R.drawable.ic_home);


        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
        userNAME = navigationView.getHeaderView(0).findViewById(R.id.userName);
        mPic = navigationView.getHeaderView(0).findViewById(R.id.userPictuRe);
        favTv = navigationView.getHeaderView(0).findViewById(R.id.favouriteLike);


        userNAME.setText(name);


        forpic.child(fireBaseuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    iMgUrl = snapshot.child("profile").getValue().toString();
                    Glide.with(MainActivity.this).load(iMgUrl)
                            .placeholder(R.drawable.placeholder).into(mPic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(name).exists()) {
                    int likecount = (int) snapshot.child(name).getChildrenCount();
                    favTv.setText(likecount + " Favourite's");
                }


//                if (snapshot.child(auth.getUid()).exists()) {
//                    int likecount = (int) snapshot.child(auth.getUid()).getChildrenCount();
//                    favTv.setText(likecount + " Favourite's");
//                } else {
//                    int likecount = (int) snapshot.child(auth.getUid()).getChildrenCount();
//                    favTv.setText(likecount + " Favourite's");
//
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        bottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
//            @Override
//            public void onItemSelected(int i) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                switch (i) {
//                    case 0:
//                        transaction.replace(R.id.content, new HomeFragment());
//                        break;
//                    case 1:
//                        transaction.replace(R.id.content, new SearchFragment());
//                        break;
//                    case 2:
//                        transaction.replace(R.id.content, new FavouriteFragment());
//                        break;
//                    case 3:
//                        transaction.replace(R.id.content, new MoreFragment());
////                        Toast.makeText(MainActivity.this, "Thanks", Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(MainActivity.this, MoreActivity.class);
////                        startActivity(intent);
//                        break;
//
//                }
//                transaction.commit();
//
//            }
//        });
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.home, R.id.favourite, R.id.search)
//                .build();


        //Default BottomBar

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home:
                        transaction.replace(R.id.content, new HomeFragment());
                        break;
                    case R.id.search:
                        transaction.replace(R.id.content, new SearchFragment());
                        break;
                    case R.id.favourite:
                        transaction.replace(R.id.content, new FavouriteFragment());
                        break;
                    case R.id.more:
                        transaction.replace(R.id.content, new MoreFragment());
                        break;

                }
                transaction.commit();
                return true;
            }
        });


    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());


        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;

        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {

            case R.id.home1:
                fragmentTransaction.replace(R.id.content, new HomeFragment());
                Toast.makeText(this, "Wow you Clicked Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favourite1:
                fragmentTransaction.replace(R.id.content, new FavouriteFragment());
                Toast.makeText(this, "Wow you Clicked Favourite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.poets:
                Toast.makeText(this, "Wow you Clicked Poets", Toast.LENGTH_SHORT).show();
                break;
            case R.id.shayari:
                Toast.makeText(this, "Wow you Clicked Shayari", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_shayari:
                Toast.makeText(this, "Wow you Clicked Image Shayari", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Wow you Clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                Toast.makeText(this, name + " SignOut Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
        fragmentTransaction.commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return true;
    }


//    private void showInternetDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//
//        View view = LayoutInflater.from(this).inflate(R.layout.no_internet_main,
//                findViewById(R.id.no_net_layout_main));
//        view.findViewById(R.id.try_Again_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isConnected(MainActivity.this)) {
//                    showInternetDialog();
//                } else {
//
//
//                }
//            }
//        });
//
//        builder.setView(view);
//
//        alertDialog = builder.create();
//
//        alertDialog.show();
//
//    }
//
//    private boolean isConnected(MainActivity dashboardActivity) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) dashboardActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
//    }


    protected void registerNetworkBroadCastReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }

    }

    protected void unregisteredNetwork() {
        try {

            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisteredNetwork();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackPress) {
            super.onBackPressed();
            return;
        }
        this.doubleBackPress = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackPress = false;
            }
        }, 2000);

    }
}