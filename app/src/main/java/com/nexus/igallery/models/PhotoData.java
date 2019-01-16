package com.nexus.igallery.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import com.nexus.igallery.database.DateConverter;
import java.util.Date;

/**
 * The entity class which connect with RoomDatabase and also create a table named PhotoData
 * which has attribute id(int type, primary key and autoGenerate), photoPath(String type), lat(double type),
 * lon(double type), title(String type, default "title"), description(String type, default "description"),
 * createDate(Date type for java instance but will convert to Long type by DateConverter when store at database),
 * updateDate(Date type for java instance but will convert to Long type by DateConverter when store at database)
 * @author Jingbo Lin
 * @see com.nexus.igallery.views.EditActivity
 * @see com.nexus.igallery.views.GalleryMapActivity
 * @see com.nexus.igallery.views.MainActivity
 * @see com.nexus.igallery.views.MyAdapter
 * @see com.nexus.igallery.views.ShowImageActivity
 * @see com.nexus.igallery.viewModels.MyViewModel
 * @see com.nexus.igallery.viewModels.MyRepository
 * @since iGallery version 1.0
 */
@Entity()
public class PhotoData {
    @PrimaryKey(autoGenerate = true)
    @android.support.annotation.NonNull
    private int id=0;

    private String photoPath;
    private double lat;
    private double lon;
    private String title = "title";
    private String description = "description";

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "create_date")
    private Date createDate;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "update_date")
    private Date updateDate;


    /**
     * constructor which initiate PhotoData instance
     * @param photoPath absolute path of photo file
     * @param lat latitude of photo's location
     * @param lon longitude of photo's location
     * @param createDate date of photo being created
     * @param updateDate date of photo being updated
     * @see com.nexus.igallery.views.MainActivity
     * @see com.nexus.igallery.common.CommonMethod
     * @since iGallery version 1.0
     */
    public PhotoData(String photoPath, double lat, double lon, Date createDate, Date updateDate) {
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

