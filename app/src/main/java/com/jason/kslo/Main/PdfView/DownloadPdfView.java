package com.jason.kslo.Main.PdfView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
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
public class DownloadPdfView extends AppCompatActivity {

    // Progress Dialog
    ProgressDialog pDialog;
    int progress_bar_type = 0;
    String file_url, origin;
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

        origin = getIntent().getStringExtra("origin");
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
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (origin.equals("detailedIntranetFile")){
                try {
//Open a URL Stream
                    Connection.Response resultImageResponse = null;
                    Map<String,String> cookies = (Map<String, String>) getIntent().getSerializableExtra("cookies");

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
            } else if (origin.equals("DetailedFile")){
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

                    byte data[] = new byte[1024];

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
            dismissDialog(progress_bar_type);

            String filepath = (getApplicationContext().getCacheDir() + "/tmp.pdf");
            File file = new File(filepath);

            PDFView pdfView = findViewById(R.id.ViewPdf);
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .enableAnnotationRendering(false)
                    .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                    .spacing(2)
                    .load();
        }
    }
}