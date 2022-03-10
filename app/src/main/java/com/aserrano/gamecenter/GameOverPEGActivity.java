package com.aserrano.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GameOverPEGActivity extends AppCompatActivity {

    private ImageView gif;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        gif = (ImageView) findViewById(R.id.gif);
        Glide.with(this).load(R.drawable.gif_skeleton).into(gif);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");
    }

    public void returnMenu(View view) {
        Intent intent = new Intent(GameOverPEGActivity.this, MenuActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }

    public void returnPeg(View view) {
        Intent intent = new Intent(GameOverPEGActivity.this, GamePEGActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }
}