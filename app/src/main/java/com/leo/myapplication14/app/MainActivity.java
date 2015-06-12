package com.leo.myapplication14.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {
    Context context = this;
    ListView list;
    ApexAdapter adapter;
    ArrayList<Apex> apexArrayListFeatured;
    ArrayList<Apex> apexArrayListArchive;
    TextView archive;
    ApexSqlliteHelper db;
    URL urlForBitmap;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new ApexSqlliteHelper(this);
        archive=(TextView)findViewById(R.id.archive);
        list =(ListView)findViewById(R.id.list);
        apexArrayListFeatured = new ArrayList<>();
        apexArrayListArchive= new ArrayList<>();
        new ApexAsynTask().execute();
    }



    private class ApexAsynTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://apex-news.herokuapp.com/all.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONArray jArray;

            try {
                jArray = new JSONArray(strJson);

                for (int i = 0; i <jArray.length() ; i++) {
                    Apex apex = new Apex();
                    JSONObject jRealObject = jArray.getJSONObject(i);
                    apex.setTitle(jRealObject.getString("title"));
                    apex.setPhoto(jRealObject.getString("photo"));
                    apex.setContent(jRealObject.getString("content"));
                    urlForBitmap = new URL(jRealObject.getString("photo"));

                    bitmap=new BitMapWorker().execute(urlForBitmap).get();
                    apex.setImage(bitmap);
                    apex.setFeatured(jRealObject.getString("featured"));
                    apex.setUrl(jRealObject.getString("url"));
                    apex.setCreated_at(jRealObject.getString("created_at"));
                    if(jRealObject.getString("featured").equals("true"))apexArrayListFeatured.add(apex);
                    else apexArrayListArchive.add(apex);
                    db.addApex(apex);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            List<Apex> listDB = db.getAllApex();
            ApexAdapter adapter = new ApexAdapter(context,R.layout.row, apexArrayListFeatured);
            list.setAdapter(adapter);
            archive.setClickable(true);
            archive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent();
                    myIntent.setClass(MainActivity.this,Archive.class);
                    Bundle bundleObject = new Bundle();
                    bundleObject.putSerializable("archivelist",apexArrayListArchive);
                    myIntent.putExtras(bundleObject);
                   startActivity(myIntent);

                }
            });
        }
    }

    class BitMapWorker extends AsyncTask<URL, Void, Bitmap>{
        Bitmap image;
        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                URL url = new URL(params[0].toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                image = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }
}
