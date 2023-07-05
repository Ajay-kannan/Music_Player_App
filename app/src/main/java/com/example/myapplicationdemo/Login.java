package com.example.myapplicationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputEditText email,password;
    Button btn_login;
    FirebaseAuth mAuth;
    TextView btn_next_register;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext() , MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_next_register = (TextView) findViewById(R.id.nextRegister);

        btn_next_register.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(v -> {
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

            mAuth.signInWithEmailAndPassword(email_text, password_text)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext() , "Login successfully ", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext() , OtpLogin.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}