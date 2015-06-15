package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class News extends Activity {
    TextView newsTitle;
    ImageView newsPhoto;
    WebView newsContent;
    TextView newsDate;
    TextView onSite;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_in_details);
        newsTitle=(TextView)findViewById(R.id.titledetails);
        newsPhoto=(ImageView)findViewById(R.id.photodetails);
        newsContent=(WebView)findViewById(R.id.contentdetails);
        newsDate=(TextView)findViewById(R.id.datedetails);
        onSite=(TextView)findViewById(R.id.website);
        onSite.setClickable(true);
        onSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent myIntent = new Intent();
                myIntent.setClass(News.this,Website.class);
                //сюда передать еще url
            }
        });
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imagePath = intent.getStringExtra("photo");
        String content = intent.getStringExtra("content");
        String created = intent.getStringExtra("created_at");
        newsTitle.setText(title);
        newsDate.setText(created);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html=content;
        newsContent.getSettings().setJavaScriptEnabled(true);
        newsContent.setBackgroundColor(Color.TRANSPARENT);
        newsContent.loadDataWithBaseURL("", html, mimeType, encoding, "");
        Picasso.with(this).load(new File(imagePath))
                .resize(Splashscreen.screenWidth,Splashscreen.screenHeight/2)
                .placeholder(R.drawable.icon)
                .error(R.drawable.placeholder)
                .into(newsPhoto);

        /*String message = intent.getStringExtra("url");
       *//* news = (WebView)findViewById(R.id.news);
        news.getSettings().setJavaScriptEnabled(true);
        news.loadUrl(message);
        news.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });*/
    }
}