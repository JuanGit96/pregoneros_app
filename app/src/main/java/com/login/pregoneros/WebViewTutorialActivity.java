package com.login.pregoneros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class WebViewTutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_tutorial);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.youtube.com/watch?v=OwuCVC988ws");

/*
* POR SI SE DESEA VER SOLO ELVIDEO USANDO HTML
* */
//        WebView myWebView = (WebView) findViewById( R.id.webview );
//
//        myWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//        });
//
//        myWebView.getSettings().setJavaScriptEnabled(true);
//
//        myWebView.getSettings().setDomStorageEnabled(true);
//
//        String playVideo= "<html><body>Youtube video .. <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"https://www.youtube.com/embed/OwuCVC988ws\" frameborder=\"0\"></body></html>";
//
//        myWebView.loadData(playVideo, "text/html", "utf-8");


    }

}


