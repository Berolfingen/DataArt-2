package com.leo.myapplication14.app.gallery;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;
import com.leo.myapplication14.app.Apex;
import com.leo.myapplication14.app.ApexSqlliteHelper;
import com.leo.myapplication14.app.R;

import java.util.ArrayList;

public class Gallery_Main extends ActionBarActivity {
    Context context = this;
    GridView gallery;
    GalleryAdapter adapter;
    ArrayList<GalleryImage> images;
    ApexSqlliteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);
        gallery=(GridView)findViewById(R.id.gallery);
        images=new ArrayList<>();
        Intent intent = getIntent();

        db = new ApexSqlliteHelper(context);
        ArrayList<Apex> full = db.getAllApex();
        for (int i = 0; i <full.size() ; i++) {
            GalleryImage image = new GalleryImage();
            image.setTitle(full.get(i).getTitle());
            image.setUrl(full.get(i).getUrl());
            image.setContent(full.get(i).getContent());
            image.setCreated_at(full.get(i).getCreated_at());
            image.setImagePath(full.get(i).getImagePath());
            images.add(image);
        }

        adapter = new GalleryAdapter(context,R.layout.gallery_row, images);
        gallery.setAdapter(adapter);
    }
}
