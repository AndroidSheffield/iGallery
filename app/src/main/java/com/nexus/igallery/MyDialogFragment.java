package com.nexus.igallery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import com.nexus.igallery.database.PhotoData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.nexus.igallery.MainActivity.REQUEST_READ_EXTERNAL_STORAGE;

public class MyDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((String) getArguments().get("title"));
        builder.setMessage((String) getArguments().get("content"))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (((String) getArguments().get("type")).equals("1")) {
                            getAllPhotos();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }


                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public MyDialogFragment newInstance(String title, String content, String type) {
        MyDialogFragment frag = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }


    public void getAllPhotos() {
        MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        List<PhotoData> myPictureList = myViewModel.getAllPhotoDataToDisplay();

        List<PhotoData> photoDataList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);

        }
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projImage = {MediaStore.Images.Media.DATA};
        Cursor mCursor = getActivity().getContentResolver().query(mImageUri, projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                try {
                    ExifInterface exif = new ExifInterface(path);
                    Date date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(exif.getAttribute(ExifInterface.TAG_DATETIME));
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
                            myViewModel.storePhoto(new ImageElement(new File(path), Double.valueOf(location[0]), Double.valueOf(location[1]), date));
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


    }




}
