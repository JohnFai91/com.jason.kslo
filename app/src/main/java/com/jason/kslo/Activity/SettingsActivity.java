package com.jason.kslo.Activity;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import com.jason.kslo.R;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.jason.kslo.App.updateLanguage;


public class SettingsActivity extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        updateLanguage(this);

        Spinner spinner = findViewById(R.id.SelectThemeSpinner);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String Theme = pref.getString("theme","");
        String Theme1;
        String Theme2;
        String Theme3;
        switch (Theme) {
            case "Day Mode":
                Theme = getString(R.string.LightTheme);
                Theme1 = getString(R.string.SystemDefault);
                Theme2 = getString(R.string.LightTheme);
                Theme3 = getString(R.string.DarkTheme) ;
                break;
            case "Night Mode":
                Theme = getString(R.string.DarkTheme);
                Theme1 = getString(R.string.SystemDefault);
                Theme2 = getString(R.string.LightTheme);
                Theme3 = getString(R.string.DarkTheme) ;
                break;
            default:
                Theme = getString(R.string.SystemDefault);
                Theme1 = getString(R.string.SystemDefault);
                Theme2 = getString(R.string.LightTheme);
                Theme3 = getString(R.string.DarkTheme) ;
                break;
        }


        List<String> categories = new ArrayList<>();
        categories.add(0, Theme);
        categories.add(Theme1);
        categories.add(Theme2);
        categories.add(Theme3);

        // Set Spinner list items in array adapter..
        ArrayAdapter<String> SelectThemeSpinnerItemArrAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories){
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

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getString(R.string.Settings));

        Button change2Eng =  findViewById(R.id.English);
        change2Eng.setOnClickListener(v -> {
            setLocale("en");
            Toast.makeText(this,"You have selected english",Toast.LENGTH_SHORT)
                    .show();

        });

        Button change2Chin =  findViewById(R.id.Chinese);
        change2Chin.setOnClickListener(v -> {
            setLocale("zh");
            Toast.makeText(this,"你選擇了中文",Toast.LENGTH_SHORT)
                    .show();
        });
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
        editor.commit(); // commit changes

        finish();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void setTheme(String theme) {

        Editor editor = pref.edit();

        editor.putString("theme", theme);  // Saving string

        // Save the changes in SharedPreferences
        editor.commit(); // commit changes

        finish();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}