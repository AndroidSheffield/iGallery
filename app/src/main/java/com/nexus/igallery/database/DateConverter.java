package com.nexus.igallery.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * The DateConverter class aim to
 * convert Date type value to Long type value which can be store in database
 * @author Jingbo Lin
 * @see MyRoomDatabase
 * @see com.nexus.igallery.models.PhotoData
 * @since iGallery version 1.0
 */
public class DateConverter {

    /**
     * convert Long value to Date
     * @param value value retrieve from database
     * @return the Date type value of date
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * convert Date value to Long
     * @param date value provided by instance and would store into databse
     * @return the Longe type value of date
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
