package com.jason.kslo.main.parseContent.loggedInParseContent.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
import com.jason.kslo.main.parseContent.loggedInParseContent.fragment.LoginFragment;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForDetailedIntranetFile;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.LoginParseItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.jason.kslo.App.updateLanguage;

public class DetailedIntranetActivity extends AppCompatActivity {

    String text, firstP, lastP, receiverTxt;
    TextView textView, receiver;
    Document doc, fileDoc;
    RecyclerView IntranetRecyclerFile;
    String detailUrl;
    Map<String,String> Cookies;
    private final ArrayList<LoginParseItem> LoginParseItems = new ArrayList<>();
    private ParseAdapterForDetailedIntranetFile fileAdapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_intranet);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title"));

        String title = getIntent().getStringExtra("title");
        String sender = getIntent().getStringExtra("sender");
        String time = getIntent().getStringExtra("time");
        detailUrl = getIntent().getStringExtra("detailUrl");

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

    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            receiver = findViewById(R.id.detailedIntranetReceiver);
            textView = findViewById(R.id.detailedIntranetText);

            progressBar = findViewById(R.id.detailedIntranetProgressBar);
            progressBar.setVisibility(View.VISIBLE);

            IntranetRecyclerFile = findViewById(R.id.recyclerViewDetailIntranetFile);

            new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

            LinearLayoutManager fileLayoutVerticalManager
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            IntranetRecyclerFile.setLayoutManager(fileLayoutVerticalManager);

            fileAdapter = new ParseAdapterForDetailedIntranetFile(LoginParseItems, getApplicationContext());
            IntranetRecyclerFile.setAdapter(fileAdapter);

            Cookies = LoginFragment.getCookies();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            parseDetailedIntranet(detailUrl);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(text,0));
            } else {
                textView.setText(Html.fromHtml(text));
            }

            receiver.setText(receiverTxt);
            progressBar.setVisibility(View.GONE);
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

    private void parseDetailedIntranet(String detailUrl) {
        try{
            doc = Jsoup.connect("https://eclass.hkmakslo.edu.hk/home/imail_gamma/" + detailUrl)
                    .cookies(Cookies)
                    .get();

            text = doc.select("td.message")
                    .select("div")
                    .eq(0)
                    .html();

            Elements fileTable= doc.select("td.tabletext")
                    .select("span.tabletext")
                    .select("a.tabletool");

            for (int i = 0; i < fileTable.size(); i++) {
                String fileUrl = fileTable
                        .eq(i)
                        .attr("href");

                String fileName = fileTable
                        .eq(i)
                        .text();

                if (fileUrl.contains("cache_view_attachment.php?")) {
                    fileUrl = "https://eclass.hkmakslo.edu.hk/home/imail_gamma/" + fileUrl;

                LoginParseItems.add(new LoginParseItem(fileName, fileUrl, Cookies));
            }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
