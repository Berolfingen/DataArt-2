package com.leo.myapplication14.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {
    private static final int QUANTITY_OF_NEWS =50;
    Context context = this;
    ListView list;
    ApexAdapter adapter;
    ArrayList<Apex> apexArrayListFeatured;
    ArrayList<Apex> apexArrayListArchive;
    ArrayList<String> newsIdInDB;
    ArrayList<String> fullIdJson;
    TextView archive;
    ApexSqlliteHelper db;
    private ProgressDialog pdia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new ApexSqlliteHelper(this);
        archive = (TextView) findViewById(R.id.archive);
        archive.setClickable(true);
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClass(MainActivity.this, Archive.class);
                   /* Bundle bundleObject = new Bundle();
                    bundleObject.putSerializable("archivelist",apexArrayListArchive);
                    myIntent.putExtras(bundleObject);*/
                startActivity(myIntent);

            }
        });
        list = (ListView) findViewById(R.id.list);
        apexArrayListFeatured = new ArrayList<>();
        apexArrayListArchive = new ArrayList<>();
        newsIdInDB = new ArrayList<>();
        fullIdJson = new ArrayList<>();
        if (isNetworkAvailable(context)) {
            db = new ApexSqlliteHelper(this);
            newsIdInDB=db.getAllNewsId();
            new ApexAsynTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet connection is not available. App is loading data from the database",
                    Toast.LENGTH_LONG).show();
            db = new ApexSqlliteHelper(this);
            ArrayList<Apex> archive = db.getFetchedNews();
            ApexAdapter adapter = new ApexAdapter(context, R.layout.row, archive, true);
            list.setAdapter(adapter);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("apexFeatured", apexArrayListFeatured);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class ApexAsynTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(context);
            pdia.setCanceledOnTouchOutside(false);
            pdia.setMessage("Loading...");
            pdia.show();
        }

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

                for (int i = 0; i < QUANTITY_OF_NEWS; i++) {
                    Apex apex = new Apex();
                    JSONObject jRealObject = jArray.getJSONObject(i);
                    String id= jRealObject.getString("id");
                    fullIdJson.add(id);
                    if(newsIdInDB.contains(id)){
                        continue;
                    }
                    else {
                        apex.setIdNews(id);
                        apex.setTitle(jRealObject.getString("title"));
                        apex.setPhoto(jRealObject.getString("photo"));
                        apex.setContent(jRealObject.getString("content"));
                        apex.setImagePath(saveToInternalSorage(new BackTask().execute(jRealObject.getString("photo")).get(), "image" + jRealObject.getString("title")));
                        apex.setUrl(jRealObject.getString("url"));
                        apex.setFeatured(jRealObject.getString("featured"));
                        apex.setUrl(jRealObject.getString("url"));
                        apex.setCreated_at(jRealObject.getString("created_at"));

                        db.addApex(apex);

                       /* if (jRealObject.getString("featured").equals("true")) apexArrayListFeatured.add(apex);
                        else apexArrayListArchive.add(apex);*/
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            pdia.dismiss();
            db.checkOnUpdates(fullIdJson);
            db = new ApexSqlliteHelper(context);
            ArrayList<Apex> archive = db.getFetchedNews();
            ApexAdapter adapter = new ApexAdapter(context, R.layout.row, archive, true);
            list.setAdapter(adapter);

        }
    }

    private class BackTask extends AsyncTask<String, Void, Bitmap> {


        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                // Download the image
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                // Decode image to get smaller image to save memory
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeStream(is, null, options);
                is.close();
            } catch (IOException e) {
                Log.d("IO ", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    private String saveToInternalSorage(Bitmap bitmapImage, String id) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, id + ".jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }


}
