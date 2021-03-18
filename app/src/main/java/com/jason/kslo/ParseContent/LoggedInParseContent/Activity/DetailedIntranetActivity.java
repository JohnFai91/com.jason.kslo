package com.jason.kslo.ParseContent.LoggedInParseContent.Activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.LoginParseItem;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseAdapter.ParseAdapterForDetailedIntranetActivity;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseAdapter.ParseAdapterForDetailedIntranetFile;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.SecondLoginParseItem;
import com.jason.kslo.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.jason.kslo.App.updateLanguage;

public class DetailedIntranetActivity extends AppCompatActivity {
    private ParseAdapterForDetailedIntranetActivity adapter;
    private ParseAdapterForDetailedIntranetFile fileAdapter;
    private final ArrayList<LoginParseItem> loginParseItems = new ArrayList<>();
    private final ArrayList<SecondLoginParseItem> secondLoginParseItems = new ArrayList<>();

    String title, text;
    TextView textView, titleTextView;
    Document doc, fileDoc;
    Button openLink;
    RecyclerView recyclerView, IntranetRecyclerFile;
    String detailUrl;
    Map<String,String> Cookies;

    ProgressBar progressBar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_intranet);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));

        String title = getIntent().getStringExtra("title");
        String sender = getIntent().getStringExtra("sender");
        String time = getIntent().getStringExtra("time");

        TextView titleTextView, senderTextView, timeTextView;

        titleTextView = findViewById(R.id.detailedIntranetTitle);
        senderTextView = findViewById(R.id.detailedIntranetAuthor);
        timeTextView = findViewById(R.id.detailedIntranetDate);

        titleTextView.setText(title);
        senderTextView.setText(sender);
        timeTextView.setText(time);

        Content content = new Content();
        content.execute();
    }
    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.detailedIntranetProgressBar);
            progressBar.setVisibility(View.VISIBLE);

            recyclerView = findViewById(R.id.recyclerViewDetailIntranet);
            IntranetRecyclerFile = findViewById(R.id.recyclerViewDetailIntranetFile);

            LinearLayoutManager layoutVerticalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutVerticalManager);

            LinearLayoutManager fileLayoutVerticalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            IntranetRecyclerFile.setLayoutManager(fileLayoutVerticalManager);

            adapter = new ParseAdapterForDetailedIntranetActivity(loginParseItems, getApplicationContext());
            recyclerView.setAdapter(adapter);

            fileAdapter = new ParseAdapterForDetailedIntranetFile(secondLoginParseItems, getApplicationContext());
            IntranetRecyclerFile.setAdapter(fileAdapter);

            detailUrl = getIntent().getStringExtra("detailUrl");
            Cookies = (Map<String, String>) getIntent().getSerializableExtra("cookies");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            parseDetailedIntranet(detailUrl);
            parseDetailedFileIntranet(detailUrl);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            fileAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
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

    private void parseDetailedFileIntranet(String mailId) {
        try{
            fileDoc = Jsoup.connect("https://www.hkmakslo.edu.hk/it-school/php/intra/mailattach.php?mail_id=" + mailId)
                    .cookies(Cookies)
                    .get();
            Elements a = fileDoc.select("a");

            int size = a.size();

            for (int i = 0; i < size; i++) {

                String fileName = a
                        .eq(i)
                        .text();

                String fileUrl = a
                        .eq(i)
                        .attr("href");

                fileUrl = "https://www.hkmakslo.edu.hk/it-school/php/intra/" + fileUrl;

                secondLoginParseItems.add(new SecondLoginParseItem(fileName, fileUrl, Cookies));
                Log.d("parseDetailedFileIntranet", "File: " + fileName + " File Url: " + fileUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseDetailedIntranet(String mailId) {
        try{
        doc = Jsoup.connect("https://www.hkmakslo.edu.hk/it-school/php/intra/mailcontent.php?mail_id=" + mailId)
                .cookies(Cookies)
                .get();

            Elements h1 = doc.select("h1");

            int size = h1.size();

            if (size >= 1){
                for (int i = 0; i < size; i++) {
                    String finalH1 = doc
                            .select("h1")
                            .eq(i)
                            .text();
                    Log.d("h1", "Content: " + finalH1);
                    loginParseItems.add(new LoginParseItem(finalH1));
                }
            }
            Elements finalP = doc.select("p");

            size = finalP.size();

                for (int i = 0; i < size; i++) {
                    String finalStringP = finalP
                            .eq(i)
                            .text();
                    loginParseItems.add(new LoginParseItem(finalStringP));
                    Log.d("parseDetailedIntranet", "Content: " + finalStringP);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}