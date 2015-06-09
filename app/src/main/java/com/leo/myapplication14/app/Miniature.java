package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Miniature extends Activity {
    TextView text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.miniature);
        text = (TextView)findViewById(R.id.loading);
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
