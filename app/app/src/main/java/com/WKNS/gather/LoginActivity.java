package com.WKNS.gather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    public static final String TAG = "TAG";

    private EditText mEmail, mPassword;
    private Button mLogin_btn, mRegister_btn, mForgotPassword_btn;
    private SignInButton mSignInGoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ui elements
        mEmail              = findViewById(R.id.editText_emailAddress);
        mPassword           = findViewById(R.id.editText_password);
        mLogin_btn          = findViewById(R.id.login_btn);
        mSignInGoogle       = findViewById(R.id.googleSignInButton);
        mRegister_btn       = findViewById(R.id.register_with_email);
        mForgotPassword_btn = findViewById(R.id.forgot_password);

        // Firebase and Google api
        mAuth                   = FirebaseAuth.getInstance();
        googleSignInOptions     = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken("8447612036-rb3c55vtu67pnd073efocp9008c0ii78.apps.googleusercontent.com")
                                .requestEmail()
                                .build();
        googleSignInClient      = GoogleSignIn.getClient(this, googleSignInOptions);
        googleSignInAccount     = GoogleSignIn.getLastSignedInAccount(this);
        db                      = FirebaseFirestore.getInstance();

        // checking to see if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            if (!mAuth.getCurrentUser().isEmailVerified()) {
                Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
            }
            else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
        else if (googleSignInAccount != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // log in button handler
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // input validation
                if (email.isEmpty()) {
                    mEmail.setError("Email is Required.");
                    mEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    mPassword.setError("Password is Required.");
                    mPassword.requestFocus();
                    return;
                }

                // authenticating user with Firebase
                signInWithEmail(email, password);
            }
        });

        // sign in with Google button handler
        mSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, GOOGLE_SIGN_IN_CODE);
            }
        });

        // register button handler
        mRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        // forgot password button handler
        mForgotPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout container = new LinearLayout(getApplicationContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(50, 0, 50, 0);

                final EditText user_email = new EditText(v.getContext());
                user_email.setLayoutParams(lp);
                user_email.setLayoutParams(lp);
                user_email.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
                user_email.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                user_email.setLines(1);
                user_email.setMaxLines(1);
                container.addView(user_email, lp);

                AlertDialog.Builder password_reset_dialog = new AlertDialog.Builder(v.getContext());
                password_reset_dialog.setTitle(R.string.label_reset_pass);
                password_reset_dialog.setMessage(R.string.label_reset_pass_message);
                password_reset_dialog.setView(container);

                password_reset_dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = user_email.getText().toString();
                        sendResetPasswordLink(email);
                    }
                });

                password_reset_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                password_reset_dialog.create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                final GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);
                final AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, "Log in Success.", Toast.LENGTH_SHORT).show();
                        storeUser(signInAccount.getEmail(), signInAccount.getGivenName(), signInAccount.getFamilyName(), signInAccount.getPhotoUrl().toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "sign in with google failed: " + e.toString());
                    }
                });
            }

            catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void signInWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "Log in Success.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendResetPasswordLink(String email) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, "Reset link sent to provided email.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error: Reset link not sent, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // creates user document in users collection in Firebase
    private void storeUser(String email, String firstName, String lastName, String profileImg) {
        DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid());
        final Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", email);
        newUser.put("firstName", firstName);
        newUser.put("lastName", lastName);
        newUser.put("profileImg", profileImg);

        documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "user stored in db successfully");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
