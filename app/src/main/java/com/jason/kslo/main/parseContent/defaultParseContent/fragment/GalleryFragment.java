package com.jason.kslo.main.parseContent.defaultParseContent.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;
import com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter.ParseAdapterForSchoolWebsite;
import com.jason.kslo.main.parseContent.parseItem.ParseItem;

import java.util.ArrayList;
/**
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.main.activity.MainActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
 **/
public class GalleryFragment extends Fragment {
    View view;
    int pageNo = 1;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    final ArrayList<ParseItem> parseItems = new ArrayList<>();
    ParseAdapterForSchoolWebsite parseAdapterForSchoolWebsite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
    /*
        swipeRefreshLayout = view.findViewById(R.id.GallerySwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageNo = 1;
            parseItems.clear();
            Content content = new Content();
            content.execute();
        });
        swipeRefreshLayout.setColorSchemeColors(
                requireActivity().getResources().getColor(android.R.color.holo_blue_dark),
                requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                requireActivity().getResources().getColor(android.R.color.holo_green_dark),
                requireActivity().getResources().getColor(android.R.color.holo_red_dark)
        );

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1)) {
                    pageNo = pageNo + 1;
                    Content content = new Content();
                    content.execute();
                }
            }
        });

        parseItems.clear();


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutHorizontalManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutHorizontalManager);

        parseAdapterForSchoolWebsite = new ParseAdapterForSchoolWebsite(parseItems, getContext());
        recyclerView.setAdapter(parseAdapterForSchoolWebsite);

        Content content = new Content();
        content.execute();
    */
        return view;
    }
    /*
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            checkInternet();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            parseImages("https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/albumindex.php?refid=&page=" + pageNo);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            parseAdapterForSchoolWebsite.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

    }
    public void parseImages(String imageUrl) {
        try {
            Document doc = Jsoup.connect(imageUrl).get();

            Elements data = doc.select("div.col_wrap");
            Elements finalData = data.select("div.wrap");

            int size = data.size();
            for (int i = 0; i < size; i++) {
                String imgUrl = data
                        .select("div.image")
                        .select("img")
                        .eq(i)
                        .attr("src");

                String title = data
                        .select("div.content")
                        .select("span.title")
                        .eq(i)
                        .text();

                String count = data
                        .select("div.content")
                        .select("span.count")
                        .eq(i)
                        .text();

                String detailUrl = finalData
                        .select("a")
                        .eq(i)
                        .attr("href");

                String baseUrl = "https://www.hkmakslo.edu.hk";
                detailUrl = baseUrl + detailUrl;
                imgUrl = baseUrl + imgUrl;

                parseItems.add(new ParseItem(imgUrl, title, count, detailUrl));
                Log.d("School Website items", "img:" + imgUrl + ". title: " + title + ". Detail Url: " + detailUrl);

            }
        } catch (IOException e) {
            e.printStackTrace();
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
    **/
}