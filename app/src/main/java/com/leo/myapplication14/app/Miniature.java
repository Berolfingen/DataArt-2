package com.leo.myapplication14.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class Miniature extends Activity {
    ImageView minuature;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miniature);
        minuature = (ImageView)findViewById(R.id.minuature);
        final int welcomeScreenDisplay = 4000;
        Thread welcomeThread = new Thread() {
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    startActivity(new Intent(Miniature.this, MainActivity.class));
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
