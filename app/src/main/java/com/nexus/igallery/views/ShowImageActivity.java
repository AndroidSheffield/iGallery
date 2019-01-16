/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.nexus.igallery.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nexus.igallery.R;
import com.nexus.igallery.models.PhotoData;

import java.util.Date;
import java.util.Map;


public class ShowImageActivity extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * googleLatLng is used to save the locations of photos. It includes both longitude and latitude.
     * title and description are used to store description of photos.
     * element is used to cross data from the Class PhotoData.java
     */
    private LatLng googleLatLng;
    private TextView title, description;
    private PhotoData element;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate this module using for Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();

        int position = -1;
        if(b != null) {
            position = b.getInt("position");
            if (position!= -1){
                ImageView imageView = findViewById(R.id.image);

                element= MyAdapter.getItems().get(position);

                googleLatLng = new LatLng(element.getLat(),element.getLon());// Get longitude and latitude of photos

                title = findViewById(R.id.info_title);
                title.setText(element.getTitle());

                description = findViewById(R.id.info_description);
                description.setText(element.getDescription());

                Bitmap myBitmap = BitmapFactory.decodeFile(element.getPhotoPath());
                imageView.setImageBitmap(myBitmap);

            }

        }

        Button btn = findViewById(R.id.buttonEdit);
        /**
         * the button to realized the edit function
         */
        final int finalPosition = position;
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ShowImageActivity.this, EditActivity.class);
                /**
                 * using the monitor to open a new activity "EditActivity"
                 * to edit the metadata of picture
                 */
                intent.putExtra("position", finalPosition);
                startActivityForResult(intent, 10001);

            }

        });

    }

    /**
     * Open location in the external Google Map.
     * This method is related to the button "button1".
     * The position is conveyed from PhotoData Class to locationUri.
     * It must be modified to a particular format in locationintent,
     * and then use Intent to send it to the API.
     */
    public void myClick(View v){
        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                PhotoData element= MyAdapter.getItems().get(position);

                Uri locationUri = Uri.parse("geo:" + element.getLat() + "," + element.getLon());// A string of particular format use for the API
                Intent locationintent = new Intent(Intent.ACTION_VIEW, locationUri);// Send location to API
                locationintent.setPackage("com.google.android.apps.maps");
                //context.startActivity(locationintent);
                startActivity(locationintent);
            }
        }
    }

    /**
     * Open location in the internal Google Map.
     * Get location firstly, and then appear location and title in the map.
     * The camera must be moced to the location of photo, or it will always show the initial position.
     * Reference, Google Map developers document: https://developers.google.com/maps/documentation/android-sdk/intro
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle b = getIntent().getExtras();
        int position = -1;
        if(b != null) {
            position = b.getInt("position");
            if (position != -1){
                PhotoData element= MyAdapter.getItems().get(position);
                googleLatLng = new LatLng(element.getLat(),element.getLon());
            }
        }

        //Add Marker
        googleMap.addMarker(new MarkerOptions().position(googleLatLng).title("Marker in Googleplex"));
        //Move camera to the location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, 13));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            element.setUpdateDate((Date) bundle.get("new_date"));
            element.setTitle(String.valueOf(bundle.get("title")));
            element.setDescription(String.valueOf(bundle.get("description")));
            title.setText(element.getTitle());
            description.setText(element.getDescription());
            MyAdapter.changeItem((int) bundle.get("position"), element);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}