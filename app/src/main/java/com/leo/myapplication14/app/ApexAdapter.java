package com.leo.myapplication14.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ApexAdapter extends ArrayAdapter<Apex> {
    ArrayList<Apex> ArrayListApex;
    int Resource;
    Context context;
    LayoutInflater vi;

    public ApexAdapter(Context context, int resource, ArrayList<Apex> objects) {
        super(context, resource, objects);
        ArrayListApex=objects;
        Resource=resource;
        this.context=context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= vi.inflate(Resource,null);
            holder= new ViewHolder();
            holder.photo=(ImageView)convertView.findViewById(R.id.photo);
            holder.photo.setClickable(true);
            holder.photo.setAdjustViewBounds(true);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.title.setClickable(true);
            holder.content=(WebView)convertView.findViewById(R.id.content);
            holder.url=(TextView)convertView.findViewById(R.id.url);
            holder.date=(TextView)convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(ArrayListApex.get(position).getPhoto())
                .resize(Splashscreen.screenWidth,Splashscreen.screenHeight/2)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.photo);
        holder.title.setText(ArrayListApex.get(position).getTitle());
        holder.date.setText("Дата размещения "+ArrayListApex.get(position).getCreated_at());
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html=ArrayListApex.get(position).getContent();
        holder.content.getSettings().setJavaScriptEnabled(true);
        holder.content.getSettings().setDefaultFontSize(14);
        //holder.content.setBackgroundColor(Color.argb(80,128,0,128));
        holder.content.setBackgroundColor(Color.TRANSPARENT);
        holder.content.loadDataWithBaseURL("", html, mimeType, encoding, "");
        holder.url.setClickable(true);

        View.OnClickListener ocUrl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),News.class);
                myIntent.putExtra("url",ArrayListApex.get(position).getUrl());
                v.getContext().startActivity(myIntent);
            }
        };
        holder.url.setOnClickListener(ocUrl);
        holder.photo.setOnClickListener(ocUrl);
        holder.title.setOnClickListener(ocUrl);

        return convertView;
    }

    static class ViewHolder {
        public ImageView photo;
        public TextView title;
        public WebView content;
        public TextView url;
        public TextView date;
    }
}


