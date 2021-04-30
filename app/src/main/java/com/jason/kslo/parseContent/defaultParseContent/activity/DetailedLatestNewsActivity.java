package com.jason.kslo.parseContent.defaultParseContent.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDetailedFile;
import com.jason.kslo.parseContent.parseItem.ParseItem;
import com.jason.kslo.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.jason.kslo.App.updateLanguage;

public class DetailedLatestNewsActivity extends AppCompatActivity {
    private ParseAdapterForDetailedFile fileAdapter;
    private final ArrayList<ParseItem> parseItem = new ArrayList<>();

    String title, text, fileName, date;
    TextView textView, titleTextView, dateTextView;
    @SuppressWarnings("unused")
    String bulletPoints, video, fileUrl;
    Document doc;
    Button openVideo;
    RecyclerView latestNewsRecyclerFile;

    ProgressBar progressBar;

    @SuppressLint("RestrictedApi")
    @Override
    @SuppressWarnings({"ConstantConditions", "deprecation"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_latest_news);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));

        Content content = new Content();
        content.execute();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            latestNewsRecyclerFile = findViewById(R.id.recyclerViewDetailLatestNewsFile);

            latestNewsRecyclerFile.getRecycledViewPool().clear();

            latestNewsRecyclerFile.setHasFixedSize(true);

            LinearLayoutManager layoutHorizontalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            latestNewsRecyclerFile.setLayoutManager(layoutHorizontalManager);
            latestNewsRecyclerFile.setNestedScrollingEnabled(false);

            fileAdapter = new ParseAdapterForDetailedFile(parseItem, getApplicationContext());
            latestNewsRecyclerFile.setAdapter(fileAdapter);

            textView = findViewById(R.id.LatestNewsDetailedText);
            titleTextView = findViewById(R.id.LatestNewsDetailedTextTitle);
            dateTextView = findViewById(R.id.latestNewsDetailedDate);

            openVideo = findViewById(R.id.openVideo);

            video = getIntent().getStringExtra("video");

            progressBar = findViewById(R.id.detailedLatestNewsProgressBar);
            progressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... voids) {

            parseDetailedLatestNews(getIntent().getStringExtra("detailUrl"));
            parseDetailedFile(getIntent().getStringExtra("detailUrl"));

            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            text = text.replaceAll(">",">" + "\n");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(text,0));
            } else {
                textView.setText(Html.fromHtml(text));
            }
            titleTextView.setText(getIntent().getStringExtra("title"));
            dateTextView.setText(date);

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
                            finish();
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            finish();
                            startActivity(webIntent);
                        }
                    });

                }
            fileAdapter.notifyDataSetChanged();
            Log.d("items", ". title: " + title + ". text: " + text + " .video: " + video + ". file: " + fileUrl + ". file name: " + fileName);
            progressBar.setVisibility(View.GONE);
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

            try {
                date = doc.select("div.contn")
                        .select("div.title")
                        .select("span.date")
                        .text();

                text = doc.select("div.paragraph_block.contents_wrap")
                        .html();

                video = doc.select("div.paragraph_block.contents_wrap")
                        .select("iframe")
                        .attr("src");


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

                fileName = finalFile
                        .eq(i)
                        .text();
                parseItem.add(new ParseItem(fileUrl, fileName));

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}