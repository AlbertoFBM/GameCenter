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

public class ChangePassActivity extends AppCompatActivity {

    private Database db;

    private String username;

    private EditText editUser, editPass, editRepeatPass;

    private boolean visible_1 = false;
    private boolean visible_2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        db = new Database(this);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");

        editUser = (EditText) findViewById(R.id.editUserChange);
        editPass = (EditText) findViewById(R.id.editPasswordChange);
        editRepeatPass = (EditText) findViewById(R.id.editRepeatPasswordChange);

        editUser.setText(username);

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible_1){
                    editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visible_1 = false;
                    editPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24, 0);
                }else{
                    editPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visible_1 = true;
                    editPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24, 0);
                }
            }
        });
        editRepeatPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible_2){
                    editRepeatPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visible_2 = false;
                    editRepeatPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24, 0);
                }else{
                    editRepeatPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visible_2 = true;
                    editRepeatPass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24, 0);
                }
            }
        });
    }


    public void goToSettings(View view) {

        Intent intent = new Intent(ChangePassActivity.this, SettingsActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);

    }

    public void changePassword(View view) {

        String us = editUser.getText().toString();
        String pass = editPass.getText().toString();
        String rpass = editRepeatPass.getText().toString();

        if (TextUtils.isEmpty(us) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(rpass)){
            Toast.makeText(getApplicationContext(), "Username and password are required", Toast.LENGTH_SHORT).show();
        }else{
            if (pass.equals(rpass)){
                boolean checkUser = db.checkUsername(us);
                if (checkUser){
                    boolean update = db.updatePassword(us, pass);
                    if (update){
                        Toast.makeText(getApplicationContext(), "Change Password Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "You have saved that same password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "User doesn't exists", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Passwords aren't equals ", Toast.LENGTH_SHORT).show();
            }
        }

    }
}