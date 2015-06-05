package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class News extends Activity {
    WebView news;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_in_details);
        Intent intent = getIntent();
        String message = intent.getStringExtra("url");
        news = (WebView)findViewById(R.id.news);
        news.getSettings().setJavaScriptEnabled(true);
        news.loadUrl(message);
        news.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }
}
