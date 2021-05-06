package com.jason.kslo.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;

public class SchoolWebsiteFragment extends Fragment {
    View view;
    Button viewPagerSwitcher;
    ViewPager2 viewPager;
    private static int size;
    private static String countReturn;
    TextView title, text;
    String originalPw,finalUsername;
    int prevMenuItem;
    String[] Title;
    String[] Text;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireContext().getTheme().applyStyle(R.style.Theme_MaterialComponents_DayNight,
                true);
        view = inflater.inflate(R.layout.fragment_school_website, container, false);
        // Inflate the layout for this fragment

        viewPager = view.findViewById(R.id.SchoolWebsiteViewPager);
        setupViewPager(viewPager);

        Content content = new Content();
        content.execute();

        return view;
    }

    private void setupViewPager(ViewPager2 viewPager)
    {
        FragmentManager fm = getChildFragmentManager();
        schoolWebsiteViewPagerAdapter adapter = new schoolWebsiteViewPagerAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Text = new String[]{getString(R.string.GoTo) + getString(R.string.Album)};
            Title = new String[]{getString(R.string.SchoolWebsite)};;
            checkInternet();

            SharedPreferences pref = requireActivity()
                    .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            originalPw = pref.getString("Password","");
            finalUsername = pref.getString("Username","");
            finalUsername = finalUsername.replaceAll("s","");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            viewPager = view.findViewById(R.id.SchoolWebsiteViewPager);

            title = view.findViewById(R.id.SchoolWebsiteTitle);
            text = view.findViewById(R.id.SchoolWebsiteText);
            viewPagerSwitcher = view.findViewById(R.id.SchoolWebsitePagerSwitcher);

            title.setText(Title[0]);

            text.setText(Text[0]);

            viewPager.setCurrentItem(0);


            viewPagerSwitcher.setOnClickListener(view -> {
                if (Title[0].contains(getString(R.string.SchoolWebsite))) {
                    Title[0] = getString(R.string.Album);
                    Text[0] = getString(R.string.ReturnTo, getString(R.string.SchoolWebsite));

                    title.setText(Title[0]);
                    text.setText(Text[0]);

                    viewPager.setCurrentItem(1);
                } else if (Title[0].contains(getString(R.string.Album))) {
                    Title[0] = getString(R.string.SchoolWebsite);
                    Text[0] = getString(R.string.ReturnTo, getString(R.string.Album));

                    title.setText(Title[0]);
                    text.setText(Text[0]);

                    viewPager.setCurrentItem(0);
                }
            });
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) requireActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Network is present and connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public void checkInternet() {

        if (!isNetworkAvailable()) {
            Snackbar.make(MainActivity.getView(), getString(R.string.CheckInternet), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.Go), view ->
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .setBackgroundTint(MainActivity.getPrimary())
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        viewPagerSwitcher.setOnClickListener(view -> {
            if (Title[0].contains(getString(R.string.SchoolWebsite))) {
                Title[0] = getString(R.string.Album);
                Text[0] = getString(R.string.ReturnTo, getString(R.string.SchoolWebsite));

                title.setText(Title[0]);
                text.setText(Text[0]);

                viewPager.setCurrentItem(1);
            } else if (Title[0].contains(getString(R.string.Album))) {
                Title[0] = getString(R.string.SchoolWebsite);
                Text[0] = getString(R.string.ReturnTo, getString(R.string.Album));

                title.setText(Title[0]);
                text.setText(Text[0]);

                viewPager.setCurrentItem(0);
            }
        });
    }

    public static int getSize() {
        return size;
    }

    public static String getCountReturn() {
        return countReturn;
    }
}