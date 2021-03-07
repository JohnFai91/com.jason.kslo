package com.jason.kslo.ParseContent.Activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.ParseContent.ParseAdapter.ParseAdapterForDetailedFile;
import com.jason.kslo.ParseContent.ParseAdapter.ParseAdapterForDetailedLatestNews;
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

public class DetailedLatestNewsActivity extends AppCompatActivity {
    private ParseAdapterForDetailedLatestNews adapter;
    private ParseAdapterForDetailedFile fileAdapter;
    private final ArrayList<SecondParseItem> parseSecondItem = new ArrayList<>();
    private final ArrayList<ParseItem> parseItem = new ArrayList<>();

    String title, text;
    TextView textView, titleTextView;
    String bulletPoints, video, fileUrl;
    Document doc;
    Button openVideo;
    RecyclerView recyclerView, latestNewsRecyclerFile;

    int bulletPointsCount = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_latest_news);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));

        Content content = new Content();
        content.execute();
    }

    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            recyclerView = findViewById(R.id.recyclerViewDetailLatestNews);
            latestNewsRecyclerFile = findViewById(R.id.recyclerViewDetailLatestNewsFile);

            recyclerView.setHasFixedSize(true);
            latestNewsRecyclerFile.setHasFixedSize(true);

            LinearLayoutManager layoutVerticalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutVerticalManager);
            recyclerView.setNestedScrollingEnabled(false);

            LinearLayoutManager layoutHorizontalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            latestNewsRecyclerFile.setLayoutManager(layoutHorizontalManager);
            latestNewsRecyclerFile.setNestedScrollingEnabled(false);

            adapter = new ParseAdapterForDetailedLatestNews(parseSecondItem, getApplicationContext());
            recyclerView.setAdapter(adapter);

            fileAdapter = new ParseAdapterForDetailedFile(parseItem, getApplicationContext());
            latestNewsRecyclerFile.setAdapter(fileAdapter);

            textView = findViewById(R.id.LatestNewsDetailedText);
            titleTextView = findViewById(R.id.LatestNewsDetailedTextTitle);

            openVideo = findViewById(R.id.openVideo);

            video = getIntent().getStringExtra("video");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            parseDetailedLatestNews(getIntent().getStringExtra("detailUrl"));
            parseDetailedFile(getIntent().getStringExtra("detailUrl"));

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            textView.setText(text);
            titleTextView.setText(title);

                if (TextUtils.isEmpty(video)){
                    openVideo.setVisibility(View.GONE);
                }
                else {
                    openVideo.setVisibility(View.VISIBLE);
                    openVideo.setOnClickListener(view -> {
                        Intent appIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(video));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(video));
                        try {
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }
                    });
                }
            adapter.notifyDataSetChanged();
            fileAdapter.notifyDataSetChanged();
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
    private void parseDetailedLatestNews(String url) {
        try {

            doc = Jsoup.connect(url).get();

            Elements data = doc.select("li");
            int size = data.size() - 3;

            try {
                for (int i = 0; i < size; i++) {
                    bulletPoints = doc.select("div.paragraph_block.contents_wrap")
                            .select("ol")
                            .select("li")
                            .eq(i)
                            .text();

                    bulletPointsCount = bulletPointsCount + 1;
                    bulletPoints = bulletPointsCount + ". " + bulletPoints;

                    parseSecondItem.add(new SecondParseItem(bulletPoints, "bulletPoints"));
                }

                title = doc.select("div.contn")
                        .select("div.title")
                        .text();

                text = doc.select("div.paragraph_block.contents_wrap")
                        .select("p")
                        .text();

                video = doc.select("div.paragraph_block.contents_wrap")
                        .select("iframe")
                        .attr("src");

                parseSecondItem.add(new SecondParseItem(title, text, video));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseDetailedFile(String fileUrl) {
        try {
            Document document = Jsoup.connect(fileUrl).get();
            Elements fileElement = document.select("div.file");
            Elements finalFile = fileElement.select("a");
            int finalFileSize = finalFile.size();

            for (int i = 0; i < finalFileSize; i++) {
                fileUrl = finalFile
                        .eq(i)
                        .attr("href");
                String baseUrl = "https://www.hkmakslo.edu.hk";
                fileUrl = baseUrl + fileUrl;

                String fileName = finalFile
                        .eq(i)
                        .text();
                parseItem.add(new ParseItem(fileUrl, fileName));

                Log.d("items", ". title: " + title + ". text: " + text + bulletPointsCount + bulletPoints +
                        " .video: " + video + ". file: " + fileUrl + ". file name: " + fileName);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}