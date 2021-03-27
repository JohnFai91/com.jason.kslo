package com.jason.kslo.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.jason.kslo.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_view);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getIntent().getStringExtra("title"));

        // File url to download
        file_url = getIntent().getStringExtra("fileUrl");

        new DownloadFileFromURL().execute(file_url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * Background Async Task to download file
     * */
    @SuppressWarnings("unchecked")
    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            origin = getIntent().getStringExtra("origin");
            if (origin.equals("DetailedFile")){
                showDialog(progress_bar_type);
            } else {
                showDialog(horizontal_progress_bar_type);
            }
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (getIntent().getStringExtra("title").contains(".pdf")) {
                if (origin.equals("detailedIntranetFile")) {
                    try {
//Open a URL Stream
                        Connection.Response resultImageResponse;
                        Map<String, String> cookies = (Map<String, String>) getIntent().getSerializableExtra("cookies");

                        resultImageResponse = Jsoup.connect(getIntent().getStringExtra("fileUrl"))
                                .cookies(cookies)
                                .ignoreContentType(true)
                                .execute();
                        // output here
                        FileOutputStream out = (new FileOutputStream(getApplicationContext().getCacheDir() + "/tmp.pdf"));
                        out.write(resultImageResponse.bodyAsBytes());
                        out.close();

                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }
                } else if (origin.equals("DetailedFile")) {
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
                                FileOutputStream(getApplicationContext().getCacheDir() + "/tmp.pdf"));

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
                Map<String, String> cookies = (Map<String, String>) getIntent().getSerializableExtra("cookies");

                    resultImageResponse = Jsoup.connect(getIntent().getStringExtra("fileUrl"))
                            .cookies(cookies)
                            .ignoreContentType(true)
                            .execute();
                    String fileName = getIntent().getStringExtra("title");
                // output here
                FileOutputStream out = (new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath()  + "/" + fileName));
                out.write(resultImageResponse.bodyAsBytes());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            if (getIntent().getStringExtra("title").contains(".pdf")) {
                if (origin.equals("DetailedFile")) {
                    dismissDialog(progress_bar_type);
                } else {
                    dismissDialog(horizontal_progress_bar_type);
                }

                String filepath = (getApplicationContext().getCacheDir() + "/tmp.pdf");
                File file = new File(filepath);

                PDFView pdfView = findViewById(R.id.ViewPdf);
                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .enableAnnotationRendering(false)
                        .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                        .spacing(2)
                        .load();
            } else {

                try {
                    fileName = getIntent().getStringExtra("title");

                    if (origin.equals("DetailedFile")) {
                    dismissDialog(progress_bar_type);
                    } else {
                        dismissDialog(horizontal_progress_bar_type);
                    }
                    onBackPressed();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()  +
                            "/" + fileName);
                    Uri data = Uri.fromFile(file);

                    if (fileName.contains(".doc") || fileName.contains(".docx")) {
                        // Word document
                        intent.setDataAndType(data, "application/msword");
                    } else if (fileName.contains(".pdf")) {
                        // PDF file
                        intent.setDataAndType(data, "application/pdf");
                    } else if (fileName.contains(".ppt") || fileName.contains(".pptx")) {
                        // Powerpoint file
                        intent.setDataAndType(data, "application/vnd.ms-powerpoint");
                    } else if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
                        // Excel file
                        intent.setDataAndType(data, "application/vnd.ms-excel");
                    } else if (fileName.contains(".wav") || fileName.contains(".mp3")) {
                        // WAV audio file
                        intent.setDataAndType(data, "audio/x-wav");
                    } else if (fileName.contains(".gif")) {
                        // GIF file
                        intent.setDataAndType(data, "image/gif");
                    } else if (fileName.contains(".jpg") || fileName.contains(".jpeg") || fileName.contains(".png")) {
                        // JPG file
                        intent.setDataAndType(data, "image/jpeg");
                    } else if (fileName.contains(".txt")) {
                        // Text file
                        intent.setDataAndType(data, "text/plain");
                    } else if (fileName.contains(".3gp") || fileName.contains(".mpg") ||
                            fileName.contains(".mpeg") || fileName.contains(".mpe") || fileName.contains(".mp4") || fileName.contains(".avi")) {
                        // Video files
                        intent.setDataAndType(data, "video/*");
                    } else {
                        intent.setDataAndType(data, "*/*");
                    }
                    getApplicationContext().grantUriPermission(getApplicationContext()
                            .getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    DownloadView.this.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}