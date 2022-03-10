package com.aserrano.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Database db;
    private EditText editUsername, editEmail, editPassword, editRepeatPassword;
    private boolean visible_1 = false;
    private boolean visible_2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = new Database(this);

        editUsername = (EditText) findViewById(R.id.editUser);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editRepeatPassword = (EditText) findViewById(R.id.editPassword2);

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible_1){
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visible_1 = false;
                    editPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24, 0);
                }else{
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visible_1 = true;
                    editPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24, 0);
                }
            }
        });
        editRepeatPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible_2){
                    editRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visible_2 = false;
                    editRepeatPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24, 0);
                }else{
                    editRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visible_2 = true;
                    editRepeatPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24, 0);
                }
            }
        });

    }

    public void goToLogin(View view) {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() { }

    public void checkSignUp(View view) {

        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String repeatPassword = editRepeatPassword.getText().toString();
        int score2048 = 0;
        int scorePEG = 0;

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)){
            Toast.makeText(getApplicationContext(), "Username and password are required", Toast.LENGTH_SHORT).show();
        }else{
            if(password.equals(repeatPassword)){
                boolean checkUser = db.checkUsername(username);
                if (!checkUser){
                    boolean insert = db.insert(username, email, password, score2048, scorePEG);
                    if (insert){
                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MenuActivity.class);
                        intent.putExtra("user", username);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Registered failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Passwords aren't equals ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}