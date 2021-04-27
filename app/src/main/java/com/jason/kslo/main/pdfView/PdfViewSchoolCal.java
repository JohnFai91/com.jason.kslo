package com.jason.kslo.main.pdfView;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.jason.kslo.R;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class PdfViewSchoolCal extends AppCompatActivity {
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.School_Cal);

        PDFView pdfView = findViewById(R.id.ViewPdf);

        pdfView.fromAsset("SchoolCal.pdf")
                .enableSwipe(true)
                .enableAnnotationRendering(false)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(2)
                .fitEachPage(true)
                .pageFitPolicy(FitPolicy.BOTH)
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