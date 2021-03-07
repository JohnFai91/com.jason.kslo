package com.jason.kslo.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.AutoUpdate.UpdateChecker;
import com.jason.kslo.Dialog.InstallUnknownAppsDialog;
import com.jason.kslo.R;
import com.jason.kslo.ui.main.SectionsPagerAdapter;

import static com.jason.kslo.App.updateLanguage;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateLanguage(this);
        contextOfApplication = getApplicationContext();

        setTheme(R.style.Theme_AppCompat_DayNight_FullScreen);
        setContentView(R.layout.activity_main);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    void openDialog() {
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.setCancelable(false);
        installUnknownAppsDialog.show(this.getSupportFragmentManager(), "ChangelogDialog");
    }

    private class Content extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            UpdateChecker.checkForDialog(getApplicationContext());
            checkUpdate();
            return null;
        }
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
}