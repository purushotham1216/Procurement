package com.mine.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class NavActivty extends AppCompatActivity {

    NavigationView navigationView;
    Animation animation;
    TextView name;
    private static int SPLASH_TIME_OUT = 4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_activty);

        navigationView = findViewById(R.id.nav_view);
        name = findViewById(R.id.prof_name);
        animation = AnimationUtils.loadAnimation(NavActivty.this,R.anim.move_left);
        navigationView.startAnimation(animation);

    }
}
