package com.nexus.igallery.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity()
public class PhotoData {
    @PrimaryKey(autoGenerate = true)
    @android.support.annotation.NonNull
    private int id=0;

    private String photoPath = null;
    private double lat = 0.0;
    private double lon = 0.0;
    private String title = "";
    private String description = "";

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "create_date")
    private Date createDate = null;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "update_date")
    private Date updateDate = null;




    public PhotoData(String photoPath, double lat, double lon, Date createDate, Date updateDate) {
//        this.name = name;
        this.photoPath = photoPath;
        this.lat = lat;
        this.lon = lon;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    @android.support.annotation.NonNull
    public int getId() {
        return id;
    }
    public void setId(@android.support.annotation.NonNull int id) {
        this.id = id;
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

