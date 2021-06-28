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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForBooks;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.SecondLoginParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;

public class BorrowedBooksFragment extends Fragment {

    ParseAdapterForBooks parseAdapterForBooks;
    final ArrayList<SecondLoginParseItem> secondLoginParseItems = new ArrayList<>();
    final String LmUrl = "https://lm.hkmakslo.edu.hk/PrivatePages/_book_table.aspx?";
    int  BookSize;
    RecyclerView borrowedBooksRecyclerView;
    View view;
    String Username, originalPw, finalUsername;
    SharedPreferences pref;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView intranetPageText;
    int size;
    static Map<String, String> cookies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_borrowed_books, container, false);

        Content content = new Content();
        content.execute();

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

        return view;
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

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class Content extends AsyncTask<Void, Void, Void> {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intranetPageText = view.findViewById(R.id.IntranetPageText);
            intranetPageText.setText(getString(R.string.GoTo) + getString(R.string.Intranet));

            swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
            swipeRefreshLayout.setRefreshing(true);

            pref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

            originalPw = pref.getString("Password","");
            Username = pref.getString("Username","");
            finalUsername = Username.replaceAll("s","");

            borrowedBooksRecyclerView = view.findViewById(R.id.BorrowedBooksRecyclerView);
            borrowedBooksRecyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutVerticalManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            borrowedBooksRecyclerView.setLayoutManager(layoutVerticalManager);

            parseAdapterForBooks = new ParseAdapterForBooks(secondLoginParseItems, getContext());
            borrowedBooksRecyclerView.setAdapter(parseAdapterForBooks);

            secondLoginParseItems.clear();

            checkInternet();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseLm();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            parseAdapterForBooks.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void parseLm(){
        try{
            Connection.Response loginForm = Jsoup.connect("https://lm.hkmakslo.edu.hk/Handlers/UserLogin.ashx?&sno=" +
                    finalUsername + "&pass=" + originalPw)
                    .method(Connection.Method.GET)
                    .execute();

            cookies = loginForm.cookies();

            Document doc = Jsoup.connect(LmUrl)
                    .cookies(cookies)
                    .get();

            Log.d("Parse Lm", "Cookies: " + loginForm.cookies());

            Elements bk = doc.select("a");

            BookSize = bk.size();
            Log.d("ParseLM", "Size: " + BookSize);

            for (int i = 0; i < BookSize; i++) {

                String bkTitle = bk
                        .eq(i)
                        .text();

                String bkImg = doc
                        .select("img.book-cover-tbumb")
                        .eq(i)
                        .attr("src");

                bkImg = bkImg.replace("..","");
                bkImg = "https://lm.hkmakslo.edu.hk" + bkImg;

                String bkBorrowedDate = doc
                        .select("td.borrowed-date")
                        .eq(i)
                        .text();

                String bkReturnDate = doc
                        .select("td.return-date")
                        .eq(i)
                        .text();

                String countReturn = doc.select("span.due")
                        .eq(i)
                        .text();

                String detailUrl = doc
                        .select("span.row")
                        .select("a.book-name")
                        .eq(i)
                        .attr("href");
                String baseDetailUrl = "https://lm.hkmakslo.edu.hk/Pages/";
                detailUrl = baseDetailUrl + detailUrl;

                countReturn = countReturn.replace("天後到期",getString(R.string.DaysToDueDate));
                bkReturnDate = bkReturnDate + " (" +countReturn + ")";

                secondLoginParseItems.add(new SecondLoginParseItem(bkTitle,bkImg,bkBorrowedDate,bkReturnDate,detailUrl,loginForm.cookies()));
                Log.d("Parse Lm", "Bk: " + " title: " + bkTitle + " imgUrl: " + bkImg +
                        " borrowed date: " + bkBorrowedDate + " return date: " + bkReturnDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Map<String, String> getCookies() {
        return cookies;
    }
}