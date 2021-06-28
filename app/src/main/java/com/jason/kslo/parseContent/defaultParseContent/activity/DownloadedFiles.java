package com.jason.kslo.parseContent.defaultParseContent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.SettingsActivity;
import com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDownloadedFiles;
import com.jason.kslo.main.parseContent.parseItem.ParseItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DownloadedFiles extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    static View view;
    @SuppressLint("StaticFieldLeak")
    static SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("StaticFieldLeak")
    static ParseAdapterForDownloadedFiles fileAdapter;

    static RecyclerView recyclerView;
    static File fileDir;

    private static final ArrayList<ParseItem> parseItem = new ArrayList<>();

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
            content.run();
        });

        recyclerView = findViewById(R.id.DownloadedFilesRecyclerView);
        LinearLayoutManager layoutHorizontalManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutHorizontalManager);
        recyclerView.setNestedScrollingEnabled(false);

        fileAdapter = new ParseAdapterForDownloadedFiles(parseItem, DownloadedFiles.this);
        recyclerView.setAdapter(fileAdapter);

        fileDir = new File(getCacheDir(), "/");

        Content content = new Content();
        content.run();
    }
    static class Content implements Runnable {
        @Override
        public void run() {

            parseItem.clear();

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
    public static void ReloadPage() {
        Content content = new Content();
        content.run();
    }
}