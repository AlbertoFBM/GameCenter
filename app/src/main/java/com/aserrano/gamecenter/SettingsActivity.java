package com.aserrano.gamecenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private Database db;
    private TextView user, email, pass, score2048, scorePEG;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new Database(this);

        user = (TextView) findViewById(R.id.user);
        email = (TextView) findViewById(R.id.email);
        pass = (TextView) findViewById(R.id.password);
        score2048 = (TextView) findViewById(R.id.score2048);
        scorePEG = (TextView) findViewById(R.id.scorePEG);

        Bundle extras = getIntent().getExtras();

        mUsername = extras.getString("user");

        String[] results = db.selectAll(mUsername);

        user.setText(results[0]);
        email.setText(results[1]);
        pass.setText("NOT AVAILABLE");
        score2048.setText(String.valueOf(results[2]));
        scorePEG.setText(String.valueOf(results[3]));


    }

    public void goToLogin(View view) {
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
    }

    public void goToSignUp(View view){
        startActivity(new Intent(SettingsActivity.this, SignUpActivity.class));
    }

    public void goToMenu(View view) {
        Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
        intent.putExtra("user", mUsername);
        startActivity(intent);
    }

    public void changePass(View view) {
        Intent intent = new Intent(SettingsActivity.this, ChangePassActivity.class);
        intent.putExtra("user", mUsername);
        startActivity(intent);
    }

    public void deleteUser(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        alert.setTitle("ARE YOU SURE?");
        alert.setMessage(R.string.message_alert_delete_user);
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteUser(mUsername);
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }
}