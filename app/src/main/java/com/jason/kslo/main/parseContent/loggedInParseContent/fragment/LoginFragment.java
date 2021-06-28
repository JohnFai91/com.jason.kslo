package com.jason.kslo.main.parseContent.loggedInParseContent.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.jason.kslo.R;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LoginFragment extends Fragment {
    View view;
    RelativeLayout viewPagerSwitcher;
    ViewPager2 viewPager;
    private static int size;
    private static String countReturn;
    String originalPw,finalUsername;
    int prevMenuItem;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireContext().getTheme().applyStyle(R.style.AppTheme_MaterialComponents,
                true);
        view = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment

        viewPager = view.findViewById(R.id.LoginViewPager);
        viewPager = view.findViewById(R.id.LoginViewPager);
        setupViewPager(viewPager);

        Content content = new Content();
        content.execute();

        return view;
    }

    private void setupViewPager(ViewPager2 viewPager)
    {
        FragmentManager fm = getChildFragmentManager();
        loginViewPagerAdapter adapter = new loginViewPagerAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SharedPreferences pref = requireActivity()
                    .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            originalPw = pref.getString("Password","");
            finalUsername = pref.getString("Username","");
            finalUsername = finalUsername.replaceAll("s","");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection.Response loginForm = Jsoup.connect("https://lm.hkmakslo.edu.hk/Handlers/UserLogin.ashx?&sno=" +
                        finalUsername + "&pass=" + originalPw)
                        .method(Connection.Method.GET)
                        .timeout(3 * 1000)
                        .execute();
                Log.d("LoginFragment", "Username: " + finalUsername + " Pw: " + originalPw);

                Document doc = Jsoup.connect("https://lm.hkmakslo.edu.hk/PrivatePages/_book_table.aspx?")
                        .cookies(loginForm.cookies())
                        .get();

                Elements bk = doc.select("a");

                size = bk.size();

                int prevCount = 0;
                for (int i = 0; i < size; i++) {
                    countReturn = doc.select("span.due")
                            .eq(i)
                            .text();
                    countReturn = countReturn.replace(" 天後到期","");

                    Log.d("ParseLM", "Size: " + size + countReturn);

                    if (!countReturn.equals("")) {
                        int closestDueDate = Integer.parseInt(countReturn);
                        
                        if (closestDueDate < prevCount) {
                            prevCount = closestDueDate;
                            countReturn = String.valueOf(closestDueDate);
                        } else if (prevCount == 0) {
                            prevCount = closestDueDate;
                            countReturn = String.valueOf(closestDueDate);
                        }
                    }
                }
                countReturn = " (" + countReturn + getString(R.string.DaysToDueDate) + ")";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            viewPager = view.findViewById(R.id.LoginViewPager);

            viewPagerSwitcher = view.findViewById(R.id.LoginViewPagerSwitcher);

            final String[] Title = {getString(R.string.Intranet) };

            viewPager.setCurrentItem(0);

            viewPagerSwitcher.setOnClickListener(view -> {
                if (Title[0].contains(getString(R.string.BorrowedBooks))) {
                    Title[0] = getString(R.string.Intranet);

                    viewPager.setCurrentItem(0);
                } else if (Title[0].contains(getString(R.string.Intranet))) {
                    Title[0] = getString(R.string.BorrowedBooks);

                    viewPager.setCurrentItem(1);
                }
            });
        }
    }

    public static int getSize() {
        return size;
    }

    public static String getCountReturn() {
        return countReturn;
    }
}