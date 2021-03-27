package com.jason.kslo.ui.main;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.jason.kslo.*;
import com.jason.kslo.main.fragment.AboutFragment;
import com.jason.kslo.main.fragment.DashboardFragment;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.LoginFragment;
import com.jason.kslo.parseContent.defaultParseContent.fragment.SchoolWebsiteFragment;

import java.util.Objects;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@SuppressWarnings("deprecation")
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Dashboard, R.string.SchoolWebsite, R.string.Login, R.string.About};
    private final Context mContext;

    @SuppressWarnings("deprecation")
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                break;
            case 1:
                fragment = new SchoolWebsiteFragment();
                break;
            case 2:
                fragment = new LoginFragment();
                break;
            case 3:
                fragment = new AboutFragment();
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }
}