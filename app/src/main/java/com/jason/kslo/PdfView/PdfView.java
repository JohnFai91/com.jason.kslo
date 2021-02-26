package com.jason.kslo.PdfView;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jason.kslo.R;
import android.content.SharedPreferences;
import java.util.Objects;

import java.io.File;

public class PdfView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view_single_page);

        com.jason.kslo.App.updateLanguage(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.View_Pdf);

        PDFView pdfView = findViewById(R.id.ViewPdf);

        String filepath = ("/sdcard/Android/data/com.jason.kslo/files/Download/tmp/tmp.pdf");
        File file = new File(filepath);

        getApplicationContext();
        SharedPreferences prefs = Objects.requireNonNull(getApplicationContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
        String Theme = prefs.getString("theme", "");
        switch (Theme) {
            case "Follow System":
                if (getSystemService(Theme) == "Light")
                    pdfView.setNightMode(false);
                else if (getSystemService(Theme) == "Dark")
                    pdfView.setNightMode(true);

            case "Day Mode":
                pdfView.setNightMode(false);

            case "Night Mode":
                pdfView.setNightMode(true);
        }


        pdfView.fromFile(file)
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