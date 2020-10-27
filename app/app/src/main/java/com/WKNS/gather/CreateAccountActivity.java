package com.WKNS.gather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private EditText name;
    private Button cancel_btn;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email = findViewById(R.id.editText_emailAddress_create);
        password = findViewById(R.id.editText_password_create);
        confirm_password = findViewById(R.id.editText_confirm_password);
        name = findViewById(R.id.editText_name);
        cancel_btn = findViewById(R.id.cancel_create_account);
        login_btn = findViewById(R.id.login_create_account);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                CreateAccountActivity.this.startActivity(intent);
            }
        });
    }
}
