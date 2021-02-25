package com.jason.kslo.Activity;

import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import com.jason.kslo.R;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        com.jason.kslo.App.updateLanguage(this);

        Spinner spinner = findViewById(R.id.SelectThemeSpinner);

        // Get custom Spinner items array.
        String[] SelectThemeSpinnerArr = getResources().getStringArray(R.array.Themes);
        // Set Spinner list items in array adapter..
        ArrayAdapter<String> SelectThemeSpinnerItemArrAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SelectThemeSpinnerArr){

        };
        SelectThemeSpinnerItemArrAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(SelectThemeSpinnerItemArrAdaptor);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    setTheme("SystemDefault");
                }
                else if(position == 2) {
                    setTheme("Light");
                }
                else  if(position == 3){
                    setTheme("Dark");
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
            Toast.makeText(getApplicationContext(),"You have selected english",Toast.LENGTH_SHORT)
            .show();

        });

        Button change2Chin =  findViewById(R.id.Chinese);
        change2Chin.setOnClickListener(v -> {
                setLocale("zh");
            Toast.makeText(getApplicationContext(),"你選擇了中文",Toast.LENGTH_SHORT)
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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Editor editor = pref.edit();

        editor.putString("lang", lang);  // Saving string

        // Save the changes in SharedPreferences
        editor.commit(); // commit changes

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        String locale = (sharedPreferences.getString("lang",""));

        com.jason.kslo.App.updateLanguage(SettingsActivity.this, locale);

        finish();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void setTheme(String theme) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Editor editor = pref.edit();

        editor.putString("theme", theme);  // Saving string

        // Save the changes in SharedPreferences
        editor.commit(); // commit changes

        finish();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}