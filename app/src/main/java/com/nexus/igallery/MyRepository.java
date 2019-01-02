package com.nexus.igallery;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import android.os.AsyncTask;
import android.util.Log;


import com.nexus.igallery.database.MyDAO;
import com.nexus.igallery.database.MyRoomDatabase;
import com.nexus.igallery.database.PhotoData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRepository extends ViewModel {
    private final MyDAO mDBDao;

    public MyRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @return
     */
    public LiveData<PhotoData> getPhotoData() {
        return mDBDao.retrieveOnePhoto();
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @return
     */
    public List<PhotoData> getPhotoAllData() {
        List<PhotoData> list = null;
        try {
            list = new retrieveAsyncTask(mDBDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void storePhoto(ImageElement imageElement) {

        new insertAsyncTask(mDBDao).execute(new PhotoData(String.valueOf(imageElement.file), imageElement.lat, imageElement.lon, imageElement.date));


    }



    private static class retrieveAsyncTask extends AsyncTask<Void, Void, List<PhotoData>> {
        private MyDAO mAsyncTaskDao;

        retrieveAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }
        protected List<PhotoData> doInBackground(Void... voids) {
            List<PhotoData> list = mAsyncTaskDao.retrieveAllPhoto();
            Log.i("MyRepository", "get All data");

            return list;
        }
    }

    private static class insertAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private LiveData<PhotoData> photoData;

        insertAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final PhotoData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "photo used: "+params[0].getPhotoPath()+"");

            return null;
        }
    }
}
