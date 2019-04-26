package com.nexus.igallery.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.nexus.igallery.models.PhotoData;

/**
 * The abstract class MyRoomDatabase extends the RoomDatabase class enable the use of RoomDatabase
 * and allow it to access the database.
 * The entity is PhotoData so that it create a table named PhotoData at the database.
 * Using DateConverter class to convert Date type value to Long type or from Long type to Data type.
 * @see com.nexus.igallery.viewModels.MyRepository
 * @since iGallery version 1.0
 */
@android.arch.persistence.room.Database(entities = {PhotoData.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract MyDAO myDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile MyRoomDatabase INSTANCE;

    /**
     * get a MyRoomDatabase instance and initiate it
     * @param context the context of current activity
     * @return an instance of MyRoomDatabase
     * @see com.nexus.igallery.viewModels.MyRepository
     * @since iGallery version 1.0
     */
    public static MyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = android.arch.persistence.room.Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class, "photo_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // do any init operation about any initialisation here
        }
    };
}
