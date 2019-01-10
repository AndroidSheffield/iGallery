/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.nexus.igallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.nexus.igallery.database.PhotoData;

import java.util.Date;

public class ShowImageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng googleLatLng;//Locations of photos
    private TextView title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();

        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                ImageView imageView = (ImageView) findViewById(R.id.image);
                PhotoData element= MyAdapter.getItems().get(position);

                googleLatLng = new LatLng(element.getLat(),element.getLon());

                title = findViewById(R.id.info_title);
                title.setText(element.getTitle());

                description = findViewById(R.id.info_description);
                description.setText(element.getDescription());

                Bitmap myBitmap = BitmapFactory.decodeFile(element.getPhotoPath());
                imageView.setImageBitmap(myBitmap);

            }

        }

        Button btn = findViewById(R.id.buttonEdit);
        final int finalPosition = position;
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ShowImageActivity.this, EditActivity.class);
                intent.putExtra("position", finalPosition);
                startActivity(intent);

            }

        });

    }

    //Open location in the external Google Map
    public void myClick(View v){
        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                PhotoData element= MyAdapter.getItems().get(position);

                //Send location to API
                Uri locationUri = Uri.parse("geo:" + element.getLat() + "," + element.getLon());
                Intent locationintent = new Intent(Intent.ACTION_VIEW, locationUri);
                locationintent.setPackage("com.google.android.apps.maps");
                //context.startActivity(locationintent);
                startActivity(locationintent);
            }
        }
    }

    //Open location in the internal Google Map
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                PhotoData element= MyAdapter.getItems().get(position);
                googleLatLng = new LatLng(element.getLat(),element.getLon());
            }
        }

        //Add Marker
        googleMap.addMarker(new MarkerOptions().position(googleLatLng).title("Marker in Googleplex"));
        //Move camera to the location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, 13));

    }

}
