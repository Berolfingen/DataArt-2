package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class Splashscreen extends Activity {
    ImageView logo;
    TextView text;
    Context context;
    public static int screenHeight;
    public static int screenWidth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight=metrics.heightPixels;
        screenWidth=metrics.widthPixels;

        text = (TextView)findViewById(R.id.loading);
        logo=(ImageView)findViewById(R.id.logo);
        context=getApplicationContext();
        Picasso.with(this).load(R.drawable.splashscreen)
                .resize(screenWidth,screenHeight*2/3)
                .into(logo);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent i = new Intent("com.leo.myapplication14.app.MainActivity");
                    startActivity(i);
                }
            }
        };
        timerThread.start();    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
