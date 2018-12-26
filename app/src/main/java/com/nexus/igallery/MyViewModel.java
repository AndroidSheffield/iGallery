package com.nexus.igallery;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.nexus.igallery.database.PhotoData;

import java.util.ArrayList;
import java.util.List;


public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    LiveData<PhotoData> photoDataToDisplay;
    List<PhotoData> allPhotoDataToDisplay;

    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
        photoDataToDisplay = mRepository.getPhotoData();
        allPhotoDataToDisplay = mRepository.getPhotoAllData();
    }


    /**
     * getter for the live data
     * @return
     */
    LiveData<PhotoData> getPhotoDataToDisplay() {
        if (photoDataToDisplay == null) {
            photoDataToDisplay = new MutableLiveData<PhotoData>();
        }
        return photoDataToDisplay;
    }

    List<PhotoData> getAllPhotoDataToDisplay() {
        allPhotoDataToDisplay = mRepository.getPhotoAllData();
        if (allPhotoDataToDisplay == null) {
            allPhotoDataToDisplay = new ArrayList<PhotoData>();
        }
        return allPhotoDataToDisplay;
    }

    public void storePhoto(ImageElement imageElement) {
        mRepository.storePhoto(imageElement);
    }
}
