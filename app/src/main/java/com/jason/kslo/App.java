package com.jason.kslo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import com.jason.kslo.Intro.SlideActivity;

import java.util.Locale;

public class App extends Application {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate()
    {
        SharedPreferences prefs = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        String Theme = prefs.getString("theme","");
        String FirstLaunch = prefs.getString("First Launch","");

            switch (Theme) {
                case "Follow System":
                    setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case "Day Mode":
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case "Night Mode":
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                }

            com.jason.kslo.App.updateLanguage(this);

        if (FirstLaunch.isEmpty()) {
            Intent intent = new Intent(this, SlideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

            super.onCreate();
    }

    public static void updateLanguage(Context ctx)
    {
        SharedPreferences prefs = ctx.getSharedPreferences("MyPref",MODE_PRIVATE);
        String lang = prefs.getString("lang","");
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);

        else
            cfg.locale = Locale.getDefault();
        ctx.getResources().updateConfiguration(cfg, null);
    }

    public static void updateLanguage(Context ctx, String lang) {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();
        ctx.getResources().updateConfiguration(cfg, null);
    }
}