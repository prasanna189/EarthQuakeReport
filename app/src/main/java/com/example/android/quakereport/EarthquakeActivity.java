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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
//        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
//        earthquakes.add(new Earthquake("4.5","San Francisco","Aug 4, 2017"));
//        earthquakes.add(new Earthquake("5.1","Londoon","Aug 1, 2017"));
//        earthquakes.add(new Earthquake("7.3","Tokyo","Feb 14, 2017"));
//        earthquakes.add(new Earthquake("3.7","Mexico City","Jan 24, 2017"));
//        earthquakes.add(new Earthquake("2.5","Moscow","May 10, 2017"));
//        earthquakes.add(new Earthquake("4.3","Rio de Janeiro","Jun 4, 2017"));
//        earthquakes.add(new Earthquake("1.8","Paris","Jul 9, 2017"));

        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();




        // Create a new {@link ArrayAdapter} of earthquakes
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, earthquakes);

        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(this,earthquakes);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                Earthquake entry = (Earthquake) parent.getAdapter().getItem(position);
                String url = entry.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        try{
            earthquakeListView.setAdapter(earthquakeAdapter);
        }
        catch (NullPointerException e){
            Toast.makeText(this, "Unable to set list adapter", Toast.LENGTH_SHORT).show();
        }

    }
}
