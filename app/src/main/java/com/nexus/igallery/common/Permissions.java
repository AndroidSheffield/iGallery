package com.nexus.igallery.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * The Permissions class aim to control the permission of app.
 * iGallery app need Location permission, read and write external storage permission of the phone.
 * @author Jingbo Lin, Ruocheng Ma
 * @see com.nexus.igallery.views.MainActivity
 * @since iGallery version 1.0
 */
public class Permissions {

    static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;

    /**
     * the method allow app ask for the location permission of phone
     * @param activity the activity (view) call this method
     * @see com.nexus.igallery.views.MainActivity
     * @since iGallery version 1.0
     */
    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    /**
     * the method aim to check the permission of app.
     * If app currently doesn't have read and write permission for phone will call it.
     * @param context the context of current activity
     * @param activity the activity (view) call this method
     * @see com.nexus.igallery.views.MainActivity
     * @since iGallery version 1.0
     */
    public static void checkPermissions(final Context context, Activity activity) {
        int currentAPIVersion = Build.VERSION.SDK_INT;

        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            requestPermission(activity);
        }
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            }

        }


    }
}
