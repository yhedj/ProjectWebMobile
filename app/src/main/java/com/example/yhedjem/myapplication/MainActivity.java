package com.example.yhedjem.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(this);

        new GetStations().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent it = new Intent(this.getApplicationContext(), DetailActivity.class);
        TextView tv = view.findViewById(R.id.address);
        String name = tv.getText().toString();
        String msg = tv.getText().toString();
        it.putExtra("name", name );
        it.putExtra("message", msg );
        startActivity(it);


    }

    private class GetStations extends AsyncTask<String, Integer,  ArrayList<HashMap<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }
        @Override
        protected  ArrayList<HashMap<String, String>> doInBackground(String... tab) {
            ArrayList<HashMap<String, String>> stationList = new ArrayList<>();

            HttpHandler sh = new HttpHandler();
// Making a request to url and getting response
            String url = "https://download.data.grandlyon.com/ws/rdata/jcd_jcdecaux.jcdvelov/all.json?maxfeatures=10";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
// Getting JSON Array node
                    JSONArray stations = jsonObj.getJSONArray("values");
// looping through All Stations
                    for (int i = 0; i < stations.length(); i++) {
                        JSONObject c = stations.getJSONObject(i);
                        String status = c.getString("status");
                        String name = c.getString("name");
                        String address = c.getString("address");
// tmp hash map for single station
                        HashMap<String, String> station = new HashMap<>();

// adding each child node to HashMap key => value
                        station.put("status", status);
                        station.put("name", name);
                        station.put("address", address);
// adding station to station list
                        stationList.add(station);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return stationList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> stationList) {
            super.onPostExecute(stationList);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, stationList,
                    R.layout.list_item, new String[]{ "name","address","status"},
                    new int[]{R.id.address, R.id.address, R.id.status});
            lv.setAdapter(adapter);
        }
    }
}
