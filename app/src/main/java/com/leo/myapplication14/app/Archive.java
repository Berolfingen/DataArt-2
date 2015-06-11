package com.leo.myapplication14.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Archive extends Activity {
    Context context = this;
    ListView list1;
    TextView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive);
        list1 = (ListView) findViewById(R.id.list1);
        main = (TextView) findViewById(R.id.main);
        Bundle bundleObject = getIntent().getExtras();
        ArrayList<Apex> archive = (ArrayList<Apex>) bundleObject.getSerializable("archivelist");
        ApexAdapter adapter = new ApexAdapter(context, R.layout.row, archive);
        list1.setAdapter(adapter);
        main.setClickable(true);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* //Intent i = new Intent("com.leo.myapplication14.app.MainActivity");
                startActivity(i);*/
                finish();
            }
        });
    }
}