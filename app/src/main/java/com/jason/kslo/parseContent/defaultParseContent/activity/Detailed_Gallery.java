package com.jason.kslo.parseContent.defaultParseContent.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.jason.kslo.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class Detailed_Gallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_gallery);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }

        PhotoView photoView = findViewById(R.id.DetailedGalleryPhotoView);
        Picasso.get().load(getIntent().getStringExtra("detailUrl")).memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                .into(photoView);

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