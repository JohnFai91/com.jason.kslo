package com.jason.kslo.main.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.main.dialog.ChangeDisplayAmountDialog;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.R;
import com.jason.kslo.ui.main.SectionsPagerAdapter;

import static com.jason.kslo.App.updateLanguage;

public class MainActivity extends AppCompatActivity  implements ChangeDisplayAmountDialog.ChangeDisplayAmountListener {
    public static Context contextOfApplication;
    @SuppressLint("StaticFieldLeak")
    private static View view;
    private static int orange, yellow, primary, red, green, blue;
    private static String future;
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

    public static View getView() {
        return view;
    }
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
    public static int getRed() {
        return red;
    }
    public static int getOrange() {
        return orange;
    }
    public static int getYellow() {
        return yellow;
    }
    public static int getPrimary() {
        return primary;
    }
    public static int getGreen() {
        return green;
    }
    public static String getFuture() {
        return future;
    }
    public static int getBlue() {
        return blue;
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

            view = findViewById(android.R.id.content);

            orange = getColor(R.color.orange);
            yellow = getColor(R.color.yellow);
            primary = getColor(R.color.colorPrimary);
            red = getColor(R.color.red);
            green = getColor(R.color.green);
            blue = getColor(R.color.blue);

            future = getString(R.string.Future);
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