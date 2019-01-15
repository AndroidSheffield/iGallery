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
 *
 */


public class commonMethod {

    public static List<PhotoData> getAllPhotos(Activity activity, MyViewModel myViewModel, List<PhotoData> myPictureList) {



        List<PhotoData> photoDataList = new ArrayList<>();

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projImage = {MediaStore.Images.Media.DATA};
        Cursor mCursor = activity.getContentResolver().query(mImageUri, projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor != null) {

            while (mCursor.moveToNext()) {

                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                try {
                    ExifInterface exif = new ExifInterface(path);
                    Date date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(exif.getAttribute(ExifInterface.TAG_DATETIME));
                    if (myPictureList.size() == 0) {
                        float[] location = new float[2];
                        exif.getLatLong(location);
                        myViewModel.storePhoto(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                        photoDataList.add(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                    }
                    else {
                        for (PhotoData temp : myPictureList) {
                            if (temp.getPhotoPath().equals(path)) {
                                break;
                            }
                            else if (date.equals(temp.getCreateDate())) {
                                break;
                            }
                            else {
                                float[] location = new float[2];
                                exif.getLatLong(location);
                                myViewModel.storePhoto(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                                photoDataList.add(new PhotoData(path, Double.valueOf(location[0]), Double.valueOf(location[1]), date, date));
                            }
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

    public static void initEasyImage(Activity activity) {
        EasyImage.configuration(activity)
                .setImagesFolderName("iGallery")
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
    }



}
