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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.List;

public class BeaconAdapter extends ArrayAdapter<Beacon> {
    private static LayoutInflater inflater;

    public BeaconAdapter(Context context, int resource, List<Beacon> beacons) {
        super(context, resource, beacons);

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.major = (TextView)view.findViewById(R.id.major);
            holder.minor = (TextView)view.findViewById(R.id.minor);
            holder.uuid = (TextView)view.findViewById(R.id.uuid);
            holder.distance = (TextView)view.findViewById(R.id.distance);
            holder.rssi = (TextView)view.findViewById(R.id.rssi);

            view.setTag(holder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Beacon b = this.getItem(position);

        viewHolder.major.setText("Major: " + b.getId2().toString());
        viewHolder.minor.setText("Minor: " + b.getId3().toString());
        viewHolder.uuid.setText(b.getId1().toString());
        viewHolder.distance.setText(String.format("%f meters", b.getDistance()));
        viewHolder.rssi.setText(b.getRssi() + "");

        return view;
    }

    public static class ViewHolder{
        public TextView major;
        public TextView minor;
        public TextView uuid;
        public TextView distance;
        public TextView rssi;
    }
}
