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
import org.jsoup.select.Elements;

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

        Content content = new Content();
        content.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            parseItems.clear();
            progressBar = findViewById(R.id.detailedImageProgress);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseDetailedImages(getIntent().getStringExtra("detailUrl"));
            return null;
        }

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

            Document document = Jsoup
                    .connect(url)
                    .get();
            Log.d("Testing", "parseDetailedImages: " + document);

            Elements items = document.select("ul.list").select("li.item").select("a.link");
            int aSize = items.size();

                for (int i = 0; i < aSize; i++) {
                    Elements finalItem = items.eq(i);
                    String imgUrl = finalItem.select("div.photo_img")
                            .select("img.image")
                            .attr("src");

                    String detailImgUrl = finalItem
                            .attr("href");

                    parseItems.add(new SecondParseItem(imgUrl, detailImgUrl));
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}