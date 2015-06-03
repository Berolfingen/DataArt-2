package com.leo.myapplication14.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ListView list;
    ApexAdapter adapter;
    ArrayList<Apex> apexArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        list =(ListView)findViewById(R.id.list);
        apexArrayList = new ArrayList<>();
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
                    apex.setId(jRealObject.getString("id"));
                    apex.setTitle(jRealObject.getString("title"));
                    apex.setPhoto(jRealObject.getString("photo"));
                    apex.setContent(jRealObject.getString("content"));
                    apexArrayList.add(apex);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ApexAdapter adapter = new ApexAdapter(getApplicationContext(),R.layout.row,apexArrayList);
            list.setAdapter(adapter);
        }
    }



}
