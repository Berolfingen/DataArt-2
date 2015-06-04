package com.leo.myapplication14.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= vi.inflate(Resource,null);
            holder= new ViewHolder();
            holder.photo=(ImageView)convertView.findViewById(R.id.photo);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.content=(WebView)convertView.findViewById(R.id.content);
            holder.url=(TextView)convertView.findViewById(R.id.url);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //new DownloadImageTask(holder.photo).execute(ArrayListApex.get(position).getPhoto());
        Picasso.with(context).load(ArrayListApex.get(position).getPhoto()).resize(250,250).into(holder.photo);
        holder.title.setText(ArrayListApex.get(position).getTitle());
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html=ArrayListApex.get(position).getContent();
        holder.content.getSettings().setJavaScriptEnabled(true);
        holder.content.loadDataWithBaseURL("", html, mimeType, encoding, "");
        //holder.url.setClickable(true);
        holder.url.setText(Html.fromHtml("<a href="+ArrayListApex.get(position).getUrl()+">Подробнее</a>"));
        holder.url.setMovementMethod(LinkMovementMethod.getInstance());
        return convertView;
    }

    static class ViewHolder {
        public ImageView photo;
        public TextView title;
        public WebView content;
        public TextView url;

    }

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
}


