package com.jason.kslo.main.parseContent.loggedInParseContent.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.R;

import java.util.Objects;

import static com.jason.kslo.App.updateLanguage;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextInputEditText username, password, eClassPassword;
    String UsernameStr, PasswordStr, eClassPasswordStr;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        updateLanguage(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
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
            eClassPassword = findViewById(R.id.eClass_password);

            username.setText(pref.getString("Username",""));
            password.setText(pref.getString("Password",""));
            eClassPassword.setText(pref.getString("eClassPassword",""));

            username.requestFocus();

            eClassPassword.setOnKeyListener((view, i, keyEvent) -> {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    login.performClick();
                }
                return false;
            });

            login.setOnClickListener(view -> {

                UsernameStr = Objects.requireNonNull(username.getText()).toString();
                PasswordStr = Objects.requireNonNull(password.getText()).toString();
                eClassPasswordStr = Objects.requireNonNull(eClassPassword.getText()).toString();

                pref.edit().putString("Username", UsernameStr).apply();
                pref.edit().putString("Password", PasswordStr).apply();
                pref.edit().putString("eClassPassword", eClassPasswordStr).apply();
                
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
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