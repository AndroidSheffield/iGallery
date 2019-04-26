package com.nexus.igallery.views;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nexus.igallery.R;
import com.nexus.igallery.models.PhotoData;
import com.nexus.igallery.viewModels.MyViewModel;

import java.io.File;
import java.io.IOException;
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
import static com.nexus.igallery.common.CommonMethod.getAllPhotos;
import static com.nexus.igallery.common.CommonMethod.initEasyImage;
import static com.nexus.igallery.common.Permissions.checkPermissions;
import static com.nexus.igallery.common.Permissions.requestPermission;

/**
 * The MainActivity extends AppcompatActivity so it provide the interface
 * which is also the main interface and show the overview of photos.
 * This class also implements MyDialogFragment.MDFListener which allow activity
 * do the specific reaction toward the dialog fragment
 * @since iGallery version 1.0
 */
public class MainActivity extends AppCompatActivity implements MyDialogFragment.MDFListener {

    private List<PhotoData> myPictureList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private FusedLocationProviderClient client;
    private int floatingType = -1;
    private Activity activity;
    private MyViewModel myViewModel;

    /**
     * the method be called when open the app
     * @param savedInstanceState application current state
     * @since iGallery version 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;


        // required by Android 6.0 +
        checkPermissions(getApplicationContext(), this);
        client = LocationServices.getFusedLocationProviderClient(this);


        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);


        initData();


        initEasyImage(this);

        int numberOfColumns = 4;
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mAdapter = new MyAdapter(myPictureList);
        mRecyclerView.setAdapter(mAdapter);

        // observe the live data whenever it change renew the interface
        myViewModel.getPhotoDataToDisplay().observe(this, new Observer<PhotoData>() {
            @Override
            public void onChanged(@Nullable final PhotoData newValue) {

                mAdapter = new MyAdapter(myPictureList);
                mRecyclerView.setAdapter(mAdapter);

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                floatingType = 1;
                EasyImage.openCamera(getActivity(), 0);
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

    /**
     * the method will only be called when activity start a new intent and require result
     * @param requestCode the int parameter which set and provided by startActivityForResult() method
     * @param resultCode the int parameter which set and provided by setResult() method
     * @param data the intent data
     * @since iGallery version 1.0
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the resultCode and requestCode is correct
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
                    photoData = new PhotoData("",0.0, 0.0, sd, null);
                    if (!title.equals("")) {
                        if (!description.equals("")) {
                            photoData.setTitle("%" + title + "%");
                            photoData.setDescription("%" + description + "%");
                        }
                        else {
                            photoData.setTitle("%" + title + "%");
                            photoData.setDescription("%");
                        }

                    }
                    else if (!description.equals("")) {
                        photoData.setTitle("%");
                        photoData.setDescription("%" + description + "%");
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
                photoData = new PhotoData("",0.0, 0.0, null, null);
                if (!title.equals("")) {
                    if (!description.equals("")) {
                        photoData.setTitle("%" + title + "%");
                        photoData.setDescription("%" + description + "%");
                    }
                    else {
                        photoData.setTitle("%" + title + "%");
                        photoData.setDescription("%");
                    }

                }
                else if (!description.equals("")) {
                    photoData.setTitle("%");
                    photoData.setDescription("%" + description + "%");
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
                if (source == EasyImage.ImageSource.CAMERA) {
                    final File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());

                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }


    /**
     * add to the grid
     * @param returnedPhotos a list of selecting photo file or photo file which just was took
     * @since iGallery version 1.0
     */
    private void onPhotosReturned(final List<File> returnedPhotos) {
        if (floatingType == 1) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                requestPermission(this);

            }
            client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Date date = Calendar.getInstance().getTime();
                        PhotoData photoData = new PhotoData(returnedPhotos.get(0).getAbsolutePath(), location.getLatitude(), location.getLongitude(), date, date);
                        myViewModel.storePhoto(photoData);
                        myPictureList.add(photoData);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(returnedPhotos.size() - 1);
                    }

                }
            });


        }
        else {

            myPictureList.addAll(getPhotoDatas(returnedPhotos));
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(returnedPhotos.size() - 1);

        }



    }

    /**
     * given a list of photos, check if it's duplicated photo and store it into database
     * @param returnedPhotos a list of selecting photo file or photo file which just was took
     * @return a list which store PhotoData instance
     * @since iGallery version 1.0
     */
    private List<PhotoData> getPhotoDatas(List<File> returnedPhotos) {
        List<PhotoData> photoDataList= new ArrayList<>();
        int duplicatedPhoto = 0;
        int compared = 0;
        for (File file: returnedPhotos){
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(file.getAbsolutePath());
                Date date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(exif.getAttribute(ExifInterface.TAG_DATETIME));
                for (PhotoData photoData : myPictureList) {
                    if (photoData.getCreateDate().equals(date)) {
                        duplicatedPhoto++;
                        break;
                    }
                }
                if (compared < duplicatedPhoto) {
                    compared++;
                    continue;
                }
                float[] location = new float[2];
                exif.getLatLong(location);

                PhotoData element= new PhotoData(file.getAbsolutePath(), Double.valueOf(location[0]), Double.valueOf(location[1]), date, date);
                myViewModel.storePhoto(element);
                photoDataList.add(element);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        if (duplicatedPhoto > 0) {
            Toast.makeText(this, duplicatedPhoto + " duplicated photos", Toast.LENGTH_SHORT).show();
        }

        return photoDataList;
    }


    /**
     * initiate the menu
     * @param menu toolbar menu
     * @return true if there is a xml about menu
     * @since iGallery version 1.0
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Set the specific action for each menu element
     * @param item menu elements
     * @return true
     * @since iGallery version 1.0
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_photo:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 10086);
                return true;
            case R.id.refresh:
                initData();
                mAdapter = new MyAdapter(myPictureList);
                mRecyclerView.setAdapter(mAdapter);
                return true;
            case R.id.synchronize:
                // required by Android 6.0 +
                checkPermissions(getApplicationContext(), this);
                MyDialogFragment sync = new MyDialogFragment().newInstance("Synchronize", "Are you sure to sync all photos from local gallery?", "1");
                FragmentManager fragmentManager = getSupportFragmentManager();
                sync.show(fragmentManager, "fragment_sync");
                return true;
            case R.id.display_map:
                Intent mapView =  new Intent(MainActivity.this, GalleryMapActivity.class);
                startActivity(mapView);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    /**
     * react specific to dialog fragment
     * @param message int value provided by MyDialogFrament
     * @since iGallery version 1.0
     */
    @Override
    public void getDataFromDialog(int message) {
        if (message == 1) {
            myPictureList.addAll(getAllPhotos(getActivity(), myViewModel));
            mAdapter.notifyDataSetChanged();
            mAdapter = new MyAdapter(myPictureList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * initiate display list
     * @since iGallery version 1.0
     */
    private void initData() {

        myPictureList = myViewModel.getAllPhotoDataToDisplay();
    }

    /**
     * getter of Activity
     * @return current Activity
     * @since iGallery version 1.0
     */
    public Activity getActivity() {
        return activity;
    }

}

