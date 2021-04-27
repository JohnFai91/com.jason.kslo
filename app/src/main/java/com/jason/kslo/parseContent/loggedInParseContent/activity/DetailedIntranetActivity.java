package com.jason.kslo.parseContent.loggedInParseContent.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;
import com.jason.kslo.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForDetailedIntranetFile;
import com.jason.kslo.parseContent.loggedInParseContent.parseItem.SecondLoginParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.jason.kslo.App.updateLanguage;

public class DetailedIntranetActivity extends AppCompatActivity {
    private ParseAdapterForDetailedIntranetFile fileAdapter;
    private final ArrayList<SecondLoginParseItem> secondLoginParseItems = new ArrayList<>();

    String text, firstP, lastP;
    TextView textView;
    Document doc, fileDoc;
    RecyclerView IntranetRecyclerFile;
    String detailUrl;
    Map<String,String> Cookies;

    ProgressBar progressBar;

    @SuppressWarnings({"deprecation","ConstantConditions"})
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_intranet);

        ActionBar actionBar = getSupportActionBar();
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
    @SuppressWarnings({"deprecation"})
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView = findViewById(R.id.detailedIntranetText);

            progressBar = findViewById(R.id.detailedIntranetProgressBar);
            progressBar.setVisibility(View.VISIBLE);

            IntranetRecyclerFile = findViewById(R.id.recyclerViewDetailIntranetFile);

            new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

            LinearLayoutManager fileLayoutVerticalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            IntranetRecyclerFile.setLayoutManager(fileLayoutVerticalManager);

            fileAdapter = new ParseAdapterForDetailedIntranetFile(secondLoginParseItems, getApplicationContext());
            IntranetRecyclerFile.setAdapter(fileAdapter);

            detailUrl = getIntent().getStringExtra("detailUrl");
            Cookies = IntranetFragment.getCookies();
        }


        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... voids) {
            if (getSharedPreferences("MyPref",MODE_PRIVATE).getString("ReadMail","").equals("true")) {
                try {
                    Jsoup.connect("https://www.hkmakslo.edu.hk/it-school/php/intra/readmail.php3")
                            .cookies(Cookies)
                            .method(Connection.Method.POST)
                            .data("folder", "inbox")
                            .data("order_by", "date")
                            .data("warned", "")
                            .data("formaction", "")
                            .data("sorting_method", "desc")
                            .data("mail_id", detailUrl)
                            .data("postURL", "index.php3?&folder=inbox&page=")
                            .data("isMoveMail", "1")
                            .post();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            parseDetailedIntranet(detailUrl);
            parseDetailedFileIntranet(detailUrl);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (text.contains("<script language=\"javascript\">&nbsp;")) {
                text = text.replace(text.substring(text.indexOf("<script language=\"javascript\">&nbsp;")), "");
            }
            Log.d("Intranet", "parseDetailedIntranet: " + " Text: " + text);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(text,0));
            } else {
                textView.setText(Html.fromHtml(text));
            }

            fileAdapter.notifyDataSetChanged();
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

    @SuppressLint("LongLogTag")
    private void parseDetailedFileIntranet(String mailId) {
        try{
            fileDoc = Jsoup.connect("https://www.hkmakslo.edu.hk/it-school/php/intra/mailattach.php?mail_id=" + mailId + "&encode=utf-8&1616736986")
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
                Log.d("DetailedFileIntranet", "Text: " + text +"File: " + fileName + " File Url: " + fileUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseDetailedIntranet(String mailId) {
        try{
        doc = Jsoup.connect("https://www.hkmakslo.edu.hk/it-school/php/intra/mailcontent.php?mail_id=" + mailId + "&encode=utf-8&1616736986")
                .cookies(Cookies)
                .get();

        text = doc.html();

        Elements p = doc.select("p");

        int pSize = p.size();
        firstP = p.eq(0).html();


        text = text.substring(text.indexOf(firstP));
        if (text.contains("document.onclick =")) {
            String finalDel = text.substring(text.indexOf("document.onclick ="));
            text = text.replace(finalDel, "");
        }
        if (text.contains("<!--")) {
            text = "";
        }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}