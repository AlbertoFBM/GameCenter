package com.aserrano.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WinnerPEGActivity extends AppCompatActivity {

    private ImageView star;
    private Animation rotate;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peg_winner);

        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");

        // Animations
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        // ID's
        star = (ImageView) findViewById(R.id.star_winner);

        star.setAnimation(rotate);
    }


    public void returnMenu(View view) {
        Intent intent = new Intent(WinnerPEGActivity.this, MenuActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }

    public void returnPeg(View view) {
        Intent intent = new Intent(WinnerPEGActivity.this, GamePEGActivity.class);
        intent.putExtra("user", username);
        startActivity(intent);
    }

}