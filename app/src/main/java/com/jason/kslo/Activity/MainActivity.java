package com.jason.kslo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.AutoUpdate.UpdateChecker;
import com.jason.kslo.Dialog.ChangelogDialog;
import com.jason.kslo.Dialog.InstallUnknownAppsDialog;
import com.jason.kslo.Intro.SlideActivity;
import com.jason.kslo.R;
import com.jason.kslo.ui.main.SectionsPagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;
    public String updateURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOfApplication = getApplicationContext();

        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

            UpdateChecker.checkForDialog(this);
            com.jason.kslo.App.updateLanguage(this);

        if (!getPackageManager().canRequestPackageInstalls()){
                openDialog();
            }
        else{
            UpdateChecker.checkForDialog(this);
        }
    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    void openDialog(){
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.show(this.getSupportFragmentManager(), "ChangelogDialog");
    }
}