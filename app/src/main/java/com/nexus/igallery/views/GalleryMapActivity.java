package com.nexus.igallery.views;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nexus.igallery.R;
import com.nexus.igallery.models.ImageElement;
import com.nexus.igallery.common.MultiDrawable;
import com.nexus.igallery.models.PhotoData;
import com.nexus.igallery.viewModels.MyViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * This class learned from demo of Google Maps Android API Utility Library.
 * The using permission has been allocated by Prof.Fabio
 * Demonstrates heavy customisation of the look of rendered clusters.
 * @see MainActivity
 * @since iGallery version 1.0
 */
public class GalleryMapActivity extends FragmentActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<ImageElement>, ClusterManager.OnClusterInfoWindowClickListener<ImageElement>, ClusterManager.OnClusterItemClickListener<ImageElement>, ClusterManager.OnClusterItemInfoWindowClickListener<ImageElement> {
    private GoogleMap mMap;
    private ClusterManager<ImageElement> mClusterManager;
    private MyViewModel myViewModel;
    private List<PhotoData> myPictureList = new ArrayList<>();

    /**
     * the method be called when clicking Map button at menu at first time
     * @param savedInstanceState application current state
     * @since iGallery version 1.0
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myPictureList = myViewModel.getAllPhotoDataToDisplay();
        setUpMap();
    }

    /**
     * the method be called when application go back to photos map
     * @since iGallery version 1.0
     */
    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    /**
     * initiate the google map
     * @since iGallery version 1.0
     */
    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

    }

    /**
     * config the google map
     * @param googleMap the GoogleMap instance which has specific configuration by developer
     * @since iGallery version 1.0
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        // request the permission of phone's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        }


        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        startMap();
    }

    /**
     * run the map
     * @since iGallery version 1.0
     */
    public void startMap() {

        // if photo data of this app isn't empty, map's beginning location would be the location of first photo
        if (myPictureList.size() != 0) {
            PhotoData photoData = myPictureList.get(0);
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(photoData.getLat(), photoData.getLon()), 9.5f));
        }
        else { // otherwise the location would be a specific location
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.7412, -2.02407), 9.5f));
        }


        // config the map and cluster manager
        mClusterManager = new ClusterManager<ImageElement>(this, getMap());
        mClusterManager.setRenderer(new ImageRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
    }

    /**
     * add items into cluster manager
     * @since iGallery version 1.0
     */
    private void addItems() {
        for (PhotoData photoData : myPictureList) {
            mClusterManager.addItem(new ImageElement(photoData));
        }

    }

    /**
     * manipulate the map
     * @return a GoogleMap instance which is also the attribute of class
     * @since iGallery version 1.0
     */
    public GoogleMap getMap() {
        return mMap;
    }

    /**
     * the method will call when user click an cluster
     * @param cluster a cluster which store InmageElement instance
     * @return true
     * @since iGallery version 1.0
     */
    @Override
    public boolean onClusterClick(Cluster<ImageElement> cluster) {
        String firstName = cluster.getItems().iterator().next().photoData.getTitle();
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<ImageElement> cluster) {

    }

    @Override
    public boolean onClusterItemClick(ImageElement imageElement) {

        return false;
    }

    /**
     * the method will call when user click an image on the map
     * and show the title and description of image
     * @param imageElement a specific image instance
     * @since iGallery version 1.0
     */
    @Override
    public void onClusterItemInfoWindowClick(ImageElement imageElement) {
        int position = -1;
        for (PhotoData photoData : MyAdapter.getItems()) {
            // find out the photo data index in the MyAdapter item list
            if (imageElement.photoData.getPhotoPath().equals(photoData.getPhotoPath())) {
                position = MyAdapter.getItems().indexOf(photoData);
                break;
            }
        }
        // when click the the specific window of image would jump to show imgae interface
        if (position != -1) {
            Intent intent = new Intent(this, ShowImageActivity.class);
            intent.putExtra("position", position);
            this.startActivity(intent);
        }

    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     * @since iGallery version 1.0
     */
    private class ImageRenderer extends DefaultClusterRenderer<ImageElement> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;
        public ImageRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.map_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        /**
         * define the specific window of image
         * @param image the item which will have window and should be ImageElement type
         * @param markerOptions marker of map
         * @since iGallery version 1.0
         */
        @Override
        protected void onBeforeClusterItemRendered(ImageElement image, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            Bitmap myBitmap = BitmapFactory.decodeFile(image.photoData.getPhotoPath());
            mImageView.setImageBitmap(myBitmap);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(image.photoData.getTitle() + ": " + image.photoData.getDescription());

        }

        /**
         * define the window for cluster
         * @param cluster the cluster of close photos
         * @param markerOptions marker of map
         * @since iGallery version 1.0
         */
        @Override
        protected void onBeforeClusterRendered(Cluster<ImageElement> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (ImageElement i : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = new BitmapDrawable(i.photoData.getPhotoPath());
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
