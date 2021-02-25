package com.jason.kslo.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;


public class DashboardFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        com.jason.kslo.App.updateLanguage(requireContext());

        final View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

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
                return url.contains("https://wapps1.hkedcity.net/cas/login?");
            }
        });
        webView.loadUrl("https://open-web-calendar.herokuapp.com/calendar.html?url=https://hkedcity.instructure.com/feeds/calendars/user_r3rBBJB2zCiooISGiBIEalWRDfnn4xTBdKxgEPr9.ics&title=HKMA%20KS%20Lo%203C&tab=agenda");

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

        swipeRefreshLayout = v.findViewById(R.id.Refresh_dashboard);
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