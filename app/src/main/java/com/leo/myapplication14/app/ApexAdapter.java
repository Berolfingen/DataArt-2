package com.leo.myapplication14.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.content=(WebView)convertView.findViewById(R.id.content);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        new DownloadImageTask(holder.photo).execute(ArrayListApex.get(position).getPhoto());
        holder.id.setText(ArrayListApex.get(position).getId());
        holder.title.setText(ArrayListApex.get(position).getTitle());
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html=ArrayListApex.get(position).getContent();
        holder.content.getSettings().setJavaScriptEnabled(true);
        holder.content.loadDataWithBaseURL("",html,mimeType,encoding,"");
        return convertView;
    }

    static class ViewHolder {
        public ImageView photo;
        public TextView id;
        public TextView title;
        public WebView content;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
    }
}


