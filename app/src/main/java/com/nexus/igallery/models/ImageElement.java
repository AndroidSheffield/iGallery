package com.nexus.igallery.models;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.File;
import java.util.Date;

/**
 * The class implements the ClusterItem class which work as a temp entity
 * when user try to use photo map interface
 * @see com.nexus.igallery.views.GalleryMapActivity
 * @since iGallery version 1.0
 */
public class ImageElement implements ClusterItem {

    public double lat;
    public double lon;
    public PhotoData photoData;
    private final LatLng mPosition;

    /**
     * constructor to initiate the class instance
     * @param photoData the photo data which will be showed on the map
     * @see com.nexus.igallery.views.GalleryMapActivity
     * @since iGallery version 1.0
     */
    public ImageElement(PhotoData photoData) {
        this.photoData = photoData;
        lat = photoData.getLat();
        lon = photoData.getLon();
        this.mPosition = new LatLng(lat, lon);
    }


    /**
     * provide the photo location
     * @return a LatLng value which is the same as photo data location
     */
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
