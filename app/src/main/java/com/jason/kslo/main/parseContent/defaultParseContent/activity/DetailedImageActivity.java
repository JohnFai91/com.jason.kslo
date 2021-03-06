package com.jason.kslo.main.parseContent.defaultParseContent.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;
import com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDetailedWebsite;
import com.jason.kslo.main.parseContent.parseItem.SecondParseItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class DetailedImageActivity extends AppCompatActivity {
    private ParseAdapterForDetailedWebsite adapter;
    private final ArrayList<SecondParseItem> parseItems = new ArrayList<>();
    public static String title;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = getIntent().getStringExtra("title");
        actionBar.setTitle(title);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDetail);

        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapterForDetailedWebsite(parseItems, this);
        recyclerView.setAdapter(adapter);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {

        Content content = new Content();
        content.execute();
        super.onResume();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.detailedImageProgress);
            progressBar.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... voids) {
            parseDetailedImages(getIntent().getStringExtra("detailUrl"));
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void parseDetailedImages(String url){
        try {

            String albumId = url.split("\\?")[1].replaceAll(".*album_id=([^&]+).*", "$1");
            Log.d("Album", "AlbumId: " + albumId);

            int totalPages, ImgCount = Integer.parseInt(getIntent().getStringExtra("imgCount"));
            totalPages = ImgCount / 16;
            if (ImgCount % 16 != 0) {
                totalPages = totalPages + 1;
            }

            for (int page = 1; page < totalPages + 1; page++) {


            Document document = Jsoup
                    .connect("https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/album.php?album_id=" +
                            albumId +"&page=" + page + "&ajax=1")
                    .get();

            int liSize = document.select("li").select("a").size();

                for (int i = 0; i < liSize; i++) {
                    String imgUrl = document.select("li")
                            .select("a")
                            .select("img")
                            .eq(i)
                            .attr("src");

                    String detailImgUrl = document.select("li")
                            .select("a")
                            .eq(i)
                            .attr("href");

                    imgUrl = "https://www.hkmakslo.edu.hk/it-school/php/webcms/public/mainpage/" + imgUrl;
                    detailImgUrl = "https://www.hkmakslo.edu.hk" + detailImgUrl;

                    parseItems.add(new SecondParseItem(imgUrl, detailImgUrl));
                    Log.d(". detailImg ", ". total pages: " + totalPages + ". img: " + imgUrl +
                            " detailed img: " + detailImgUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}