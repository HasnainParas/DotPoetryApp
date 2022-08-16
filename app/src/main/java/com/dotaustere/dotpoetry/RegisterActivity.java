package com.dotaustere.dotpoetry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.dotaustere.dotpoetry.Activities.MainActivity;
import com.dotaustere.dotpoetry.Activities.SignUpActivity;
import com.dotaustere.dotpoetry.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    String email, password;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);

        binding.logInBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateEmail();
                validatePassword();
                dialog.show();
                email = binding.emailBox.getText().toString();
                password = binding.passwordBox2.getText().toString();

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                            Toasty.success(RegisterActivity.this, "Ju na", Toasty.LENGTH_SHORT, true).show();

                        } else {

                            Toasty.error(RegisterActivity.this, "Authentication Failed " + task.getException(), Toasty.LENGTH_SHORT, true).show();

                        }

                    }
                });
                dialog.dismiss();


            }
        });


    }


    private boolean validateEmail() {
        String emailInput = binding.emailBox.getText().toString().trim();
        if (emailInput.isEmpty()) {
            binding.emailBox.setError("Field can't be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.emailBox.setError("Please enter a valid email address");
            return false;
        } else {
            binding.emailBox.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = binding.passwordBox.getText().toString().trim();
        String ConfirmpasswordInput = binding.passwordBox2.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            binding.passwordBox.setError("Field can't be empty");

            return false;
        }
        if (passwordInput.length() < 5) {
            binding.passwordBox.setError("Password must be at least 5 characters");
            return false;
        }
        if (!passwordInput.equals(ConfirmpasswordInput)) {
            binding.passwordBox2.setError("Password Would Not be matched");
            return false;
        } else {
//            Toasty.success(this, "Password Match", Toasty.LENGTH_SHORT, true).show();
            Toast.makeText(this, "Password Match", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


}