package com.jason.kslo.intro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.jason.kslo.R;

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