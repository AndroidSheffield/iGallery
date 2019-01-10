/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.nexus.igallery;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.nexus.igallery.database.PhotoData;

import java.io.File;
import java.util.Date;

class ImageElement implements ClusterItem {
    int image=-1;
    File file=null;
    double lat;
    double lon;
    Date date;
    String title;
    String description;
    PhotoData photoData;
    private final LatLng mPosition;




    public ImageElement(File fileX, double lat, double lon, Date date) {
        file= fileX;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.mPosition = new LatLng(lat, lon);
    }



    public ImageElement(PhotoData photoData) {
        this.photoData = photoData;
        lat = photoData.getLat();
        lon = photoData.getLon();
        this.mPosition = new LatLng(lat, lon);
    }


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
