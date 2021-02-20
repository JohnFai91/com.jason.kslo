package com.jason.kslo.Activity;

import android.os.Bundle;
import android.webkit.WebView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.R;
import com.jason.kslo.ui.main.SectionsPagerAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import java.util.Locale;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
private WebView webView;
    Button btn;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public interface OnBackPressedListener {
        public View doBack();
    }
    public static class BaseBackPressedListener implements OnBackPressedListener {
        private final FragmentActivity activity;

        public BaseBackPressedListener(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        public View doBack() {
            activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return null;
        }
    }
}