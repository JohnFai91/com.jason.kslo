package com.jason.kslo.ParseContent.Activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.ParseContent.ParseAdapter.ParseAdapterForDetailedWebsite;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DetailedImageActivity extends AppCompatActivity {
    private ParseAdapterForDetailedWebsite adapter;
    private final ArrayList<ParseItem> parseItems = new ArrayList<>();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("title") + "");

        TextView titleTextView = findViewById(R.id.TitleDetailedText);
        ImageView titleImageView = findViewById(R.id.TitleDetailedImage);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewDetail);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapterForDetailedWebsite(parseItems, this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();

        titleTextView.setText(getIntent().getStringExtra("title"));

        Picasso.get().load(getIntent().getStringExtra("image")).memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE).into(titleImageView);
    }

    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            parseDetailedImages(getIntent().getStringExtra("detailUrl"));

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
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

            Document doc = Jsoup.connect(url).get();

            Elements data = doc.select("ul.thumbnails");
            Elements li = data.select("li");

            int size = li.size();

            for (int i = 0; i < size; i++)  {
                String imgUrl = li
                        .select("a")
                        .eq(i)
                        .attr("href");


                String baseImageUrl = "https://www.hkmakslo.edu.hk";
                imgUrl = baseImageUrl + imgUrl;

                parseItems.add(new ParseItem(imgUrl));
                Log.d("items", ". img: " + imgUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}