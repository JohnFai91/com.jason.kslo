package com.jason.kslo.main.parseContent.loggedInParseContent.fragment;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.activity.LoginActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForBooks;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.SecondLoginParseItem;

import java.util.ArrayList;
import java.util.Map;

import static com.jason.kslo.main.activity.MainActivity.getSecondLoginParseItems;
import static com.jason.kslo.main.activity.MainActivity.parseLm;

public class BorrowedBooksFragment extends Fragment {

    static ParseAdapterForBooks parseAdapterForBooks;
    static ArrayList<SecondLoginParseItem> secondLoginParseItems;
    static int  BookSize;
    RecyclerView borrowedBooksRecyclerView;
    @SuppressLint("StaticFieldLeak")
    static View view;
    static String Username, originalPw, finalUsername;
    SharedPreferences pref;
    SwipeRefreshLayout swipeRefreshLayout;
    int size;
    static Map<String, String> cookies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_borrowed_books, container, false);

        secondLoginParseItems = getSecondLoginParseItems();
        pref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        originalPw = pref.getString("Password","");
        Username = pref.getString("Username","");
        finalUsername = Username.replaceAll("s","");

        Button loginButton = view.findViewById(R.id.LoginButton);
        if (!Username.isEmpty()&&!finalUsername.isEmpty()) {
            loginButton.setVisibility(View.GONE);

            Content content = new Content();
            content.execute();
            swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
            swipeRefreshLayout.setOnRefreshListener(() -> {

                swipeRefreshLayout.setColorSchemeColors(
                        requireActivity().getResources().getColor(android.R.color.holo_blue_dark),
                        requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                        requireActivity().getResources().getColor(android.R.color.holo_green_dark),
                        requireActivity().getResources().getColor(android.R.color.holo_red_dark)
                );

                Content con = new Content();
                con.execute();
            });
        } else {
            loginButton.setOnClickListener(view -> {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            });
            loginButton.setVisibility(View.VISIBLE);
        }
        return view;
    }
    private static boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) view.getContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Network is present and connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void checkInternet() {

        if (!isNetworkAvailable()) {
            Snackbar.make(MainActivity.getView(), view.getContext().getString(R.string.CheckInternet), Snackbar.LENGTH_LONG)
                    .setAction(view.getContext().getString(R.string.Go), view ->
                            view.getContext().startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .setBackgroundTint(MainActivity.getPrimary())
                    .show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private static class Content extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
            swipeRefreshLayout.setRefreshing(true);

            RecyclerView borrowedBooksRecyclerView = view.findViewById(R.id.BorrowedBooksRecyclerView);
            borrowedBooksRecyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutVerticalManager
                    = new LinearLayoutManager(borrowedBooksRecyclerView.getContext(),
                    LinearLayoutManager.VERTICAL, false);
            borrowedBooksRecyclerView.setLayoutManager(layoutVerticalManager);

            parseAdapterForBooks = new ParseAdapterForBooks(secondLoginParseItems, borrowedBooksRecyclerView.getContext());
            borrowedBooksRecyclerView.setAdapter(parseAdapterForBooks);

            secondLoginParseItems.clear();

            checkInternet();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseLm(false);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            parseAdapterForBooks.notifyDataSetChanged();
            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
            swipeRefreshLayout.setRefreshing(false);

        }
    }
    public static Map<String, String> getCookies() {
        return cookies;
    }
}