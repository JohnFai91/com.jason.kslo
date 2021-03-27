package com.jason.kslo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Environment;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatDelegate;
import com.jason.kslo.intro.SlideActivity;
import com.jason.kslo.main.changelog.ChangelogActivity;

import java.io.File;
import java.util.Locale;

@SuppressWarnings("unused")
public class App extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();

        deleteDir();

        SharedPreferences prefs = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        String version = prefs.getString("version","");
        String Theme = prefs.getString("theme","");
        String Intro = prefs.getString("slide","");

        if (TextUtils.isEmpty(Theme)){
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

        if (TextUtils.isEmpty(version)){
            deleteCache(this);
            deleteDir();
            prefs.edit().putString("version", BuildConfig.VERSION_NAME).apply();

        }
        if (!version.equals(BuildConfig.VERSION_NAME)){
            deleteCache(this);

            prefs.edit().putString("version", BuildConfig.VERSION_NAME).apply();
            Intent intent = new Intent(getApplicationContext(), ChangelogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("updated","true");
            startActivity(intent);
        }

            if (Intro.isEmpty()) {

                prefs.edit().putString("version", BuildConfig.VERSION_NAME).apply();

                Intent intent = new Intent(getApplicationContext(), SlideActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
    }

    @SuppressWarnings("deprecation")
    public static void updateLanguage(Context ctx)
    {
        SharedPreferences prefs = ctx.getSharedPreferences("MyPref",MODE_PRIVATE);
        String lang = prefs.getString("lang","");
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            if (lang.equals("zh-HK")){
                cfg.setLocale(Locale.TRADITIONAL_CHINESE);
                Locale.setDefault(Locale.TRADITIONAL_CHINESE);
                ctx.getResources().updateConfiguration(cfg, null);
            } else if (lang.equals("en")){
                cfg.setLocale(Locale.ENGLISH);
                Locale.setDefault(Locale.ENGLISH);
                ctx.getResources().updateConfiguration(cfg, null);
            } else {
                cfg.setLocale(Locale.getDefault());
                ctx.getResources().updateConfiguration(cfg, null);
            }
    }

    @SuppressWarnings({"deprecation"})
    public static void updateLanguage(Context ctx, String lang) {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            if (lang.equals("zh-HK")){
                cfg.setLocale(Locale.TRADITIONAL_CHINESE);
                Locale.setDefault(Locale.TRADITIONAL_CHINESE);
                ctx.getResources().updateConfiguration(cfg, null);
            } else if (lang.equals("en")){
                Locale.setDefault(Locale.ENGLISH);
            cfg.setLocale(Locale.ENGLISH);
                ctx.getResources().updateConfiguration(cfg, null);
        } else {
            cfg.setLocale(Locale.getDefault());
            ctx.getResources().updateConfiguration(cfg, null);
        }
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    public void deleteDir() {
        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                //noinspection ResultOfMethodCallIgnored
                new File(dir, child).delete();
            }
        }
    }
}