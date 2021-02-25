package com.jason.kslo.PdfView;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import com.github.barteksc.pdfviewer.PDFView;
import com.jason.kslo.R;

import java.io.File;

public class PdfView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view);

        com.jason.kslo.App.updateLanguage(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.View_Pdf);

        PDFView pdfView = findViewById(R.id.ViewPdf);

        String filepath = ("/sdcard/Android/data/com.jason.kslo/files/Download/tmp/tmp.pdf");
        File file = new File(filepath);
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableSwipe(true)
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