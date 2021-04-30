package com.jason.kslo.parseContent.loggedInParseContent.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.BorrowedBooksFragment;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;

import static com.jason.kslo.App.updateLanguage;

public class detailedBooksActivity extends AppCompatActivity {
    String ACNO,Version,EditionYear,Publisher,ISBN,BookClassification,PositionInLib,CallNumber,Writer;
    TextView TitleTextView, ACNOTextView, PositionInLibTextView, CallNumberTextView, BookClassificationTextView, ISBNTextView,
            PublisherTextView, EditionYearTextView, VersionTextView, dueDateTextView, borrowedDateTextView, WriterTextView;
    ImageView BkImg;
    String url;
    Map<String,String> cookies;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    @SuppressLint("StaticFieldLeak")
    static View view;

    @SuppressLint({"StaticFieldLeak", "SetTextI18n"})
    @SuppressWarnings({"ConstantConditions"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage(this);
        setContentView(R.layout.activity_detailed_books);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getIntent().getStringExtra("title"));

        url = getIntent().getStringExtra("detailUrl");
        cookies = IntranetFragment.getCookies();

        BkImg = findViewById(R.id.BookDetailedImage);

        Picasso.get().load(getIntent().getStringExtra("image"))
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(BkImg);
        view = findViewById(android.R.id.content);

        Button renewBook = findViewById(R.id.RenewBook);
        renewBook.setText(getString(R.string.RenewBook));
        renewBook.setOnClickListener(view -> {
            RenewBook renewBook1 = new RenewBook();
            renewBook1.execute();
        });
        Content content = new Content();
        content.execute();

    }
    @SuppressLint("StaticFieldLeak")
    private class RenewBook extends AsyncTask<Void, Void, Void> {
        Document document;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String jsonBody = "{\"sno\":\"201801323\",\"acno\":\"C014185\"}";
                document = Jsoup.connect("https://lm.hkmakslo.edu.hk/Services/BookService.asmx/RenewBook")
                                                    .method(Connection.Method.POST)
                                                    .cookies(BorrowedBooksFragment.getCookies())
                                                    .header("Content-Type", "application/json")
                                                    .header("Accept", "application/json")
                                                    .followRedirects(true)
                                                    .ignoreHttpErrors(true)
                                                    .ignoreContentType(true)
                                                    .userAgent("Mozilla/5.0 AppleWebKit/537.36 (KHTML," +
                                                            " like Gecko) Chrome/45.0.2454.4 Safari/537.36")
                                                    .method(Connection.Method.POST)
                                                    .requestBody(jsonBody)
                                                    .maxBodySize(1_000_000 * 30) // 30 mb ~
                                                    .timeout(0) // infinite timeout
                                                    .post();
                Log.d("RenewBook", "Msg: " + document);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            String result = document.text().replaceAll("\"","");
            Log.d("RenewBook", "ParsedMsg: " + result);
            Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();
            Content content = new Content();
            content.execute();
        }
    }
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class Content extends AsyncTask<Void, Void, Void> {

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
            progressBar = findViewById(R.id.detailedBookProgress);
            progressBar.setVisibility(View.VISIBLE);

            TitleTextView = findViewById(R.id.DetailedBookTitle);
            TitleTextView.setText(getIntent().getStringExtra("title"));

            dueDateTextView = findViewById(R.id.DetailedBookDueDate);
            dueDateTextView.setText(getString(R.string.DueDate) + getIntent().getStringExtra("dueDate"));

            borrowedDateTextView = findViewById(R.id.DetailedBookBorrowedDate);
            borrowedDateTextView.setText(getString(R.string.BorrowedDate) + getIntent().getStringExtra("borrowedDate"));

            ACNOTextView = findViewById(R.id.ACNOText);
            PositionInLibTextView = findViewById(R.id.PositionInLibText);
            CallNumberTextView = findViewById(R.id.CallNumberText);
            BookClassificationTextView = findViewById(R.id.BookClassificationText);
            ISBNTextView = findViewById(R.id.ISBNText);
            PublisherTextView = findViewById(R.id.PublisherText);
            EditionYearTextView = findViewById(R.id.EditionYearText);
            VersionTextView = findViewById(R.id.VersionText);
            WriterTextView = findViewById(R.id.WriterText);

            setText();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc;

            try {
                doc = Jsoup.connect(url)
                        .get();

                Log.d("ParseDetailedLM", "Cookies: " + cookies);

                Elements table = doc.select("div.widget-content");
                Elements topTable = doc.select("div.deck.book-info");

                ACNO = table.select("tr")
                                    .eq(0)
                                    .select("td.value")
                                    .text();

                CallNumber = table.select("tr")
                        .eq(1)
                        .select("td.value")
                        .text();

                PositionInLib = table.select("tr")
                        .eq(3)
                        .select("td.value")
                        .text();

                BookClassification = table.select("tr")
                        .eq(4)
                        .select("td.value")
                        .text();

                ISBN = table.select("tr")
                        .eq(5)
                        .select("td.value")
                        .text();

                Publisher = table.select("tr")
                        .eq(6)
                        .select("td.value")
                        .text();

                Writer = topTable.select("p.bk_author_name")
                        .eq(0)
                        .text();

                EditionYear = table.select("tr")
                        .eq(7)
                        .select("td.value")
                        .text();
                EditionYear = EditionYear.replaceAll("\\.","");

                Version = table.select("tr")
                        .eq(8)
                        .select("td.value")
                        .text();

                Log.d("DetailedBooks", "ACNO: " + ACNO + " CallNumber: " + CallNumber + " PositionInLib: " + PositionInLib
                + " BookClassification: " + BookClassification + " ISBN: " + ISBN + " Publisher: " + Publisher + " Writer: " + Writer +
                        " EditionYear: " + EditionYear + " Version: " +Version);

        } catch (Exception e) {
            e.printStackTrace();
        }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

                // Stuff that updates the UI
            progressBar.setVisibility(View.GONE);
            setText();

            super.onPostExecute(unused);
        }
    }

    private void setText(){
        ACNOTextView.setText(ACNO);
        VersionTextView.setText(Version);
        PositionInLibTextView.setText(PositionInLib);
        CallNumberTextView.setText(CallNumber);
        BookClassificationTextView.setText(BookClassification);
        ISBNTextView.setText(ISBN);
        PublisherTextView.setText(Publisher);
        EditionYearTextView.setText(EditionYear);
        WriterTextView.setText(Writer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}