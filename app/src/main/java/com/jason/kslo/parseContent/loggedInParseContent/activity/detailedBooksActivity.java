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
import com.jason.kslo.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
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
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage(this);
        setTheme(R.style.Theme_AppCompat_DayNight_FullScreen_Actionbar);
        setContentView(R.layout.activity_detailed_books);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getIntent().getStringExtra("title"));

        url = getIntent().getStringExtra("detailUrl");
        cookies = (Map<String, String>) getIntent().getSerializableExtra("cookies");

        BkImg = findViewById(R.id.BookDetailedImage);

        Picasso.get().load(getIntent().getStringExtra("image"))
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(BkImg);

        Button renewBook = findViewById(R.id.RenewBook);
        renewBook.setText(getString(R.string.RenewBook) + " (" + getString(R.string.Coming_Soon) + ")");

        Content content = new Content();
        //noinspection deprecation
        content.execute();

    }
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class Content extends AsyncTask<Void, Void, Void> {

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