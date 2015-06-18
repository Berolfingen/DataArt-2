package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Website extends Activity {
    WebView web;
    String url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.website);
        web=(WebView)findViewById(R.id.web);

        Intent intent = getIntent();
        url=intent.getStringExtra("url");
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient());
    }
}