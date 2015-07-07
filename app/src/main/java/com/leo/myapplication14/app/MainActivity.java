package com.leo.myapplication14.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.leo.myapplication14.app.gallery.Gallery_Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    public static int QUANTITY_OF_NEWS_IN_DB;
    Context context = this;
    ListView list;
    ArrayList<Apex> full;
    ArrayList<String> newsIdInDB;
    ArrayList<String> fullIdJson;
    TextView archive;
    ApexSqlliteHelper db;
    HashMap<String, String> id_updated;
    EditText edit;
    String value;
    private ProgressDialog pdia;
    private static final String MY_PREFERENCES = "my_preferences";
    private static final String QUANTITY_IN_DB = "quantityInDb";
    SharedPreferences myPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new ApexSqlliteHelper(this);
        myPref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();


        if (isFirst(MainActivity.this)) {
            editor.putInt(QUANTITY_IN_DB, 50);
            editor.apply();
        }
        QUANTITY_OF_NEWS_IN_DB = myPref.getInt(QUANTITY_IN_DB, 0);
        Log.d("q", Integer.toString(QUANTITY_OF_NEWS_IN_DB));
        archive = (TextView) findViewById(R.id.archive);
        archive.setClickable(true);
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClass(MainActivity.this, Archive.class);
                startActivity(myIntent);

            }
        });
        list = (ListView) findViewById(R.id.list);
        full = new ArrayList<>();
        id_updated = new HashMap<>();
        newsIdInDB = new ArrayList<>();
        fullIdJson = new ArrayList<>();

        if (isNetworkAvailable(context)) {
            Date date = new Date(System.currentTimeMillis());
            editor.putLong("time", date.getTime());
            editor.apply();
            db = new ApexSqlliteHelper(this);
            newsIdInDB = db.getAllNewsId();
            id_updated = db.getUpdatedDatesToId(newsIdInDB);

            new ApexAsynTask().execute();

        } else {
            long millis = myPref.getLong("time", 0L);
            Date date = new Date(millis);
            Toast.makeText(getApplicationContext(), "Internet connection is not available. App is loading data from the database." +
                            "Last update was on " + date.getDate() + " " + date.getMonth() + " " + date.getYear() + " " + date.getHours() +
                            ":" + date.getMinutes(),
                    Toast.LENGTH_LONG).show();
            setAdapterToMain();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.quantity);

        if (!isNetworkAvailable(context))
            item.setEnabled(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery: {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Gallery_Main.class);
                ArrayList<Apex> fullApex = new ArrayList<>();
                fullApex.addAll(full);
                intent.putParcelableArrayListExtra("fullApex", fullApex);
                startActivity(intent);
                return true;
            }
            case R.id.authors: {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.aboutauthors);
                TextView authors = (TextView) findViewById(R.id.authors);
                dialog.show();
                return true;
            }
            case R.id.quantity: {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.quantitynews);
                edit = (EditText) dialog.findViewById(R.id.edit);
                Button button = (Button) dialog.findViewById(R.id.button);
                TextView textView = (TextView) findViewById(R.id.textView);
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        value = edit.getText().toString();
                        try {
                            int quantity = Integer.parseInt(value);
                            if (quantity <= 0 || quantity > 200) {
                                Toast.makeText(getApplicationContext(), "Your data is incorrect",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                editor.putInt(QUANTITY_IN_DB, quantity);
                                editor.apply();
                                finish();
                                startActivity(getIntent());
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "That is not a number",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ApexAsynTask extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... params) {
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

            JSONArray jArray;
            try {
                jArray = new JSONArray(resultJson);
                for (int i = 0; i < QUANTITY_OF_NEWS_IN_DB; i++) {
                    Apex apex = new Apex();
                    JSONObject jRealObject = jArray.getJSONObject(i);
                    String id = jRealObject.getString("id");
                    String updated_at = jRealObject.getString("updated_at");
                    fullIdJson.add(id);
                    if (newsIdInDB.contains(id)) {
                        if (updated_at.equals(id_updated.get(id))) {
                            continue;
                        } else {
                            apex.setIdNews(id);
                            getApexValuesJson(apex, jRealObject);
                            apex.setUpdated_at(updated_at);
                            db.updateApex(apex);
                        }
                    } else {
                        apex.setIdNews(id);
                        getApexValuesJson(apex, jRealObject);
                        apex.setUpdated_at(updated_at);
                        full.add(apex);
                        db.addApex(apex);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            db.checkOnUpdates(fullIdJson);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            if (pdia.isShowing()) pdia.dismiss();

            setAdapterToMain();
        }
    }

    protected Bitmap downloadBitmap(String path) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();

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

    private String saveToInternalStorage(Bitmap bitmapImage, String id) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, id + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
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

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void getApexValuesJson(Apex apex, JSONObject jRealObject) {
        try {
            apex.setTitle(jRealObject.getString("title"));
            apex.setPhoto(jRealObject.getString("photo"));
            apex.setContent(jRealObject.getString("content"));
            apex.setImagePath(saveToInternalStorage(downloadBitmap(jRealObject.getString("photo")), "image" + jRealObject.getString("title")));
            apex.setUrl(jRealObject.getString("url"));
            apex.setFeatured(jRealObject.getString("featured"));
            apex.setMain(jRealObject.getString("main"));
            apex.setCreated_at(jRealObject.getString("created_at"));
        } catch (JSONException e) {
            Log.d("json", e.getMessage());
        }
    }

    public void setAdapterToMain() {
        db = new ApexSqlliteHelper(this);
        ArrayList<Apex> fullMain = new ArrayList<>();
        ArrayList<Apex> fetched = db.getFetchedNews();
        ArrayList<Apex> mainNotFetched = db.getMainMotFetchedNews();
        fullMain.addAll(fetched);
        fullMain.addAll(mainNotFetched);
        ApexAdapter adapter = new ApexAdapter(context, R.layout.row, fullMain);
        list.setAdapter(adapter);
    }

    public static boolean isFirst(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if (first) {
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }

   /* public static boolean isOnline(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }*/


}


