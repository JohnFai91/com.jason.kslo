package com.jason.kslo.ParseContent.LoggedInParseContent.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.jason.kslo.Main.Activity.MainActivity;
import com.jason.kslo.R;

import java.util.Objects;

import static com.jason.kslo.App.updateLanguage;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextInputEditText username, password;
    String Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLanguage(this);

        super.onCreate(savedInstanceState);
        setTheme(R.style.Widget_AppCompat_ActionBar);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.Login);

        Content content = new Content();
        content.run();
    }

    private class Content implements Runnable{
        @Override
        public void run() {
            SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);

            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            login = findViewById(R.id.loginButton);

            username.setText(pref.getString("Username",""));
            password.setText(pref.getString("Password",""));

            login.setOnClickListener(view -> {

                Username = Objects.requireNonNull(username.getText()).toString();
                Password = Objects.requireNonNull(password.getText()).toString();
                Log.d("Login", "Username: " + Username + " Password: " + Password);

                pref.edit().putString("Username", Username).apply();
                pref.edit().putString("Password",Password).apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}