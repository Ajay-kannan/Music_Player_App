package com.example.myapplicationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    TextInputEditText email,password;
    Button btn_reg;
    FirebaseAuth mAuth;
    TextView btn_next_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        btn_reg = (Button) findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();

        btn_next_login = (TextView) findViewById(R.id.nextLogin);

        btn_next_login.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext() , Login.class);
            startActivity(i);
            finish();
        });

        btn_reg.setOnClickListener(v -> {
            String email_text, password_text ;
            email_text = email.getText().toString();
            password_text = password.getText().toString();

            if(TextUtils.isEmpty(email_text))
            {
                Toast.makeText(getApplicationContext(),"Enter the email", Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(password_text))
            {
                Toast.makeText(getApplicationContext(),"Enter the email", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email_text, password_text)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Account created.",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext() , Login.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        });
    }
}