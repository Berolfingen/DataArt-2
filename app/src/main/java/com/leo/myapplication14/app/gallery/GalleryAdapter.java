package com.leo.myapplication14.app.gallery;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.leo.myapplication14.app.News;
import com.leo.myapplication14.app.R;
import com.leo.myapplication14.app.Splashscreen;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<GalleryImage> {
    ArrayList<GalleryImage> ArrayListGalleryImage;
    int Resource;
    Context context;
    LayoutInflater vi;

    public GalleryAdapter(Context context, int resource, ArrayList<GalleryImage> objects) {
        super(context, resource, objects);
        ArrayListGalleryImage = objects;
        Resource = resource;
        this.context = context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = vi.inflate(Resource, null);
            holder = new ViewHolder();
            holder.gallery_image = (ImageView) convertView.findViewById(R.id.gallery_image);
            holder.gallery_image.setClickable(true);
            holder.gallery_image.setAdjustViewBounds(true);
            holder.gallery_text = (TextView) convertView.findViewById(R.id.gallery_text);
            holder.gallery_text.setClickable(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(new File(ArrayListGalleryImage.get(position).getImagePath()))
                .resize(Splashscreen.screenWidth * 7 / 30, Splashscreen.screenHeight / 7)
                .placeholder(R.drawable.icon).error(R.drawable.placeholder).into(holder.gallery_image);
        holder.gallery_text.setText(ArrayListGalleryImage.get(position).getShortTitle());

        View.OnClickListener ocUrl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), News.class);
                myIntent.putExtra("title", ArrayListGalleryImage.get(position).getTitle());
                myIntent.putExtra("photo", ArrayListGalleryImage.get(position).getImagePath());
                myIntent.putExtra("content", ArrayListGalleryImage.get(position).getContent());
                myIntent.putExtra("created_at", ArrayListGalleryImage.get(position).getCreated_atFormatted());
                myIntent.putExtra("url", ArrayListGalleryImage.get(position).getUrl());
                v.getContext().startActivity(myIntent);
            }
        };
        holder.gallery_image.setOnClickListener(ocUrl);
        holder.gallery_text.setOnClickListener(ocUrl);
        return convertView;
    }

    static class ViewHolder {
        public ImageView gallery_image;
        public TextView gallery_text;
    }
}
