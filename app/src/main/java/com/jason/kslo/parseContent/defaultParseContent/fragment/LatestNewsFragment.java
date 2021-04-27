package com.jason.kslo.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
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
import com.jason.kslo.parseContent.defaultParseContent.parseAdapter.ParseAdapterForLatestNews;
import com.jason.kslo.parseContent.parseItem.SecondParseItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pullToRefresh.setColorSchemeColors(
                    Objects.requireNonNull(getActivity()).getColor(android.R.color.holo_blue_dark),
                    getActivity().getColor(android.R.color.holo_orange_dark),
                    getActivity().getColor(android.R.color.holo_green_dark),
                    getActivity().getColor(android.R.color.holo_red_dark)
            );
        }

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
            for (int pageNo = 1; pageNo < 4; pageNo++) {
                parseLatestNews("https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/tpl.php?bulletin=1&component_id=1&mode=published&page=" + pageNo);
            }

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
    private  void parseLatestNews(String LatestNewsUrl){
        //parse Latest News
        try {
            Document document = Jsoup.connect(LatestNewsUrl).get();

            Elements LatestNewsData = document.select("ul.list.lightbox");
            Elements FinalLatestNewsData = LatestNewsData.select("div.content");

            LatestNewsSize = FinalLatestNewsData.size();
            for (b = 0; b < LatestNewsSize; b++) {

                String latestNewsTime = FinalLatestNewsData
                        .select("small.date")
                        .eq(b)
                        .text();

                String titleLatestNews = FinalLatestNewsData
                        .select("span.title")
                        .eq(b)
                        .text();

                String detailUrl = FinalLatestNewsData
                .select("a")
                 .eq(b)
                 .attr("href");

                String imgUrl = document.select("li")
                        .select("div.thumb")
                        .eq(b)
                        .select("img")
                        .attr("src");

                String baseDetailUrl = "https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/";
                detailUrl = baseDetailUrl + detailUrl;

                String baseImgUrl = "https://www.hkmakslo.edu.hk";
                imgUrl = baseImgUrl + imgUrl;

                SecondParseItems.add(new SecondParseItem(imgUrl, titleLatestNews, latestNewsTime, detailUrl));
                Log.d("Latest News items",   ". ImageUrl:" + imgUrl + ". Title: " + titleLatestNews + ". Time: " + latestNewsTime + ". Detail Url: " + detailUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}