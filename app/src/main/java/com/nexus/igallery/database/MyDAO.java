package com.nexus.igallery.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nexus.igallery.models.PhotoData;

import java.util.Date;
import java.util.List;

/**
 * DAO class provide the method for Room to operate the database
 * @see MyRoomDatabase
 * @see com.nexus.igallery.viewModels.MyRepository
 * @since iGallery version 1.0
 */
@Dao
public interface MyDAO {
    /**
     * insert a PhotoData list to PhotoData table
     * @param photoData an element of PhotoData list
     * @since iGallery version 1.0
     */
    @Insert
    void insertAll(PhotoData... photoData);

    /**
     * insert a photo data into PhotoData table
     * @param photoData the photo data which will be inserted
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    @Insert
    void insert(PhotoData photoData);

    /**
     * delete the specific photo data from PhotoData table
     * @param photoData the photo data which will be deleted
     * @since iGallery version 1.0
     */
    @Delete
    void delete(PhotoData photoData);

    /**
     * update the specific photo data of PhotoData table
     * @param photoData the photo data which will be updated
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    @Update
    void update(PhotoData photoData);

    /**
     * the method selects a random element
     * @return a LiveData with a random PhotoData retrieved from database
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    @Query("SELECT * FROM PhotoData ORDER BY RANDOM() LIMIT 1")
    LiveData<PhotoData> retrieveOnePhoto();

    /**
     * the method retrieve all photo from database
     * @return a list which store PhotoData instance
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    @Query("SELECT * FROM PhotoData")
    List<PhotoData> retrieveAllPhoto();

    /**
     * the method used for retrieve photo data by given parameters (contain date)
     * @param startDate the start date selected at the search interface
     * @param upateDate the end date selected at the search interface
     * @param title the title which is typed down at the search interface
     * @param description the description which is typed down at the search interface
     * @return a list which store PhotoData instance
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    @Query("SELECT * FROM PhotoData WHERE create_date BETWEEN :startDate AND :upateDate AND title LIKE :title AND description LIKE :description")
    List<PhotoData> retrievePhotoWithDate(Date startDate, Date upateDate, String title, String description);

    /**
     * the method used for retrieve photo data by given parameters (contain date)
     * @param title the title which is typed down at the search interface
     * @param description the description which is typed down at the search interface
     * @return a list which store PhotoData instance
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    @Query("SELECT * FROM PhotoData WHERE title LIKE :title AND description LIKE :description")
    List<PhotoData> retrievePhotoWithoutDate(String title, String description);

    /**
     * delete a list of photo data from PhotoData table
     * @param photoData an element of PhotoData list
     * @since iGallery version 1.0
     */
    @Delete
    void deleteAll(PhotoData... photoData);

    /**
     * count the current number of photo data
     * @return a Integer represent the number of photo data
     * @since iGallery version 1.0
     */
    @Query("SELECT COUNT(*) FROM PhotoData")
    int howManyElements();
}
