package com.jason.kslo.ParseContent.Fragment;

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
import com.jason.kslo.ParseContent.ParseAdapter.ParseAdapterForLatestNews;
import com.jason.kslo.ParseContent.ParseAdapter.ParseAdapterForSchoolWebsite;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.ParseContent.ParseItem.SecondParseItem;
import com.jason.kslo.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.jason.kslo.App.updateLanguage;

public class SchoolWebsiteFragment extends Fragment {
    ParseAdapterForSchoolWebsite parseAdapterForSchoolWebsite;
    ParseAdapterForLatestNews parseAdapterForLatestNews;
    ArrayList<ParseItem> parseItems = new ArrayList<>();
    ArrayList<SecondParseItem> SecondParseItems = new ArrayList<>();

    View view;
    int b = 0;
    int LatestNewsSize = 0;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_school_website, container, false);

        Content content = new Content();
        content.execute();

        return view;
    }
    private class Content extends AsyncTask<Void,Void,Void> {

            @Override
        protected void onPreExecute() {

                progressBar = view.findViewById(R.id.progress);
                progressBar.setVisibility(View.VISIBLE);

                RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                RecyclerView latestNewsRecyclerView = view.findViewById(R.id.LatestNewsRecycler);

                latestNewsRecyclerView.setHasFixedSize(true);

                LinearLayoutManager layoutVerticalManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                latestNewsRecyclerView.setLayoutManager(layoutVerticalManager);

                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutHorizontalManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutHorizontalManager);

                parseAdapterForLatestNews = new ParseAdapterForLatestNews(SecondParseItems, getContext());
                latestNewsRecyclerView.setAdapter(parseAdapterForLatestNews);

                parseAdapterForSchoolWebsite = new ParseAdapterForSchoolWebsite(parseItems, getContext());
                recyclerView.setAdapter(parseAdapterForSchoolWebsite);
            super.onPreExecute();

        }


        @Override
        protected Void doInBackground(Void... voids) {
            parseItems.clear();
            for (int pageNo = 1; pageNo < 4; pageNo++) {
                parseImages("https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/albumindex.php?refid=&page=" + pageNo);
            }

            SecondParseItems.clear();
            for (int pageNo = 1; pageNo < 3; pageNo++) {
                parseLatestNews("https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/tpl.php?bulletin=1&component_id=1&mode=published&page=" + pageNo);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            parseAdapterForSchoolWebsite.notifyDataSetChanged();
            parseAdapterForLatestNews.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
        }

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

                String baseUrl = "https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/";
                detailUrl = baseUrl + detailUrl;
                imgUrl = baseUrl + imgUrl;

                parseItems.add(new ParseItem(imgUrl, title, count, detailUrl));
                Log.d("School Website items", "img:" + imgUrl + ". title: " + title + ". Detail Url: " + detailUrl);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}