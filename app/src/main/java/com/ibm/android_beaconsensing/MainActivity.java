/**
 * Copyright (c) 2016 IBM Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.ibm.android_beaconsensing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ibm.pisdk.PIAPIAdapter;
import com.ibm.pisdk.PIBeaconSensor;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PIBeaconSensor.BeaconsInRangeListener {

    // Settings vars
    public static String TENANT;
    public static String ORG;
    public static String USERNAME;
    public static String PASSWORD;

    // Beacon vars
    PIBeaconSensor mBeaconSensor;

    // XML
    Button start;
    Button stop;
    ListView beaconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get UI elements
        start = (Button) findViewById(R.id.startButton);
        stop = (Button) findViewById(R.id.stopButton);
        beaconList = (ListView) findViewById(R.id.beaconList);

        // Use default preference settings
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        TENANT = prefs.getString("pref_tenant", null);
        ORG = prefs.getString("pref_org", null);
        USERNAME = prefs.getString("pref_username", null);
        PASSWORD = prefs.getString("pref_password", null);

        PIAPIAdapter mAdapter = new PIAPIAdapter(this, USERNAME, PASSWORD, "https://presenceinsights.ibmcloud.com", TENANT, ORG);

        mBeaconSensor = PIBeaconSensor.getInstance(this, mAdapter);

        // adding beacon layout for iBeacons
        mBeaconSensor.addBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");

        mBeaconSensor.setBeaconsInRangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent  = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startSensing(View v) {
        start.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);
        mBeaconSensor.start();
    }

    public void stopSensing(View v) {
        beaconList.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        mBeaconSensor.stop();
    }

    @Override
    public void beaconsInRange(ArrayList<Beacon> arrayList) {
        if (!arrayList.isEmpty()) {
            List<Beacon> beacons = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++)  {
                beacons.add(arrayList.get(i));
            }
            beaconList.setAdapter(new BeaconAdapter(this, R.layout.list, beacons));
            beaconList.setVisibility(View.VISIBLE);
        }

    }
}
