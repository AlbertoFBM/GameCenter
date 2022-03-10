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

public class LoginActivity extends AppCompatActivity {

    private Database db;
    private EditText editUsername, editPassword;
    private boolean visible = false;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database(this);
        editUsername = (EditText) findViewById(R.id.editUser);
        editPassword = (EditText) findViewById(R.id.editPassword);

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible){
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visible = false;
                    editPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24, 0);
                }else{
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visible = true;
                    editPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24, 0);
                }
            }
        });
    }

    public void goToMenu(View view){

        username = editUsername.getText().toString();
        password = editPassword.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
        }else{
            boolean checkLogin = db.checkLogin(username, password);
            if (checkLogin){
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("user", username);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void forgotPass(View view) {
        Intent intent = new Intent(LoginActivity.this, ChangePassActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
        finish();
    }

    public void goToSignUp(View view){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() { }
}