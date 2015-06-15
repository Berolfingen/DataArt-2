package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.File;

public class News extends Activity {
    TextView newsTitle;
    ImageView newsPhoto;
    WebView newsContent;
    TextView newsDate;
    TextView onSite;
    String url;
    Context context=this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_in_details);
        newsTitle=(TextView)findViewById(R.id.titledetails);
        newsPhoto=(ImageView)findViewById(R.id.photodetails);
        newsContent=(WebView)findViewById(R.id.contentdetails);
        newsDate=(TextView)findViewById(R.id.datedetails);
        onSite=(TextView)findViewById(R.id.website);
        onSite.setClickable(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imagePath = intent.getStringExtra("photo");
        String content = intent.getStringExtra("content");
        String created = intent.getStringExtra("created_at");
        url=intent.getStringExtra("url");

        onSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isNetworkAvailable(context)) {
                    Intent myIntent = new Intent();
                    myIntent.setClass(News.this, Website.class);
                    myIntent.putExtra("url", url);
                    startActivity(myIntent);
                } else{
                    Toast.makeText(getApplicationContext(), "Internet connection is not available",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        newsTitle.setText(title);
        newsDate.setText(created);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html=content;
        newsContent.getSettings().setJavaScriptEnabled(true);
        newsContent.setBackgroundColor(Color.TRANSPARENT);
        newsContent.loadDataWithBaseURL("", html, mimeType, encoding, "");
        newsContent.getSettings().setJavaScriptEnabled(true);
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