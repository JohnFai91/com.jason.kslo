package com.jason.kslo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatDelegate;
import com.jason.kslo.Intro.SlideActivity;

import java.util.Locale;

public class App extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();

        SharedPreferences prefs = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        String Theme = prefs.getString("theme","");
        String Intro = prefs.getString("slide","");

        if (Intro.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SlideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (Theme.isEmpty()){
            prefs.edit().putString("theme","Follow System")
            .apply();
        }

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
                break;}

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