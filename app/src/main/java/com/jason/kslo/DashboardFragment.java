package com.jason.kslo;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.net.Uri;


public class DashboardFragment<activity> extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgressBar;
    private final String PATH = "/data/data/com.jason.kslo/";  //put the downloaded file here
    private String downloadURL ="https://gurl.pro/45spib";
    private String Path = "/sdcard/Android/data/com.jason.kslo/files/Download/Hw.ics";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        SwipeRefreshLayout mySwipeRefreshLayout;
        Uri Download_Uri = Uri.parse(downloadURL);
        FragmentActivity activity = getActivity();

        WebView webView = v.findViewById(R.id.Dashboard_webview);
        WebSettings webSettings = webView.getSettings();

        // Enable Javascript
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // hide element by class name
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('dhx_cal_date')[0].style.display='none'; })()");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("https://open-web-calendar.herokuapp.com/about.html?")){
                    return true;
                }
                if (url.contains("https://wapps1.hkedcity.net/cas/login?"))
                    return true;
                return false;
            }
        });
        webView.loadUrl("https://open-web-calendar.herokuapp.com/calendar.html?url=https%3A%2F%2Fhkedcity.instructure.com%2Ffeeds%2Fcalendars%2Fuser_r3rBBJB2zCiooISGiBIEalWRDfnn4xTBdKxgEPr9.ics&title=HKMA%20KS%20Lo%203C&tab=agenda");

        webView.canGoBack();
        webView.setOnKeyListener(new View.OnKeyListener() {

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

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.Refresh_dashboard);
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