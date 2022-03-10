package com.aserrano.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameOverScreen extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");

    }

    @Override
    public void onBackPressed() { }

    public void restartGame(View v){
        Intent intent = new Intent(GameOverScreen.this, Game2048Activity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }

    public void returnMenu(View view) {
        Intent intent = new Intent(GameOverScreen.this, MenuActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }
}