package com.nexus.igallery.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.nexus.igallery.models.PhotoData;

import java.util.ArrayList;
import java.util.List;

/**
 * The class extends AndroidViewModel and associate with MyRepository to manipulate the database
 * @author Jingbo Lin
 * @see com.nexus.igallery.views.EditActivity
 * @see com.nexus.igallery.views.GalleryMapActivity
 * @see com.nexus.igallery.views.MainActivity
 * @since iGallery version 1.0
 */
public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    LiveData<PhotoData> photoDataToDisplay;
    List<PhotoData> allPhotoDataToDisplay;
    List<PhotoData> searchDataToDisplay;

    /**
     * constructor to initiate the MyViewModel instance
     * @param application current Application
     * @since iGallery version 1.0
     */
    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
        photoDataToDisplay = mRepository.getPhotoData();
        allPhotoDataToDisplay = mRepository.getPhotoAllData();
    }


    /**
     * getter for the live data
     * @return a LiveData with a random PhotoData retrieved from database
     * @see com.nexus.igallery.views.MainActivity
     * @since iGallery version 1.0
     */
    public LiveData<PhotoData> getPhotoDataToDisplay() {
        if (photoDataToDisplay == null) {
            photoDataToDisplay = new MutableLiveData<>();
        }
        return photoDataToDisplay;
    }

    /**
     * getter for the whole photo data from the database
     * @return a list which store PhotoData instance
     * @see com.nexus.igallery.common.CommonMethod
     * @see com.nexus.igallery.views.MainActivity
     * @see com.nexus.igallery.views.GalleryMapActivity
     * @since iGallery version 1.0
     */
    public List<PhotoData> getAllPhotoDataToDisplay() {
        allPhotoDataToDisplay = mRepository.getPhotoAllData();
        if (allPhotoDataToDisplay == null) {
            allPhotoDataToDisplay = new ArrayList<PhotoData>();
        }
        return allPhotoDataToDisplay;
    }


    /**
     * getter for specific photo data
     * @param photoData a PhotoData type params store the constraints
     * @return a list which store PhotoData instance
     * @see com.nexus.igallery.views.MainActivity
     * @since iGallery version 1.0
     */
    public List<PhotoData> searchPhotoDataToDisplay(PhotoData photoData) {
        searchDataToDisplay = mRepository.getPhotoBySearch(photoData);

        return searchDataToDisplay;
    }


    /**
     * support for insert a new photo data
     * @param photoData the photo data which will be inserted
     * @see com.nexus.igallery.views.MainActivity
     * @see com.nexus.igallery.common.CommonMethod
     * @since iGallery version 1.0
     */
    public void storePhoto(PhotoData photoData) {
        mRepository.storePhoto(photoData);
    }

    /**
     * support for update the specific photo data
     * @param photoData the photo data which will be updated
     * @see com.nexus.igallery.views.EditActivity
     * @since iGallery version 1.0
     */
    public void updatePhoto(PhotoData photoData) {
        mRepository.updatePhoto(photoData);
    }
}
