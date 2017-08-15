/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    ProgressDialog progressDialog;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        try{
            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            if (earthquakeListView != null) {
                earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Earthquake entry = (Earthquake) parent.getAdapter().getItem(position);
                        String url = entry.getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
            else{
                Toast.makeText(EarthquakeActivity.this, "Unable to set onItemClickListener for list items", Toast.LENGTH_LONG).show();
            }
        }
        catch (NullPointerException e){
            Log.e(LOG_TAG, "Problem in setting onItemClickListener for list items", e);
        }


        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);


    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: make progressDialog to show completion percentage
            progressDialog = new ProgressDialog(EarthquakeActivity.this);
            progressDialog.setTitle("Getting earthquake data");
            progressDialog.setMessage("Downloading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<Earthquake> doInBackground(String... urls) {

            //return null if there is no URL
            if(urls.length < 1 || urls[0] == null){
                return null;
            }

            // Perform the HTTP request for earthquake data and process the response.

            return QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL);
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            // Update the information displayed to the user.
            if(earthquakes == null){
                return;
            }
            updateUI(earthquakes);
            progressDialog.dismiss();
        }
    }

    void updateUI(List<Earthquake> earthquakes){
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(this,earthquakes);
        try{
            if (earthquakeListView != null) {
                earthquakeListView.setAdapter(earthquakeAdapter);
            }
            else{
                Toast.makeText(EarthquakeActivity.this, "Unable to set onItemClickListener for list items", Toast.LENGTH_LONG).show();
            }
        }
        catch (NullPointerException e){
            Toast.makeText(this, "Unable to set list adapter", Toast.LENGTH_SHORT).show();
        }
    }
}
