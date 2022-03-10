package com.aserrano.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Winner2048Activity extends AppCompatActivity {

    private ImageView star;
    private Animation rotate;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner2048);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");

        // Animations
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        // ID's
        star = (ImageView) findViewById(R.id.star_winner);

        star.setAnimation(rotate);
    }

    public void returnMenu(View view) {
        Intent intent = new Intent(Winner2048Activity.this, MenuActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }

    public void return2048(View view) {
        Intent intent = new Intent(Winner2048Activity.this, Game2048Activity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }
}