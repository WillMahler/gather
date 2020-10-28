package com.WKNS.gather;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail, mPassword, mConfirm_password, mName;
    private Button mCancel_btn, mLogin_btn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail               = findViewById(R.id.editText_emailAddress_register);
        mPassword            = findViewById(R.id.editText_password_register);
        mConfirm_password    = findViewById(R.id.editText_confirm_password);
        mName                = findViewById(R.id.editText_name);
        mCancel_btn          = findViewById(R.id.cancel_register);
        mLogin_btn           = findViewById(R.id.login_register);
        mAuth                = FirebaseAuth.getInstance();

        mCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirm_password = mConfirm_password.getText().toString().trim();

                // input validation
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long.");
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    mConfirm_password.setError("Please confirm password.");
                    return;
                }
                if (!confirm_password.equals(password)) {
                    mPassword.setError("Passwords do not match.");
                    mConfirm_password.setError("Passwords do not match.");
                    return;
                }

                // registering user in firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sending verification email
                            user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(RegisterActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(RegisterActivity.this, "Error: Verification email not sent, " +e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
