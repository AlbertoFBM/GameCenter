package com.aserrano.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4500;

    ImageView logo;
    TextView title, createdBy, version;
    Animation topAnim, bottomAnim, rotateAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        // ID's

        logo = findViewById(R.id.imageLogo);
        title = findViewById(R.id.title);
        version = findViewById(R.id.version);
        createdBy = findViewById(R.id.authorName);


        // Animations

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);

        // Set animations

        title.setAnimation(topAnim);
        logo.setAnimation(rotateAnim);
        version.setAnimation(bottomAnim);
        createdBy.setAnimation(bottomAnim);


        // Splash

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        }, SPLASH_SCREEN);
    }

    @Override
    public void onBackPressed() { }
}