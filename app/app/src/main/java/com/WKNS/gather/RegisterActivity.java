package com.WKNS.gather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private EditText mEmail, mPassword, mConfirm_password, mFirstName, mLastName, mPhoneNum;
    private Button mCancel_btn, mRegister_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail               = findViewById(R.id.editText_emailAddress_register);
        mPassword            = findViewById(R.id.editText_password_register);
        mConfirm_password    = findViewById(R.id.editText_confirm_password);
        mFirstName           = findViewById(R.id.editText_firstName);
        mLastName            = findViewById(R.id.editText_lastName);
        mPhoneNum            = findViewById(R.id.editText_phone_number);
        mCancel_btn          = findViewById(R.id.cancel_register);
        mRegister_btn        = findViewById(R.id.login_register);
        mAuth                = FirebaseAuth.getInstance();
        db                   = FirebaseFirestore.getInstance();

        // cancel button handler
        mCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        // register button handler
        mRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirm_password = mConfirm_password.getText().toString().trim();
                final String first_name = mFirstName.getText().toString().trim();
                final String last_name = mLastName.getText().toString().trim();
                final String phoneNum = mPhoneNum.getText().toString().trim();

                // input validation
                if (email.isEmpty()) {
                    mEmail.setError("Email is required.");
                    mEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Please provide valid email");
                    mEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    mPassword.setError("Password is required.");
                    mPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long.");
                    mPassword.requestFocus();
                    return;
                }
                if (confirm_password.isEmpty()) {
                    mConfirm_password.setError("Please confirm password.");
                    mConfirm_password.requestFocus();
                    return;
                }
                if (!confirm_password.equals(password)) {
                    mPassword.setError("Passwords do not match.");
                    mConfirm_password.setError("Passwords do not match.");
                    mConfirm_password.requestFocus();
                    return;
                }
                if (first_name.isEmpty()) {
                    mFirstName.setError("First name is required.");
                    mFirstName.requestFocus();
                    return;
                }
                if (last_name.isEmpty()) {
                    mLastName.setError("Last name is required.");
                    mLastName.requestFocus();
                    return;
                }
                if(!phoneNum.isEmpty() && !android.util.Patterns.PHONE.matcher(phoneNum).matches()) {
                    mPhoneNum.setError("Please enter valid phone number.");
                    mPhoneNum.requestFocus();
                    return;
                }

                // authenticating user with firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendVerificationEmail();
                            storeUser("", email, phoneNum, first_name, last_name);
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void sendVerificationEmail() {
        mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Email verification sent!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Email verification failed: " + e.toString());
            }
        });
    }

    // creates user document in users collection in Firebase
    private void storeUser(String profileImg, String email, String phoneNum, String firstName, String lastName) {
        DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid());
        final Map<String, Object> newUser = new HashMap<>();
        newUser.put("profileImg", profileImg);
        newUser.put("email", email);
        newUser.put("phoneNum", phoneNum);
        newUser.put("firstName", firstName);
        newUser.put("lastName", lastName);
        newUser.put("bio", "");

        documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "user stored in db successfully");
                Toast.makeText(RegisterActivity.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "user storage in db failed: " + e.toString());
                finish();
            }
        });
    }
}