package com.jason.kslo.main.pdfView.download;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class PdfViewFeaturedNotice extends AppCompatActivity {
    private static final String url = PdfConstants.UPDATE_URL;
    SharedPreferences sharedPreferences;
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        setTitle(R.string.Featured_Notice);

        sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);

        Content content = new Content();
        content.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PDFView pdfView = findViewById(R.id.ViewPdf);

            String currentPdf = sharedPreferences.getString("CurrentPdf", "");
            if (!new File(getCacheDir() + "/" + currentPdf).exists()) {
                currentPdf = "";
                sharedPreferences.edit().putString("CurrentPdf", "").apply();
                sharedPreferences.edit().putInt("CurrentPdfCode", 0).apply();
            }

            if (currentPdf.isEmpty()) {
                pdfView.fromAsset("May.pdf")
                        .defaultPage(3)
                        .enableSwipe(true)
                        .enableAnnotationRendering(false)
                        .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                        .spacing(2)
                        .fitEachPage(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .load();
            } else {
                pdfView.fromFile(new File(getCacheDir() + "/" + currentPdf))
                        .defaultPage(sharedPreferences.getInt("PdfDefaultPage",0))
                        .enableSwipe(true)
                        .enableAnnotationRendering(false)
                        .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                        .spacing(2)
                        .fitEachPage(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .load();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                parseJson(result);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... args) {
            return HttpUtils.get(url);
        }
    }
    private void parseJson(String result) {
        try {
            int version = sharedPreferences.getInt("CurrentPdfCode",0);

            JSONObject obj = new JSONObject(result);
            int fileCode = obj.getInt(PdfConstants.File_VERSION_CODE);
            String fileName = obj.getString(PdfConstants.File_Name);
            String fileUrl = obj.getString(PdfConstants.File_DOWNLOAD_URL);

            int defaultPage = obj.getInt(PdfConstants.Default_Page);

            Log.d(PdfConstants.TAG, "result: " + result +
                    " fileCode: " + fileCode + " fileName: " + fileName + " fileUrl : " + fileUrl);

            if (fileCode > version) {
                    sharedPreferences.edit().putInt("CurrentPdfCode", fileCode).apply();
                    sharedPreferences.edit().putInt("PdfDefaultPage", defaultPage).apply();

                    Intent intent = new Intent(PdfViewFeaturedNotice.this, DownloadView.class);
                    intent.putExtra("title", fileName);
                    intent.putExtra("fileUrl", fileUrl);
                    intent.putExtra("origin", "UpdateNotice");

                    finish();
                    startActivity(intent);

            } else {
                if (!(sharedPreferences.getInt("PdfDefaultPage",0) == defaultPage)) {
                    sharedPreferences.edit().putInt("PdfDefaultPage", defaultPage).apply();
                }
            }

        } catch (JSONException e) {
            Log.e(PdfConstants.TAG, "parse json error: " + e);
        }
    }
}