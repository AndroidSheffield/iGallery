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

/**
 * This class aim to realized the function of showing the image for a grid
 * and it can be used to acquire map function and edit function.
 * The using permission has been allocated by Prof.Fabio
 * @author Ruocheng Ma
 * @author Jiachen Yang
 * @author Jingbo Lin
 * @since iGallery version 1.0
 */
public class ShowImageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng googleLatLng;
    private TextView title, description;
    private PhotoData element;

    /**
     * the method be called when open the app
     * @param savedInstanceState application current state
     * @since iGallery version 1.0
     */
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
     * This method is related to the button "button_map".
     * The position is conveyed from PhotoData Class to locationUri.
     * It must be modified to a particular format in locationintent,
     * and then use Intent to send it to the API.
     * @since iGallery version 1.0
     */
    public void myClick(View v){
        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b != null) {
            position = b.getInt("position");
            if (position!=-1){
                PhotoData element= MyAdapter.getItems().get(position);

                // A string of particular format use for the API
                Uri locationUri = Uri.parse("geo:" + element.getLat() + "," + element.getLon());
                // Send location to API
                Intent locationintent = new Intent(Intent.ACTION_VIEW, locationUri);
                locationintent.setPackage("com.google.android.apps.maps");
//                context.startActivity(locationintent);
                startActivity(locationintent);
            }
        }
    }

    /**
     * Open location in the internal Google Map.
     * Get location firstly, and then appear location and title in the map.
     * The camera must be moved to the location of photo, or it will always show the initial position.
     * Reference, Google Map developers document: https://developers.google.com/maps/documentation/android-sdk/intro
     * @since iGallery version 1.0
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

        // Add Marker
        googleMap.addMarker(new MarkerOptions().position(googleLatLng).title("Marker in Googleplex"));
        // Move camera to the location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, 13));

    }


    /**
     * output the information of photo
     * include the "title" and "description"
     * provide the button to edit the metadata about the photo
     * @param requestCode request the data
     * @param resultCode get the result code
     * @param data the data
     * @since iGallery version 1.0
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            element.setUpdateDate((Date) bundle.get("new_date"));
            element.setTitle(String.valueOf(bundle.get("title")));
            element.setDescription(String.valueOf(bundle.get("description")));
            title.setText(element.getTitle());
            //using the database management to get information of title
            description.setText(element.getDescription());
            //using the database management to get information of description
            MyAdapter.changeItem((int) bundle.get("position"), element);
        }
    }
    /**
     * set an icon before title at toolbar which allow going back to the previous page
     * @param item menu elements
     * @return true
     * @since iGallery version 1.0
     */
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