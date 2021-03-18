package com.jason.kslo.Main.PdfView;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jason.kslo.R;

public class PdfViewHalfDaySchedule extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.Half_Day_Schedule);

        PDFView pdfView = findViewById(R.id.ViewPdf);


        pdfView.fromAsset("3C_Schedule_(Half Day).pdf")
                .enableSwipe(true)
                .enableAnnotationRendering(false)
                .scrollHandle(new DefaultScrollHandle(this))
                .fitEachPage(true)
                .pageFitPolicy(FitPolicy.BOTH)
                .spacing(2)
                .load();
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