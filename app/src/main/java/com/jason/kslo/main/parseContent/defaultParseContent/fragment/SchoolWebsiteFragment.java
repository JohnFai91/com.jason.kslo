package com.jason.kslo.main.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter.ParseAdapterForSchoolWebsite;
import com.jason.kslo.main.parseContent.parseItem.SecondParseItem;
import com.jason.kslo.main.parseContent.parseItem.ThirdParseItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.jason.kslo.App.updateLanguage;

public class SchoolWebsiteFragment extends Fragment {
    ParseAdapterForLatestNews parseAdapterForLatestNews;
    final ArrayList<SecondParseItem> SecondParseItems = new ArrayList<>();

    View view;
    int b = 0;
    int LatestNewsSize = 0;
    ProgressBar LatestNewsProgressBar, GalleryProgressBar;
    SwipeRefreshLayout pullToRefresh;

    RecyclerView GalleryRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    final ArrayList<ThirdParseItem> parseItems = new ArrayList<>();
    ParseAdapterForSchoolWebsite parseAdapterForSchoolWebsite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(requireContext());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_school_website, container, false);


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

        RecyclerView latestNewsRecyclerView = view.findViewById(R.id.LatestNewsRecycler);

        latestNewsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutVerticalManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        latestNewsRecyclerView.setLayoutManager(layoutVerticalManager);

        parseAdapterForLatestNews = new ParseAdapterForLatestNews(SecondParseItems, getContext());
        latestNewsRecyclerView.setAdapter(parseAdapterForLatestNews);

        LatestNewsProgressBar = view.findViewById(R.id.LatestNewsProgressBar);
        GalleryProgressBar = view.findViewById(R.id.GalleryProgressBar);

        GalleryRecyclerView = view.findViewById(R.id.GalleryRecyclerView);

        parseAdapterForSchoolWebsite = new ParseAdapterForSchoolWebsite(parseItems, requireActivity());
        GalleryRecyclerView.setAdapter(parseAdapterForSchoolWebsite);

        LinearLayoutManager layoutHorizontalManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GalleryRecyclerView.setLayoutManager(layoutHorizontalManager);

        Content content = new Content();
        content.execute();

        return view;
    }
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {

            @Override
        protected void onPreExecute() {
                super.onPreExecute();

                pullToRefresh.setRefreshing(true);
                LatestNewsProgressBar.setVisibility(View.VISIBLE);
                GalleryProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseLatestNews();
            parseImages();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            parseAdapterForLatestNews.notifyDataSetChanged();
            parseAdapterForSchoolWebsite.notifyDataSetChanged();

            LatestNewsProgressBar.setVisibility(View.GONE);
            GalleryProgressBar.setVisibility(View.GONE);
            pullToRefresh.setRefreshing(false);
        }
    }
    private  void parseLatestNews(){
        SecondParseItems.clear();
        try{
        //parse Latest News
        String doc = Jsoup.connect("https://www.hkmakslo.edu.hk/latest-new/").get().toString();
        String ajax_nonceStr = doc.substring(doc.indexOf("/* <![CDATA[ */\n" +
                "var ECLASS_LATEST_NEWS_API = ")).replace("/* <![CDATA[ */\n" +
                "var ECLASS_LATEST_NEWS_API = ","").replace(doc.substring(doc.indexOf(";\n" +
                "/* ]]> */")), "");
        String ajax_nonce = ajax_nonceStr.substring(ajax_nonceStr.indexOf(",\"_ajax_nonce\":\""));
        ajax_nonce = ajax_nonce.replace(",\"_ajax_nonce\":\"","");
        ajax_nonce = ajax_nonce.replace(ajax_nonce.substring(ajax_nonce.indexOf("\",\"page_url\":\"")),"");
        ajax_nonce = ajax_nonce.replace("\",\"page_url\":\"","");

        String latestNews = Jsoup.connect("https://www.hkmakslo.edu.hk/wp-admin/admin-ajax.php?action=eclass_latest_news_api&num_of_record=&_ajax_nonce=" +
                        "action=eclass_latest_news_api&num_of_record=&_ajax_nonce=" + ajax_nonce)
                .ignoreContentType(true)
                .execute()
                .body();

            JSONObject obj = new JSONObject(latestNews).getJSONObject("Announcements");
            JSONArray jaAnnouncement = obj.getJSONArray("Announcement");
            for (int i = 0; i < jaAnnouncement.length(); i++) {
                JSONObject finalJaAnnouncement = new JSONObject(jaAnnouncement.get(i).toString());

                String detailUrl = "https://www.hkmakslo.edu.hk/latest-new/?id=" + finalJaAnnouncement.getString("AnnouncementID");

                String titleLatestNews = finalJaAnnouncement.getString("Title");

                String latestNewsDate = finalJaAnnouncement.getString("AnnouncementDate");

                String sender = finalJaAnnouncement.getString("PosterNameCH") +
                        " " + finalJaAnnouncement.getString("PosterNameEN");

                String desc = finalJaAnnouncement.toString();

            SecondParseItems.add(new SecondParseItem(sender, titleLatestNews, latestNewsDate, desc, detailUrl));
            Log.d("Latest News items",   ". Title: " + titleLatestNews + ". Time: " + latestNewsDate
                    + ". Detail Url: " + detailUrl + ". Sender: " + sender);

                requireActivity().runOnUiThread(() -> parseAdapterForLatestNews.notifyDataSetChanged());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseImages() {
        parseItems.clear();
        try {
            Document doc = Jsoup.connect("https://www.hkmakslo.edu.hk/school-life_c/gallery/").get();

            Elements data = doc.select("div.huge-it-list.album-list").select("ul.list").select("li.item");
            int size = data.size();
            for (int i = 0; i < size; i++) {
                Elements imgDoc = data.eq(i);

                String title = imgDoc
                        .select("p.album_title")
                        .text();

                String detailUrl = imgDoc
                        .select("a.itemLink")
                        .attr("href");

                String imgUrl = imgDoc
                        .select("img.image")
                        .attr("src");
                imgUrl = imgUrl.replace(".jpg","-300x200.jpg");

                parseItems.add(new ThirdParseItem(imgUrl, title, detailUrl));
                Log.d("School Website items", "img:" + imgUrl + ". title: " + title + ". Detail Url: " + detailUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}