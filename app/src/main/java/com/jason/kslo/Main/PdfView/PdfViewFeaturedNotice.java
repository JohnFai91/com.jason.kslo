package com.jason.kslo.Main.PdfView;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jason.kslo.R;

public class PdfViewFeaturedNotice extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view);

        PDFView pdfView = findViewById(R.id.ViewPdf);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        setTitle(R.string.Featured_Notice);

        pdfView.fromAsset("Feb_March_Notice.pdf")
                .defaultPage(1)
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