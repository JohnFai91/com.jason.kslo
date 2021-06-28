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
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.activity.SettingsActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class PdfViewSchedule extends AppCompatActivity {
    private static final String url = PdfConstants.UPDATE_SCHEDULE_URL;
    SharedPreferences sharedPreferences;
    File file;
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        setTitle(R.string.Half_Day_Schedule);

        PDFView pdfView = findViewById(R.id.ViewPdf);

        sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);

        Content content = new Content();
        content.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PDFView pdfView = findViewById(R.id.ViewPdf);

            String currentPdf = sharedPreferences.getString("CurrentSchedulePdf", "");
            if (!new File(getCacheDir() + "/" + currentPdf).exists()) {
                currentPdf = "";
                sharedPreferences.edit().putInt("CurrentSchedulePdfCode", 0).apply();
            }
            file = new File(getCacheDir() + "/" + currentPdf);

            if (currentPdf.isEmpty()) {

                pdfView.fromAsset("3C_Schedule_(Half Day).pdf")
                        .enableSwipe(true)
                        .enableAnnotationRendering(false)
                        .scrollHandle(new DefaultScrollHandle(PdfViewSchedule.this))
                        .fitEachPage(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .spacing(2)
                        .load();
            } else {
                pdfView.fromFile(file)
                        .defaultPage(sharedPreferences.getInt("PdfScheduleDefaultPage",0))
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
            int version = sharedPreferences.getInt("CurrentSchedulePdfCode",0), fileCode = 0, defaultPage = 0;
            String fileName = null,fileUrl = null;

            JSONObject obj = new JSONObject(result);
            if (sharedPreferences.getString("Class", "3C").equals("3C")) {
                fileCode = obj.getInt(PdfConstants.File_VERSION_CODE);
                fileName = obj.getString(PdfConstants.File_Name);
                fileUrl = obj.getString(PdfConstants.File_DOWNLOAD_URL);
                defaultPage = obj.getInt(PdfConstants.Default_Page);
            } else if (sharedPreferences.getString("Class", "3C").equals("2D")) {
                fileCode = obj.getInt(PdfConstants.File_VERSION_CODE_2D);
                fileName = obj.getString(PdfConstants.File_Name_2D);
                fileUrl = obj.getString(PdfConstants.File_DOWNLOAD_URL_2D);
                defaultPage = obj.getInt(PdfConstants.Default_Page_2D);
            }

            Log.d(PdfConstants.TAG, "result: " + result +
                    " fileCode: " + fileCode + " fileName: " + fileName + " fileUrl : " + fileUrl);

            String selectedClass = sharedPreferences.getString("Class", "3C");
            String pdfClass = sharedPreferences.getString("CurrentSchedulePdfClass","3C");
            if (fileCode > version) {
                startDownload(fileCode, defaultPage, fileName, fileUrl, selectedClass);
            } else if(pdfClass.equals("3C")) {
                if (!selectedClass.equals("3C")) {
                    startDownload(fileCode,defaultPage,fileName,fileUrl, selectedClass);
                } else {
                    checkDefaultPage(defaultPage);
                }

            } else if (pdfClass.equals("2D")){
                if (!selectedClass.equals("2D")) {
                    startDownload(fileCode,defaultPage,fileName, fileUrl, selectedClass);
                } else {
                    checkDefaultPage(defaultPage);
                }
            }

        } catch (JSONException e) {
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
        if (item.getItemId() == R.id.menu_action_bar_add_to_home_screen) {
            addShortcut(getString(R.string.Schedule), R.mipmap.ic_launcher_schedule);
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
        return super.onOptionsItemSelected(item);
    }
    void startDownload(int fileCode, int defaultPage, String fileName, String fileUrl, String selectedClass) {

        Intent intent = new Intent(PdfViewSchedule.this, DownloadView.class);
        intent.putExtra("title", fileName);
        intent.putExtra("fileUrl", fileUrl);
        intent.putExtra("PdfClass", selectedClass);
        intent.putExtra("fileCode", fileCode);
        intent.putExtra("defaultPage", defaultPage);
        intent.putExtra("Class", selectedClass);

        intent.putExtra("origin", "UpdateSchedule");

        finish();
        startActivity(intent);
    }
    void checkDefaultPage(int defaultPage) {
        if (!(sharedPreferences.getInt("PdfScheduleDefaultPage",0) == defaultPage)) {
            sharedPreferences.edit().putInt("PdfScheduleDefaultPage", defaultPage).apply();
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
                        .setIntent(new Intent(this, PdfViewSchedule.class).setAction(Intent.ACTION_MAIN))
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