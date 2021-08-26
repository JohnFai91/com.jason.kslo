package com.jason.kslo.main.pdfView.download;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.pm.ShortcutManagerCompat;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.HttpUtils;
import com.jason.kslo.main.DownloadView;
import com.jason.kslo.main.activity.SettingsActivity;
import org.json.JSONObject;

import java.io.File;

public class PdfViewFeaturedNotice extends AppCompatActivity {
    private static final String url = PdfConstants.UPDATE_NOTICE_URL;
    SharedPreferences sharedPreferences;
    File file;
    String currentPdf;
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);

        setTitle(R.string.Featured_Notice);

        sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        file = new File(getCacheDir() + "/" + sharedPreferences.getString("CurrentPdf", ""));

        SharedPreferences prefs = getSharedPreferences("Shortcuts",MODE_PRIVATE);
        int notice = prefs.getInt("Notice", 0);
        if (notice < 2) {
            addShortcut(getString(R.string.Notice), R.mipmap.ic_launcher_attachment);
            prefs.edit().putInt("Notice", notice + 1).apply();
            Toast.makeText(this, R.string.PleasePressAddAutomatically,Toast.LENGTH_LONG).show();
        }

        Content content = new Content();
        content.execute();

    }
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PDFView pdfView = findViewById(R.id.ViewPdf);

            currentPdf = sharedPreferences.getString("CurrentPdf", "");
            if (!new File(getCacheDir() + "/" + currentPdf).exists()) {
                currentPdf = "";
                sharedPreferences.edit().putString("CurrentPdf", "").apply();
                sharedPreferences.edit().putInt("CurrentPdfCode", 0).apply();
            }

            if (currentPdf.isEmpty()) {
                pdfView.fromAsset("Sep.pdf")
                        .defaultPage(3)
                        .enableSwipe(true)
                        .enableAnnotationRendering(false)
                        .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                        .spacing(2)
                        .fitEachPage(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .load();
            } else {
                pdfView.fromFile(file)
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
            int version = sharedPreferences.getInt("CurrentPdfCode",0), fileCode, defaultPage;
            String fileName,fileUrl;

            JSONObject obj = new JSONObject(result);
            fileCode = obj.getInt(PdfConstants.File_VERSION_CODE);
            fileName = obj.getString(PdfConstants.File_Name);
            fileUrl = obj.getString(PdfConstants.File_DOWNLOAD_URL);
            defaultPage = obj.getInt(PdfConstants.Default_Page);

            Log.d(PdfConstants.TAG, "result: " + result +
                    " fileCode: " + fileCode + " fileName: " + fileName + " fileUrl : " + fileUrl);

            if (fileCode > version) {
                startDownload(fileCode, defaultPage, fileName, fileUrl);
            } else {
                checkDefaultPage(defaultPage);
            }

        } catch (Exception e) {
            Log.e(PdfConstants.TAG, "parse json error: " + e);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_files_with_shortcuts_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_action_bar_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.menu_action_bar_share) {

            Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setType("application/pdf")
                    .setStream(path)
                    .setChooserTitle("Choose bar")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);

            return true;
        }
        if (item.getItemId() == R.id.menu_action_bar_add_to_home_screen) {
            addShortcut(getString(R.string.Notice), R.mipmap.ic_launcher_attachment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void startDownload(int fileCode, int defaultPage, String fileName, String fileUrl) {

        Intent intent = new Intent(PdfViewFeaturedNotice.this, DownloadView.class);
        intent.putExtra("title", fileName);
        intent.putExtra("fileUrl", fileUrl);
        intent.putExtra("fileCode", fileCode);
        intent.putExtra("defaultPage", defaultPage);

        intent.putExtra("origin", "UpdateNotice");

        finish();
        startActivity(intent);
    }
    void checkDefaultPage(int defaultPage) {
        if (!(sharedPreferences.getInt("PdfDefaultPage",0) == defaultPage)) {
            sharedPreferences.edit().putInt("PdfDefaultPage", defaultPage).apply();
        }
    }

    private void addShortcut(String shortcutName, int icon){
        ShortcutManager shortcutManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager = getSystemService(ShortcutManager.class);
        }

        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {

                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, shortcutName)
                        .setIcon(Icon.createWithResource(this, icon))
                        .setShortLabel(shortcutName)
                        .setIntent(new Intent(this, PdfViewFeaturedNotice.class).setAction(Intent.ACTION_MAIN))
                        .build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    shortcutManager.requestPinShortcut(pinShortcutInfo, null);
                }
            }
        }
    }
}