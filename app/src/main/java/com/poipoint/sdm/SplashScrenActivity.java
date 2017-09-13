package com.poipoint.sdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashScrenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scren);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        ImageView splashImage=(ImageView)findViewById(R.id.splash_image);
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.1f,1,1.1f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i=new Intent(getApplicationContext(),CategoreyActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashImage.startAnimation(scaleAnimation);

    }
}
