package com.example.glutenfree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    Animation animationFadeIn;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.logo);
        Thread thread=new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        MediaPlayer m=MediaPlayer.create(Splash.this,R.raw.splash);
                        m.start();
                        Animation animation= AnimationUtils.loadAnimation(Splash.this,R.anim.anim1);
                        imageView.startAnimation(animation);
                        wait(5000);
                        m.stop();

                    }
                }catch (InterruptedException e) {
                }
                finish();
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);


            }
        };
        thread.start();
    }
}