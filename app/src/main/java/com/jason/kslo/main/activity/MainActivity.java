package com.jason.kslo.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.main.fragment.AboutFragment;
import com.jason.kslo.main.parseContent.defaultParseContent.fragment.DashboardFragment;
import com.jason.kslo.main.parseContent.defaultParseContent.fragment.LatestNewsFragment;
import com.jason.kslo.main.parseContent.loggedInParseContent.fragment.BorrowedBooksFragment;

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
        badgeDrawable.setVisible(false);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
        Content content = new Content();
        content.run();
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
                    return new LatestNewsFragment();
                case 1:
                    return new DashboardFragment();
                case 2:
                    return new BorrowedBooksFragment();
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