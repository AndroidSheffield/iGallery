package com.nexus.igallery.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyDAO {
    @Insert
    void insertAll(PhotoData... photoData);

    @Insert
    void insert(PhotoData photoData);

    @Delete
    void delete(PhotoData photoData);

    // it selects a random element
    @Query("SELECT * FROM PhotoData ORDER BY RANDOM() LIMIT 1")
    LiveData<PhotoData> retrieveOnePhoto();

    @Query("SELECT * FROM PhotoData")
    List<PhotoData> retrieveAllPhoto();

    @Delete
    void deleteAll(PhotoData... photoData);

    @Query("SELECT COUNT(*) FROM PhotoData")
    int howManyElements();
}
