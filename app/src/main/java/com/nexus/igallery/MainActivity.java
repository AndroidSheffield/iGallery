package com.nexus.igallery;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nexus.igallery.database.PhotoData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.nexus.igallery.commonMethod.getAllPhotos;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.MDFListener {

    static final int REQUEST_READ_EXTERNAL_STORAGE = 2987;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 7829;
    private static final String PHOTOS_KEY = "easy_image_photos_list";
    private List<PhotoData> myPictureList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private FusedLocationProviderClient client;
    private int floatingType = -1;

    private Activity activity;

    private MyViewModel myViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions();
        client = LocationServices.getFusedLocationProviderClient(this);


        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);


        initData();
        activity = this;

        int numberOfColumns = 4;
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mAdapter = new MyAdapter(myPictureList);
        mRecyclerView.setAdapter(mAdapter);
        myViewModel.getPhotoDataToDisplay().observe(this, new Observer<PhotoData>() {
            @Override
            public void onChanged(@Nullable final PhotoData newValue) {

                mAdapter = new MyAdapter(myPictureList);
                mRecyclerView.setAdapter(mAdapter);

            }
        });

        // required by Android 6.0 +
        checkPermissions(getApplicationContext());

        initEasyImage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingType = 1;
                EasyImage.openCameraForImage(getActivity(), 0);
            }
        });

        FloatingActionButton fabGallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingType = 0;
                EasyImage.openGallery(getActivity(), 0);
            }
        });
    }

    private void initEasyImage() {
        EasyImage.configuration(this)
                .setImagesFolderName("iGallery")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);
    }

    private void initData() {

        myPictureList = myViewModel.getAllPhotoDataToDisplay();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void checkPermissions(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                }

            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Writing external storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                }

            }


        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if ((resultCode == Activity.RESULT_OK) && requestCode == 10086) {
            Bundle bundle = data.getExtras();
            PhotoData photoData;
            String startDate = String.valueOf(bundle.get("search_startDate"));
            String endDate = String.valueOf(bundle.get("search_endDate"));
            String title = String.valueOf(bundle.get("title"));
            String description = String.valueOf(bundle.get("description"));
            if (!startDate.equals("")) {

                try {
                    Date sd = new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(startDate);
                    photoData = new PhotoData("",0.0, 0.0, sd);
                    if (!title.equals("")) {
                        if (!data.getStringExtra("description").equals("")) {
                            photoData.setTitle("%" + data.getStringExtra("title") + "%");
                            photoData.setDescription("%" + data.getStringExtra("description") + "%");
                        }
                        else {
                            photoData.setTitle("%" + data.getStringExtra("title") + "%");
                            photoData.setDescription("%");
                        }

                    }
                    else if (!description.equals("")) {
                        photoData.setTitle("%");
                        photoData.setDescription("%" + data.getStringExtra("description") + "%");
                    }

                    if (!endDate.equals("")) {
                        photoData.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(endDate));
                    }
                    else {
                        photoData.setUpdateDate(new Date());
                    }
                    myPictureList = myViewModel.searchPhotoDataToDisplay(photoData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            else {
                photoData = new PhotoData("",0.0, 0.0, null);
                if (!title.equals("")) {
                    if (!data.getStringExtra("description").equals("")) {
                        photoData.setTitle("%" + data.getStringExtra("title") + "%");
                        photoData.setDescription("%" + data.getStringExtra("description") + "%");
                    }
                    else {
                        photoData.setTitle("%" + data.getStringExtra("title") + "%");
                        photoData.setDescription("%");
                    }

                }
                else if (!description.equals("")) {
                    photoData.setTitle("%");
                    photoData.setDescription("%" + data.getStringExtra("description") + "%");
                }
                if (!endDate.equals("")) {
                    try {
                        photoData.setCreateDate(new Date(1900, 01, 01));
                        photoData.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(endDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                myPictureList = myViewModel.searchPhotoDataToDisplay(photoData);
            }


            mAdapter.notifyDataSetChanged();
            mAdapter = new MyAdapter(myPictureList);
            mRecyclerView.setAdapter(mAdapter);
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(final List<File> imageFiles, EasyImage.ImageSource source, int type) {


                onPhotosReturned(imageFiles);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    final File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());

                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }




    /**
     * add to the grid
     * @param returnedPhotos
     */
    private void onPhotosReturned(final List<File> returnedPhotos) {
        if (floatingType == 1) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions();

            }
            client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        ImageElement temp = new ImageElement(returnedPhotos.get(0), location.getLatitude(), location.getLongitude(), Calendar.getInstance().getTime());
                        myViewModel.storePhoto(temp);
                        myPictureList.add(new PhotoData(temp.file.getAbsolutePath(), temp.lat, temp.lon, temp.date));
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(returnedPhotos.size() - 1);
                    }

                }
            });


        }
        else {


            myPictureList.addAll(getImageElements(returnedPhotos));

            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(returnedPhotos.size() - 1);

        }



    }

    /**
     * given a list of photos, it creates a list of myElements
     * @param returnedPhotos
     * @return
     */
    private List<PhotoData> getImageElements(List<File> returnedPhotos) {
        List<PhotoData> imageElementList= new ArrayList<>();
        for (File file: returnedPhotos){
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(file.getAbsolutePath());

                float[] location = new float[2];
                exif.getLatLong(location);
                try {
                    Date date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(exif.getAttribute(ExifInterface.TAG_DATETIME));

                    ImageElement temp = new ImageElement(file, Double.valueOf(location[0]), Double.valueOf(location[1]), date);
                    myViewModel.storePhoto(temp);
                    PhotoData element= new PhotoData(file.getAbsolutePath(), temp.lat, temp.lon, temp.date);
                    imageElementList.add(element);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // exif Datetime need to be used

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        return imageElementList;
    }

    public Activity getActivity() {
        return activity;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_photo:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 10086);

                return true;
            case R.id.synchronize:
                MyDialogFragment sync = new MyDialogFragment().newInstance("Synchronize", "Are you sure to sync all photos from local gallery?", "1");
                FragmentManager fragmentManager = getSupportFragmentManager();
                sync.show(fragmentManager, "fragment_sync");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    public void getDataFromDialog(int message) {
        if (message == 1) {
            myPictureList.addAll(getAllPhotos(getActivity(), myViewModel, myPictureList, REQUEST_READ_EXTERNAL_STORAGE));
            mAdapter.notifyDataSetChanged();
            mAdapter = new MyAdapter(myPictureList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

