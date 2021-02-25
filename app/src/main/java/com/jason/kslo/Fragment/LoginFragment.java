package com.jason.kslo.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.webkit.*;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;

public class LoginFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        com.jason.kslo.App.updateLanguage(requireContext());

        final View v = inflater.inflate(R.layout.fragment_login, container, false);
        final WebView webView = v.findViewById(R.id.login_webview);
        webView.loadUrl("https://www.hkmakslo.edu.hk");

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
                                                    "document.getElementsByClassName('widget news')[0].style.display='none'; })()");
                                            webView.loadUrl("javascript:(function() { " +
                                                    "document.getElementsByClassName('content_wrap')[0].style.display='none'; })()");
                                            webView.loadUrl("javascript:(function() { " +
                                                    "document.getElementsByClassName('MainNav_menu')[0].style.display='none'; })()");
                                            webView.loadUrl("javascript:(function() { " +
                                                    "document.getElementsByClassName('row row-footer')[0].style.display='none'; })()");
                                        }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return url.equals("https://www.hkmakslo.edu.hk/it-school/php/popupMessageBox.php");
            }

});

        swipeRefreshLayout = v.findViewById(R.id.Refresh_login);
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