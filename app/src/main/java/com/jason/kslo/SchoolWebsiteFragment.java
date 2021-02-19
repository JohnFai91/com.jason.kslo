package com.jason.kslo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.FragmentActivity;
import android.view.MotionEvent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.net.Uri;
import android.content.Intent;
import android.app.DownloadManager;
import android.webkit.URLUtil;
import android.os.Environment;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

import java.io.File;

public class SchoolWebsiteFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_school_website, container, false);


        Button button = v.findViewById(R.id.Open_pdf);
        button.setVisibility(View.GONE);

        SwipeRefreshLayout mySwipeRefreshLayout;
        FragmentActivity activity = getActivity();
        final WebView webView = (WebView) v.findViewById(R.id.home_webview);
        webView.loadUrl("https://www.hkmakslo.edu.hk");

        File file = new File("/sdcard/Android/data/com.jason.kslo/files/Download/tmp/tmp.pdf");
        if (file.exists()) {
            file.delete();
        }

        String webUrl = webView.getUrl();
        Uri uri = Uri.parse(webUrl);

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // hide element by class name
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('widget links')[0].style.display='none'; })()");
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('map')[0].style.display='none'; })()");
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('login')[0].style.display='none'; })()");
            }
        });

        //download
        webView.setDownloadListener(new DownloadListener() {
                                        @Override
                                        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                                            request.setDescription("Downloading file...");
                                            request.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, "/tmp/tmp.pdf");
                                            DownloadManager dm = (DownloadManager) getContext().getSystemService(getContext().DOWNLOAD_SERVICE);
                                            dm.enqueue(request);
                                            Toast.makeText(getContext().getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                                            getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                            webView.goBack();
                                        }

                                        BroadcastReceiver onComplete = new BroadcastReceiver() {
                                            @Override
                                            public void onReceive(Context context, Intent intent) {
                                                Toast.makeText(getContext().getApplicationContext(), "Download Complete, click button on left to view.", Toast.LENGTH_SHORT).show();
                                                button.setVisibility(View.VISIBLE);

                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getContext(), PdfView.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        button.setVisibility(View.GONE);
                                                        if (file.exists()) {
                                                            file.delete();
                                                        }
                                                    }
                                                }, 5000);
                                            }
                                        };
                                    });

        webView.canGoBack();
    webView.setOnKeyListener(new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == MotionEvent.ACTION_UP
                    && webView.canGoBack()) {
                webView.goBack();
                return true;
                }
                return false;
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webView.reload();
                    }
                },1500);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );
        return v;}
}