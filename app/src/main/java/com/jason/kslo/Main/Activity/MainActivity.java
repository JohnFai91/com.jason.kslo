package com.jason.kslo.Main.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.Main.AutoUpdate.UpdateChecker;
import com.jason.kslo.Main.Dialog.ChangeDisplayAmountDialog;
import com.jason.kslo.Main.Dialog.InstallUnknownAppsDialog;
import com.jason.kslo.R;
import com.jason.kslo.ui.main.SectionsPagerAdapter;

import static com.jason.kslo.App.updateLanguage;

public class MainActivity extends AppCompatActivity  implements ChangeDisplayAmountDialog.ChangeDisplayAmountListener {
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateLanguage(this);

        setTheme(R.style.Theme_AppCompat_DayNight_FullScreen);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Content content = new Content();
        content.run();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    void openDialog() {
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.setCancelable(false);
        installUnknownAppsDialog.show(this.getSupportFragmentManager(), "ChangelogDialog");
    }

    public void checkUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                openDialog();
            } else {
                UpdateChecker.checkForDialog(this);
            }
        }
    }
    private class Content implements Runnable{


        @Override
        public void run() {
            checkUpdate();
            contextOfApplication = getApplicationContext();
        }
    }
    @Override
    public void applyTexts(String number) {
        SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);

        pref.edit().putString("intranetAmount",number).apply();

        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);

    }
}