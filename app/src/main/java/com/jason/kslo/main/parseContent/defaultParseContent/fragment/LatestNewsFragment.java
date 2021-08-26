package com.jason.kslo.main.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;
import com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter.ParseAdapterForLatestNews;
import com.jason.kslo.main.parseContent.parseItem.SecondParseItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.jason.kslo.App.updateLanguage;

public class LatestNewsFragment extends Fragment {
    ParseAdapterForLatestNews parseAdapterForLatestNews;
    final ArrayList<SecondParseItem> SecondParseItems = new ArrayList<>();

    View view;
    int b = 0;
    int LatestNewsSize = 0;
    ProgressBar progressBar;
    SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(requireContext());
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_latest_news, container, false);


        pullToRefresh = view.findViewById(R.id.schoolWebsiteRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            Content content = new Content();
            content.execute();
            pullToRefresh.setRefreshing(true);
        });

        pullToRefresh.setColorSchemeColors(
                requireActivity().getResources().getColor(android.R.color.holo_blue_dark),
                requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                requireActivity().getResources().getColor(android.R.color.holo_green_dark),
                requireActivity().getResources().getColor(android.R.color.holo_red_dark)
        );

        Content content = new Content();
        content.execute();

        return view;
    }
    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {

            @SuppressWarnings("deprecation")
            @Override
        protected void onPreExecute() {
                pullToRefresh.setRefreshing(true);

                progressBar = view.findViewById(R.id.progress);
                progressBar.setVisibility(View.VISIBLE);

                RecyclerView latestNewsRecyclerView = view.findViewById(R.id.LatestNewsRecycler);

                latestNewsRecyclerView.setHasFixedSize(true);

                LinearLayoutManager layoutVerticalManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                latestNewsRecyclerView.setLayoutManager(layoutVerticalManager);

                parseAdapterForLatestNews = new ParseAdapterForLatestNews(SecondParseItems, getContext());
                latestNewsRecyclerView.setAdapter(parseAdapterForLatestNews);

            super.onPreExecute();

        }


        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... voids) {


            SecondParseItems.clear();
            parseLatestNews();

            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            parseAdapterForLatestNews.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            pullToRefresh.setRefreshing(false);
        }

        @SuppressWarnings({"deprecation", "EmptyMethod"})
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    private  void parseLatestNews(){
        //parse Latest News
        try {
            Document document = Jsoup.connect("https://www.hkmakslo.edu.hk/").get();

            Elements latestNewsData = document.select("div#latestNews.col-xs-12");
            Elements FinalLatestNewsData = latestNewsData.select("h2");

            LatestNewsSize = FinalLatestNewsData.size();
            for (b = 0; b < LatestNewsSize; b++) {

                String latestNewsDate = latestNewsData
                        .select("span.newsDate")
                        .eq(b)
                        .text();

                String titleLatestNews = FinalLatestNewsData
                        .eq(b)
                        .text();

                String detailUrl = latestNewsData
                        .select("a")
                        .eq(b + 1)
                        .attr("href");
                String imgUrl = null;

                SecondParseItems.add(new SecondParseItem(imgUrl, titleLatestNews, latestNewsDate, detailUrl));
                Log.d("Latest News items",   ". Title: " + titleLatestNews + ". Time: " + latestNewsDate + ". Detail Url: " + detailUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}