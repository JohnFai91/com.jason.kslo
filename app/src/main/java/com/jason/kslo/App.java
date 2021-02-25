package com.jason.kslo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;

import java.util.Locale;

public class App extends Application
{
    @Override
    public void onCreate()
    {

        com.jason.kslo.App.updateLanguage(this);
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