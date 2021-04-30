package com.jason.kslo.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import com.jason.kslo.R;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.parseContent.loggedInParseContent.activity.LoginActivity;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;

import java.util.ArrayList;
import java.util.List;

import static com.jason.kslo.App.updateLanguage;


public class SettingsActivity extends AppCompatActivity {
    String Theme, Theme1, Theme2, Theme3;
    Spinner spinner;
    Button change2Eng, change2Chin;
    Button reLogin;
    SwitchCompat downloadAgainQuery;

    SharedPreferences pref;
    @SuppressLint("RestrictedApi")
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateLanguage(this);
        setContentView(R.layout.activity_settings);

        spinner = findViewById(R.id.SelectThemeSpinner);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Theme = pref.getString("theme","");
        String selectedClass = pref.getString("Class","3C");

        Spinner classesSpinner = findViewById(R.id.SettingsChooseClass);
        String[] classes = {
                getString(R.string.Class) + " (" + selectedClass + ")","2D", "3C"
        };
        ArrayAdapter<? extends String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classesSpinner.setAdapter(adapter);

        classesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                 switch (position) {
                     case 1:
                         pref.edit().putString("Class","2D").apply();
                         finish();
                         startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                         break;
                     case 2:
                         pref.edit().putString("Class","3C").apply();
                         finish();
                         startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });

            downloadAgainQuery = findViewById(R.id.downloadQuerySwitcher);

        change2Eng =  findViewById(R.id.English);
        change2Chin =  findViewById(R.id.Chinese);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getString(R.string.Settings));

        reLogin = findViewById(R.id.reLoginButton);

        Content content = new Content();
        content.run();
    }
    class Content implements Runnable{
        @Override
        public void run() {

            switch (Theme) {
                case "Day Mode":
                    Theme = getString(R.string.LightTheme);
                    Theme1 = getString(R.string.SystemDefault);
                    Theme2 = getString(R.string.LightTheme);
                    Theme3 = getString(R.string.DarkTheme);
                    break;
                case "Night Mode":
                    Theme = getString(R.string.DarkTheme);
                    Theme1 = getString(R.string.SystemDefault);
                    Theme2 = getString(R.string.LightTheme);
                    Theme3 = getString(R.string.DarkTheme);
                    break;
                default:
                    Theme = getString(R.string.SystemDefault);
                    Theme1 = getString(R.string.SystemDefault);
                    Theme2 = getString(R.string.LightTheme);
                    Theme3 = getString(R.string.DarkTheme);
                    break;
            }

            downloadAgainQuery.setChecked(pref.getString("DownloadAgainQuery", "true").equals("true"));
            downloadAgainQuery.setOnClickListener(view -> {
                if (downloadAgainQuery.isChecked()) {
                    pref.edit().putString("DownloadAgainQuery","true").apply();
                } else if (!downloadAgainQuery.isChecked()) {
                    pref.edit().putString("DownloadAgainQuery","false").apply();
                }
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                finish();
                startActivity(intent);
            });

            List<String> categories = new ArrayList<>();
            categories.add(0, Theme);
            categories.add(Theme1);
            categories.add(Theme2);
            categories.add(Theme3);

            // Set Spinner list items in array adapter..
            ArrayAdapter<String> SelectThemeSpinnerItemArrAdaptor = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, categories){
            };

            SelectThemeSpinnerItemArrAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(SelectThemeSpinnerItemArrAdaptor);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 1) {
                        setTheme("Follow System");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
                    else if(position == 2) {
                        setTheme("Day Mode");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    else  if(position == 3){
                        setTheme("Night Mode");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            change2Eng.setOnClickListener(v -> setLocale("en"));


            change2Chin.setOnClickListener(v -> setLocale("zh-HK"));

            reLogin.setOnClickListener(view -> {
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);

                startActivity(intent);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String lang){
        Editor editor = pref.edit();

        editor.putString("lang", lang);  // Saving string

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        finish();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
    public void setTheme(String theme) {

        Editor editor = pref.edit();

        editor.putString("theme", theme);  // Saving string

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        finish();
        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
        finish();
        startActivity(intent);
    }
}