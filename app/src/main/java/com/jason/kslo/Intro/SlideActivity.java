package com.jason.kslo.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.jason.kslo.R;

import java.util.Objects;

public class SlideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    SlideViewPagerAdapter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slide_acticity);

        viewPager = findViewById(R.id.IntroViewPager);
        Adapter = new SlideViewPagerAdapter(this);
        viewPager.setAdapter(Adapter);
    }
}