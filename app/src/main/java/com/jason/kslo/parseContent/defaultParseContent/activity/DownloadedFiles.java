package com.jason.kslo.parseContent.defaultParseContent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.SettingsActivity;
import com.jason.kslo.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDownloadedFiles;
import com.jason.kslo.parseContent.parseItem.ParseItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DownloadedFiles extends AppCompatActivity {
    private ParseAdapterForDownloadedFiles fileAdapter;
    private final ArrayList<ParseItem> parseItem = new ArrayList<>();
    static RecyclerView recyclerView;
    ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    static View view;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_files);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.DownloadedFiles);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        view = findViewById(android.R.id.content);

        Content content = new Content();
        content.execute();
    }
    @SuppressLint("StaticFieldLeak")
    class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout = findViewById(R.id.DownloadedFileSwipeRefresh);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(android.R.color.holo_blue_dark),
                    getResources().getColor(android.R.color.holo_orange_dark),
                    getResources().getColor(android.R.color.holo_green_dark),
                    getResources().getColor(android.R.color.holo_red_dark)
            );
            swipeRefreshLayout.setOnRefreshListener(() -> {
                Content content = new Content();
                content.execute();
            });

            parseItem.clear();

            recyclerView = findViewById(R.id.DownloadedFilesRecyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutHorizontalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutHorizontalManager);
            recyclerView.setNestedScrollingEnabled(false);

            fileAdapter = new ParseAdapterForDownloadedFiles(parseItem, DownloadedFiles.this);
            recyclerView.setAdapter(fileAdapter);

            progressBar = findViewById(R.id.DownloadedFilesProgressBar);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            File fileDir = new File(getCacheDir(), "/");
            for (File f : Objects.requireNonNull(fileDir.listFiles())) {
                if (f.isFile()) {
                    String fileName = f.getName();

                    if (!fileName.contains("Hw.ics") && !fileName.contains("-pdfview.pdf")) {
                        long fileSize = f.length();
                        Date date = new Date(f.lastModified());
                        String fileDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date);

                        parseItem.add(new ParseItem(fileName, fileSize, fileDate));
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            fileAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_bar_menu_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static View getView() {
        return view;
    }
}