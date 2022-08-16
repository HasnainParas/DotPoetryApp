package com.dotaustere.dotpoetry.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dotaustere.dotpoetry.Models.User;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.RegisterActivity;
import com.dotaustere.dotpoetry.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    TextView googleLOGInBtn;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 11;

    FirebaseAuth auth;
    FirebaseDatabase database;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

//        if (!isConnected(this)) {
//            showInternetDialog();
//        } else {
//
//
//            auth = FirebaseAuth.getInstance();
//
//            if (auth.getCurrentUser() != null) {
//                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                finish();
//
//            }
//
//        }

//        else {
//
//            auth = FirebaseAuth.getInstance();
//
//            if (auth.getCurrentUser() != null) {
//                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                finish();
//
//            }
//
//
//            database = FirebaseDatabase.getInstance();
//
//
//            googleLOGInBtn = findViewById(R.id.googleSignIn);
//
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();
//
//            googleSignInClient = GoogleSignIn.getClient(this, gso);
//
//            googleLOGInBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = googleSignInClient.getSignInIntent();
//                    startActivityForResult(intent, RC_SIGN_IN);
//                    binding.loggingloader.setVisibility(View.VISIBLE);
//
//
//                }
//            });
//
//
//        }


        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();

        }


        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isConnected(SignUpActivity.this)) {
                    showInternetDialog();

                } else {
                    Intent intent = googleSignInClient.getSignInIntent();
                    binding.loggingloader.setVisibility(View.VISIBLE);
                    startActivityForResult(intent, RC_SIGN_IN);
                }
            }
        });

//        binding.fireBaseLogInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                email = binding.emailBox.getText().toString();
//                password = binding.passwordBox.getText().toString();
//                if (email.isEmpty()) {
//                    binding.emailBox.setError("Field can't be empty");
//                    return;
//                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    binding.emailBox.setError("Please enter a valid email address");
//                    return;
//                } else {
//
//                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//                            if (task.isSuccessful()) {
//                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                                Toast.makeText(SignUpActivity.this, "Juuu ", Toast.LENGTH_SHORT).show();
//                                finish();
//
//                            } else {
//                                Toast.makeText(SignUpActivity.this, "Failed... \n" + task.getException(), Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    });
//                }
//
//
//            }
//        });
//        binding.fireBaseSignUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SignUpActivity.this, RegisterActivity.class));
//                finish();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();
            authWithGoogle(account.getIdToken());


        }


    }

    void authWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            User firebaseuser = new User(user.getUid(),
                                    user.getDisplayName(),
                                    user.getPhotoUrl().toString());

                            database.getReference().child("profiles")
                                    .child(user.getUid())
                                    .setValue(firebaseuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finishAffinity();
                                        binding.loggingloader.setVisibility(View.INVISIBLE);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            Log.e("profile", user.getPhotoUrl().toString());
                        } else {
                            Log.e("err", task.getException().getLocalizedMessage());

                        }
                    }
                });


    }


    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage("Please Connect to the Internet to Sign Up")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recreate();
                    }
                });
        builder.show();
//        View view = LayoutInflater.from(this).inflate(R.layout.no_internet_dialog, findViewById(R.id.no_internet_layout));
//        view.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isConnected(SignUpActivity.this)) {
//                    showInternetDialog();
//                } else {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
//                }
//            }
//        });

//        builder.setView(view);
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();

    }

    private boolean isConnected(SignUpActivity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {

            return false;

        }
    }

//    private boolean validateEmail() {
//        String emailInput = binding.emailBox.getText().toString().trim();
//        if (emailInput.isEmpty()) {
//            binding.emailBox.setError("Field can't be empty");
//            return false;
//
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
//            binding.emailBox.setError("Please enter a valid email address");
//            return false;
//        } else {
//            binding.emailBox.setError(null);
//            return true;
//        }
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();

    }
}