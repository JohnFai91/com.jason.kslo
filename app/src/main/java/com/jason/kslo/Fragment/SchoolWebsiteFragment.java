package com.jason.kslo.Fragment;

import android.content.*;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.MotionEvent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.net.Uri;
import android.app.DownloadManager;
import android.webkit.URLUtil;
import android.os.Environment;
import android.widget.Toast;
import android.view.KeyEvent;
import androidx.webkit.WebSettingsCompat;
import com.jason.kslo.PdfView.PdfView;
import com.jason.kslo.R;

import java.io.File;
import java.util.Objects;

public class SchoolWebsiteFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_school_website, container, false);

        com.jason.kslo.App.updateLanguage(Objects.requireNonNull(getContext()));

        Button button = v.findViewById(R.id.Open_pdf);
        button.setVisibility(View.GONE);

        final WebView webView = v.findViewById(R.id.home_webview);
        webView.loadUrl("https://www.hkmakslo.edu.hk");

        File file = new File("/sdcard/Android/data/com.jason.kslo/files/Download/tmp/tmp.pdf");
        if (file.exists()) {
            file.delete();
        }

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String Theme = prefs.getString("theme", "");
        switch (Theme) {
            case "Follow System":
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        webSettings.setForceDark(WebSettingsCompat.FORCE_DARK_ON);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        webSettings.setForceDark(WebSettingsCompat.FORCE_DARK_OFF);
                        break;
                }
                break;
            case "Day Mode":
                webSettings.setForceDark(WebSettingsCompat.FORCE_DARK_OFF);
                break;
            case "Night Mode":
                webSettings.setForceDark(WebSettingsCompat.FORCE_DARK_ON);
                break;
        }

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
                webView.loadUrl("javascript:(function() { " +

                        "document.getElementsByClassName('row row-footer')[0].style.display='none'; })()");
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
                                            getContext();
                                            DownloadManager dm = (DownloadManager) Objects.requireNonNull(getContext()).getSystemService(Context.DOWNLOAD_SERVICE);
                                            dm.enqueue(request);
                                            Toast.makeText(getContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                                            getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                            webView.goBack();
                                        }

                                        final BroadcastReceiver onComplete = new BroadcastReceiver() {
                                            @Override
                                            public void onReceive(Context context, Intent intent) {
                                                Toast.makeText(Objects.requireNonNull(getContext()), "Download Complete, click button on left to view.", Toast.LENGTH_SHORT).show();
                                                button.setVisibility(View.VISIBLE);

                                                button.setOnClickListener(view -> {
                                                    Intent intent1 = new Intent(getContext(), PdfView.class);
                                                    startActivity(intent1);
                                                });
                                                new Handler().postDelayed(() -> {
                                                    button.setVisibility(View.GONE);
                                                    if (file.exists()) {
                                                        file.delete();
                                                    }
                                                }, 5000);
                                            }
                                        };
                                    });

        webView.canGoBack();
    webView.setOnKeyListener((v1, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == MotionEvent.ACTION_UP
                && webView.canGoBack()) {
            webView.goBack();
            return true;
            }
            return false;
        });

        swipeRefreshLayout = v.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                webView.reload();
            },1500);
        });
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );
        return v;}
}