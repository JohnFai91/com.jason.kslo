package com.jason.kslo.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.SettingsActivity;
import com.jason.kslo.main.pdfView.download.PdfViewFeaturedNotice;
import com.jason.kslo.main.pdfView.download.PdfViewSchedule;
import com.jason.kslo.main.parseContent.defaultParseContent.activity.Detailed_Gallery;
import com.jason.kslo.main.parseContent.loggedInParseContent.fragment.IntranetFragment;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

@SuppressWarnings("deprecation")
public class DownloadView extends AppCompatActivity {

    // Progress Dialog
    ProgressDialog pDialog;
    final int progress_bar_type = 0;
    final int horizontal_progress_bar_type = 1;
    String file_url, origin, fileName;
    File file;
    String download, fileType;
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getIntent().getStringExtra("title"));

        origin = getIntent().getStringExtra("origin");

        // File url to download
        file_url = getIntent().getStringExtra("fileUrl");

        fileName = getIntent().getStringExtra("title");
        Log.d("origin", "origin: " + origin);
        if (origin.equals("detailedWebsite")) {
            fileName = "gallery_" + fileName + file_url.substring(file_url.lastIndexOf("/") + 1);
            fileName = fileName.replace("...","");
        }
        file = new File(getCacheDir() + "/" + fileName);

        switch (origin) {
            case "UpdateNotice":
            case "UpdateSchedule":
                download = "true";
                new DownloadFileFromURL().execute(file_url);
                break;
            case "OpenFile":
                download = "false";
                new DownloadFileFromURL().execute(file_url);
                break;
            case "detailedWebsite":
                if (!file.exists()) {
                    download = "true";
                } else {
                    download = "false";
                }
                new DownloadFileFromURL().execute(file_url);
                break;
            default:
                if (getSharedPreferences("MyPref", MODE_PRIVATE).getString("DownloadAgainQuery", "true")
                        .equals("true")) {
                    if (file.exists()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadView.this);
                        builder.setTitle(getString(R.string.DownloadAgainQuery))
                                .setMessage(getString(R.string.DownloadedMsg, fileName))
                                .setCancelable(true)
                                .setNegativeButton(getString(R.string.Download), (dialog, which) -> {
                                    download = "true";
                                    dialog.dismiss();
                                    new DownloadFileFromURL().execute(file_url);
                                })
                                .setPositiveButton(R.string.Open, (dialog, which) -> {
                                    download = "false";
                                    dialog.dismiss();
                                    new DownloadFileFromURL().execute(file_url);
                                })

                                .setNeutralButton((R.string.Share), (dialog, which) -> {
                                    Uri path;
                                    path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                                            file);
                                    Intent intent = ShareCompat.IntentBuilder.from(this)
                                            .setType("application/pdf")
                                            .setStream(path)
                                            .setChooserTitle("Choose bar")
                                            .createChooserIntent()
                                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    dialog.dismiss();
                                    onBackPressed();
                                    startActivity(intent);
                                })

                                .setOnCancelListener(dialogInterface -> {
                                    builder.create().dismiss();
                                    onBackPressed();
                                });
                        //Creating dialog box
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        download = "true";
                        new DownloadFileFromURL().execute(file_url);
                    }
                } else {
                    download = "false";
                    new DownloadFileFromURL().execute(file_url);
                }
                break;
        }
    }
    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == progress_bar_type) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.Downloading));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
            return pDialog;
        } else if (id == horizontal_progress_bar_type) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.Downloading));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (download.equals("true")) {
                if (origin.equals("DetailedFile")||(origin.equals("UpdateNotice")||(origin.equals("UpdateSchedule"))
                        ||(origin.equals("detailedWebsite")))) {
                    showDialog(progress_bar_type);
                } else {
                    showDialog(horizontal_progress_bar_type);
                }
            }
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
        if (TextUtils.equals(download, "true")) {
            int count;
            if (fileName.contains(".pdf")||fileName.contains(".jpg") || fileName.contains(".jpeg") || fileName.contains(".png")) {
                if (origin.equals("detailedIntranetFile")) {
                    try {
//Open a URL Stream
                        Connection.Response resultImageResponse;
                        Map<String, String> cookies = IntranetFragment.getCookies();
                        resultImageResponse = Jsoup.connect(getIntent().getStringExtra("fileUrl"))
                                .cookies(cookies)
                                .ignoreContentType(true)
                                .execute();
                        // output here
                        FileOutputStream out = (new FileOutputStream(getApplicationContext().getCacheDir() + "/" + fileName));
                        out.write(resultImageResponse.bodyAsBytes());
                        out.close();

                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }
                } else {
                    try {
                        URL url = new URL(f_url[0]);
                        URLConnection connection = url.openConnection();
                        connection.connect();

                        // this will be useful so that you can show a typical 0-100%
                        // progress bar
                        int lengthOfFile = connection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(),
                                8192);

                        // Output stream
                        OutputStream output = new DataOutputStream(new
                                FileOutputStream(getApplicationContext().getCacheDir() + "/" + fileName));

                        byte[] data = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress("" + (int) ((total * 100) / lengthOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();

                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }
                }
            } else {
                try {
//Open a URL Stream
                    Connection.Response resultImageResponse;
                    Map<String, String> cookies = IntranetFragment.getCookies();

                    resultImageResponse = Jsoup.connect(getIntent().getStringExtra("fileUrl"))
                            .cookies(cookies)
                            .ignoreContentType(true)
                            .execute();
                    String fileName = getIntent().getStringExtra("title");
                    // output here
                    FileOutputStream out = (new FileOutputStream(getApplicationContext().getCacheDir()
                            + "/" + fileName));
                    out.write(resultImageResponse.bodyAsBytes());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            return null;
        }


        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            if (download.equals("true")) {
                if (origin.equals("DetailedFile") || origin.equals("UpdateNotice")||origin.equals("UpdateSchedule")||
                origin.equals("detailedWebsite")) {
                    dismissDialog(progress_bar_type);
                } else {
                    dismissDialog(horizontal_progress_bar_type);
                }
            }
            if (fileName.contains(".pdf")) {
                    if (origin.equals("UpdateNotice")) {

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
                        sharedPreferences.edit().putString("CurrentPdf",fileName).apply();
                        sharedPreferences.edit().putString("CurrentPdfClass",
                                getIntent().getStringExtra("Class")).apply();
                        sharedPreferences.edit().putInt("CurrentPdfCode",
                                getIntent().getIntExtra("fileCode",1)).apply();
                        sharedPreferences.edit().putInt("PdfDefaultPage",
                                getIntent().getIntExtra("defaultPage",0)).apply();

                        finish();
                        startActivity(new Intent(DownloadView.this, PdfViewFeaturedNotice.class));
                    } else if (origin.equals("UpdateSchedule")) {

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
                        sharedPreferences.edit().putString("CurrentSchedulePdf",fileName).apply();
                        sharedPreferences.edit().putString("CurrentSchedulePdfClass",
                                getIntent().getStringExtra("Class")).apply();
                        sharedPreferences.edit().putInt("CurrentSchedulePdfCode",
                                getIntent().getIntExtra("fileCode",1)).apply();
                        sharedPreferences.edit().putInt("PdfScheduleDefaultPage",
                                getIntent().getIntExtra("defaultPage",0)).apply();
                        finish();
                        startActivity(new Intent(DownloadView.this, PdfViewSchedule.class));
                    } else {

                        PDFView pdfView = findViewById(R.id.ViewPdf);
                        pdfView.fromFile(file)
                                .enableSwipe(true)
                                .enableAnnotationRendering(false)
                                .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                                .spacing(2)
                                .load();
                    }
                } else if (fileName.contains(".jpg") || fileName.contains(".jpeg") || fileName.contains(".png")) {
                    Intent intent = new Intent(DownloadView.this, Detailed_Gallery.class);
                    intent.putExtra("filePath",getCacheDir() + "/" + fileName);
                    intent.putExtra("title", fileName);
                    intent.putExtra("origin", "downloadedFiles");
                    finish();
                    startActivity(intent);

                } else {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (download.equals("true")) {
                        if (origin.equals("DetailedFile")) {
                            dismissDialog(progress_bar_type);
                        } else {
                            dismissDialog(horizontal_progress_bar_type);
                        }
                        onBackPressed();
                    }
                    Uri data = FileProvider.getUriForFile(getApplicationContext(),
                            getApplicationContext().getPackageName() + ".provider", file);
                    if (fileName.contains(".doc") || fileName.contains(".docx")) {
                        // Word document
                        fileType = "application/msword";
                        intent.setDataAndType(data, "application/msword");
                    } else if (fileName.contains(".ppt") || fileName.contains(".pptx")) {
                        // Powerpoint file
                        fileType = "application/vnd.ms-powerpoint";
                        intent.setDataAndType(data, "application/vnd.ms-powerpoint");
                    } else if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
                        // Excel file
                        fileType = "application/vnd.ms-excel";
                        intent.setDataAndType(data, "application/vnd.ms-excel");
                    } else if (fileName.contains(".wav") || fileName.contains(".mp3")) {
                        // WAV audio file
                        fileType = "audio/x-wav";
                        intent.setDataAndType(data, "audio/x-wav");
                    } else if (fileName.contains(".gif")) {
                        // GIF file
                        fileType = "image/gif";
                        intent.setDataAndType(data, "image/gif");
                    } else if (fileName.contains(".jpg") || fileName.contains(".jpeg") || fileName.contains(".png")) {
                        // JPG file
                        fileType = "image/jpeg";
                        intent.setDataAndType(data, "image/jpeg");
                    } else if (fileName.contains(".txt")) {
                        // Text file
                        fileType = "text/plain";
                        intent.setDataAndType(data, "text/plain");
                    } else if (fileName.contains(".3gp") || fileName.contains(".mpg") ||
                            fileName.contains(".mpeg") || fileName.contains(".mpe") || fileName.contains(".mp4") ||
                            fileName.contains(".avi")) {
                        // Video files
                        fileType = "video/*";
                        intent.setDataAndType(data, "video/*");
                    } else {
                        fileType = "*/*";
                        intent.setDataAndType(data, "*/*");
                    }
                    onBackPressed();
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    finish();
                    DownloadView.this.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_files_menu, menu);
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
        return super.onOptionsItemSelected(item);
    }
}