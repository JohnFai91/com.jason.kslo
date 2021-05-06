package com.jason.kslo.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.main.fragment.AboutFragment;
import com.jason.kslo.parseContent.defaultParseContent.fragment.DashboardFragment;
import com.jason.kslo.parseContent.defaultParseContent.fragment.SchoolWebsiteFragment;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.LoginFragment;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.jason.kslo.App.updateLanguage;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;
    @SuppressLint("StaticFieldLeak")
    private static View view;
    private static int orange, yellow, primary, red, green, blue;
    private static String future;
    private static String MovedTo, cancel, undo;
    static BadgeDrawable badgeDrawable;
    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;
    public static int NewMsgSize = 0;
    static Boolean checkMsg;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_MaterialComponents);
        updateLanguage(this);
        setContentView(R.layout.activity_main);
        checkUpdate();

        view = findViewById(android.R.id.content);
        contextOfApplication = MainActivity.this;

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

        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.nav_login);
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));

                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        bottomNavigationView.getMenu().getItem(position).setChecked(true);
                        if (viewPager.getCurrentItem() == 2) {
                            badgeDrawable.setVisible(false);
                            checkMsg = true;
                        } else {
                            if (checkMsg != null && checkMsg) {
                                CheckNewMsg checkNewMsg = new CheckNewMsg();
                                checkNewMsg.execute();
                                checkMsg = false;
                            }
                        }
                    }
                });


        CheckNewMsg checkNewMsg = new CheckNewMsg();
        checkNewMsg.execute();

        Content content = new Content();
        content.run();

        checkInternet();
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
    public static String getMovedTo() {
        return MovedTo;
    }
    public static String getCancel() {
        return cancel;
    }
    public static String getUndo() {
        return undo;
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                orange = getColor(R.color.orange);
                yellow = getColor(R.color.yellow);
                primary = getColor(R.color.colorPrimary);
                red = getColor(R.color.red);
                green = getColor(R.color.green);
                blue = getColor(R.color.blue);
            }

            undo = getString(R.string.Undo);
            future = getString(R.string.Future);
            cancel = getString(R.string.Cancel);
            MovedTo = getString(R.string.MovedTo,getString(R.string.RecyclingBin));
        }
    }
    static class CheckNewMsg extends AsyncTask<Void, Void, Void> {
        String Username;
        String Password;
        Map<String, String> Cookies;
        SharedPreferences pref;
        final String LOGIN_FORM_URL = "https://www.hkmakslo.edu.hk/it-school//php/login_v5.php3?ran=0.20496586848356468";
        final String LOGIN_ACTION_URL = "https://www.hkmakslo.edu.hk/it-school/php/login_do.php3";
        final String IntranetUrl = "https://www.hkmakslo.edu.hk/it-school/php/intra/index.php3?index.php3?folder=inbox&page=";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            badgeDrawable.setVisible(false);

            pref = getContextOfApplication().getSharedPreferences("MyPref", MODE_PRIVATE);
            Username = pref.getString("Username","");
            Password = pref.getString("Password","");
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(md).update(Password.getBytes());

            byte[] byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte byteDatum : byteData)
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));

            Password = sb.toString();
            Log.d("Md5 Converter", "Converted: " + Password);

            NewMsgSize = 0;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Map<String, String> cookies;
                if (IntranetFragment.getCookies() == null) {

                    Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                            .method(Connection.Method.GET)
                            .execute();

                    Map<String, String> formData = new HashMap<>();
                    formData.put("userloginid",Username);
                    formData.put("password",Password);
                    formData.put("FakePassword", "登入密碼 / Password");
                    formData.put("language","zh-HK");

                    Connection.Response response = Jsoup.connect(LOGIN_ACTION_URL)
                            .data(formData)
                            .cookies(loginForm.cookies())
                            .execute();
                    Cookies = response.cookies();
                    Log.d("ParseNewMsg", "response: " + Cookies);
                } else {
                    Cookies = IntranetFragment.getCookies();
                    Log.d("ParseNewMsg", "getCookies: " + Cookies);
                }

                Document document = Jsoup.connect(IntranetUrl + "0")
                        .cookies(Cookies)
                        .get();

                int size = document
                        .select("td")
                        .select("img")
                        .size();
                for (int i = 0; i < size; i++) {
                    String img = document
                            .select("td")
                            .select("img")
                            .eq(i)
                            .attr("src");
                    if (img.contains("/it-school//images/themes/itschool/icon_read.gif")) {
                        NewMsgSize = NewMsgSize + 1;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("ParseNewMsgSize", "Size: " + NewMsgSize);
            return null;
        }

            @Override
        protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                if (NewMsgSize == 0) {
                    badgeDrawable.setVisible(false);
                } else {
                    badgeDrawable.setNumber(NewMsgSize);
                    badgeDrawable.setVisible(true);
                }
            }
    }

    public static int getNewMsgSize() {
        return NewMsgSize;
    }

    public void checkUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
                installUnknownAppsDialog.setCancelable(false);
                installUnknownAppsDialog.show(getSupportFragmentManager(), "ChangelogDialog");
            } else {
                UpdateChecker.checkForDialog(this);
            }
        }
    }
}