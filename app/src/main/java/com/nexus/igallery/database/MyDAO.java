package com.nexus.igallery.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface MyDAO {
    @Insert
    void insertAll(PhotoData... photoData);

    @Insert
    void insert(PhotoData photoData);

    @Delete
    void delete(PhotoData photoData);

    @Update
    void update(PhotoData photoData);

    // it selects a random element
    @Query("SELECT * FROM PhotoData ORDER BY RANDOM() LIMIT 1")
    LiveData<PhotoData> retrieveOnePhoto();

    @Query("SELECT * FROM PhotoData")
    List<PhotoData> retrieveAllPhoto();

    @Query("SELECT * FROM PhotoData WHERE create_date BETWEEN :startDate AND :upateDate AND title LIKE :title AND description LIKE :description")
    List<PhotoData> retrievePhotoWithDate(Date startDate, Date upateDate, String title, String description);

    @Query("SELECT * FROM PhotoData WHERE title LIKE :title AND description LIKE :description")
    List<PhotoData> retrievePhotoWithoutDate(String title, String description);

    @Delete
    void deleteAll(PhotoData... photoData);

    @Query("SELECT COUNT(*) FROM PhotoData")
    int howManyElements();
}
