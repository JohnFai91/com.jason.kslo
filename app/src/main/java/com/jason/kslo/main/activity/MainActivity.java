package com.jason.kslo.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.main.fragment.AboutFragment;
import com.jason.kslo.parseContent.defaultParseContent.fragment.DashboardFragment;
import com.jason.kslo.parseContent.defaultParseContent.fragment.SchoolWebsiteFragment;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.LoginFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import static com.jason.kslo.App.updateLanguage;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;
    @SuppressLint("StaticFieldLeak")
    private static View view;
    private static int orange, yellow, primary, red, green, blue;
    private static String future;
    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        updateLanguage(this);
        setContentView(R.layout.activity_main);

        view = findViewById(android.R.id.content);

        viewPager = findViewById(R.id.activityMainViewPager);
        setupViewPager(viewPager);
        bottomNavigationView = findViewById(R.id.bottom_nav_main);

        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        viewPager.setCurrentItem(1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_school_website:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.nav_dashboard:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.nav_login:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.nav_about:
                    viewPager.setCurrentItem(3);
                    break;
            }

            return false;
        });

                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    }
                });

        checkInternet();

        Content content = new Content();
        content.run();

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Network is present and connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public void checkInternet() {

        if (!isNetworkAvailable()) {
            Snackbar.make(getView(), getString(R.string.CheckInternet), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.Go), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .show();
        }
    }

    public boolean CheckActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.hkmakslo.edu.hk").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("CheckInternet", "Error: ", e);
            }
        } else {
            Log.d("CheckInternet", "No network present");
        }
        return false;
    }

    public static View getView() {
        return view;
    }
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
    public static int getRed() {
        return red;
    }
    public static int getOrange() {
        return orange;
    }
    public static int getYellow() {
        return yellow;
    }
    public static int getPrimary() {
        return primary;
    }
    public static int getGreen() {
        return green;
    }
    public static String getFuture() {
        return future;
    }
    public static int getBlue() {
        return blue;
    }

    void openDialog() {
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.setCancelable(false);
        installUnknownAppsDialog.show(this.getSupportFragmentManager(), "ChangelogDialog");
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

    private void setupViewPager(ViewPager2 viewPager)
    {
        FragmentManager fm = Objects.requireNonNull(getSupportFragmentManager());
        ViewPagerAdapter adapter = new ViewPagerAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                default:
                    return new SchoolWebsiteFragment();
                case 1:
                    return new DashboardFragment();
                case 2:
                    return new LoginFragment();
                case 3:
                    return new AboutFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
    private class Content implements Runnable{


        @Override
        public void run() {
            checkUpdate();
            contextOfApplication = getApplicationContext();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                orange = getColor(R.color.orange);
                yellow = getColor(R.color.yellow);
                primary = getColor(R.color.colorPrimary);
                red = getColor(R.color.red);
                green = getColor(R.color.green);
                blue = getColor(R.color.blue);
            }

            future = getString(R.string.Future);
        }
    }
}