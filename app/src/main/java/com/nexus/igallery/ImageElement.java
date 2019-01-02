/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.nexus.igallery;

import android.location.Location;

import java.io.File;
import java.util.Date;

class ImageElement {
    int image=-1;
    File file=null;
    double lat;
    double lon;
    Date date;

    public ImageElement(File fileX, double lat, double lon, Date date) {
        file= fileX;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
    }
}
