package com.nexus.igallery.common;

import android.app.Activity;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.nexus.igallery.models.PhotoData;
import com.nexus.igallery.viewModels.MyViewModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * The class provide the static method for views
 * which allow app retrieve all photos from local gallery and store these in database.
 * Meanwhile defining the initial function to config the EasyImage
 * @see com.nexus.igallery.views.MainActivity
 * @since iGallery version 1.0
 */
public class CommonMethod {

    /**
     * The method would check the local gallery and retrieve all photos
     * but only store the photos which are still unavailable.
     * This method will return a list which will use by new adapter
     * @param activity the activity (view) call this method
     * @param myViewModel the viewModel which is used at the specific activity (previous param)
     * @return a list which stores PhotoData instance
     * @see com.nexus.igallery.views.MainActivity
     * @since iGallery version 1.0
     */
    public static List<PhotoData> getAllPhotos(Activity activity, MyViewModel myViewModel) {

        List<PhotoData> currentList = myViewModel.getAllPhotoDataToDisplay();
        List<PhotoData> photoDataList = new ArrayList<>();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projImage = {MediaStore.Images.Media.DATA};
        Cursor mCursor = activity.getContentResolver().query(mImageUri, projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor != null) {
            // retrieve the local photos through Cursor
            while (mCursor.moveToNext()) {

                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                try {

                    ExifInterface exif = new ExifInterface(path);
                    Date date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(exif.getAttribute(ExifInterface.TAG_DATETIME));

                    // check if current picture list from database is empty,
                    // if so, store all photo data from local gallery into database and add these into return list
                    if (currentList.size() == 0) {
                        float[] location = new float[2];
                        exif.getLatLong(location);
                        myViewModel.storePhoto(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                        photoDataList.add(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                    }
                    else { // if no, only store the photo which is unavailable for database
                        int isDuplicated = 0;
                        for (PhotoData temp : currentList) {
                            if (temp.getPhotoPath().equals(path) || date.equals(temp.getCreateDate())) {
                                isDuplicated = 1;
                                break;
                            }
                        }
                        if (isDuplicated == 0) {
                            float[] location = new float[2];
                            exif.getLatLong(location);
                            myViewModel.storePhoto(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                            photoDataList.add(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            mCursor.close();
        }

        return photoDataList;
    }

    /**
     * Config the EasyImage for provided activity
     * @param activity
     * @since iGallery version 1.0
     */
    public static void initEasyImage(Activity activity) {
        EasyImage.configuration(activity)
                .setImagesFolderName("iGallery")
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
    }

}
