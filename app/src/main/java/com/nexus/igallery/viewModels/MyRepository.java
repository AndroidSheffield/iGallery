package com.nexus.igallery.viewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import com.nexus.igallery.database.MyDAO;
import com.nexus.igallery.database.MyRoomDatabase;
import com.nexus.igallery.models.PhotoData;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The class extends ViewModel and aim to call the MyDAO method and insert data into database
 * or retrieve data from it
 * @author Jingbo Lin
 * @see MyViewModel
 * @since iGallery version 1.0
 */
public class MyRepository extends ViewModel {
    private final MyDAO mDBDao;

    /**
     * constructor to initiate the class instance
     * @param application
     */
    public MyRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @return a LiveData with a random PhotoData retrieved from database
     * @see MyViewModel
     * @since iGallery version 1.0
     */
    public LiveData<PhotoData> getPhotoData() {
        return mDBDao.retrieveOnePhoto();
    }

    /**
     * it gets the data when changed in the db and returns it to the ViewModel
     * @see MyViewModel
     * @return a list which store PhotoData instance
     * @since iGallery version 1.0
     */
    public List<PhotoData> getPhotoAllData() {
        List<PhotoData> list = null;
        try {
            list = new retrieveAllAsyncTask(mDBDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *  the method use the async process(retrieveAsyncTask) to access database and retrieve photo data
     *  base on the constraints which search interface provided
     * @param params a PhotoData type params store the constraints
     * @return a list which store PhotoData instance
     * @see MyViewModel
     * @since iGallery version 1.0
     */
    public List<PhotoData> getPhotoBySearch(PhotoData params) {
        List<PhotoData> list = null;
        try {
            list = new retrieveAsyncTask(mDBDao).execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * the method use the async process(updateAsyncTask) to access database
     * and update the specific photo data at PhotoData table
     * @param photoData the photo data which will be updated
     * @see MyViewModel
     * @since iGallery version 1.0
     */
    public void updatePhoto(PhotoData photoData) {
        new updateAsyncTask(mDBDao).execute(photoData);
    }

    /**
     * the method use the async process(insertAsyncTask) to access database
     * and insert a new photo data into PhotoData table
     * @param photoData the photo data which will be inserted
     * @see MyViewModel
     * @since iGallery version 1.0
     */
    public void storePhoto(PhotoData photoData) {
        new insertAsyncTask(mDBDao).execute(photoData);
    }

    /**
     * the method use the async process(deleteAsyncTask) to access database
     * and delete a new photo data into PhotoData table
     * @param photoData the photo data which will be deleted
     * @see MyViewModel
     * @since iGallery version 1.0
     */
    public void deletePhoto(PhotoData photoData) {
        new deleteAsyncTask(mDBDao).execute(photoData);
    }

    /**
     * The inner class provide an async process for getPhotoBySearch(PhotoData params) to retrieve data by constraints
     * the input parameter should be PhotoData instance and the return should be List<PhotoData>
     * @author Jingbo Lin
     * @since iGallery version 1.0
     */
    private static class retrieveAsyncTask extends AsyncTask<PhotoData, Void, List<PhotoData>> {
        private MyDAO mAsyncTaskDao;

        retrieveAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * the method allow the query to execute
         * @param params constraints from search interface
         * @return a list which store PhotoData instance
         * @since iGallery version 1.0
         */
        protected List<PhotoData> doInBackground(PhotoData... params) {

            List<PhotoData> list;
            if (params[0].getCreateDate() != null) {

                list = mAsyncTaskDao.retrievePhotoWithDate(params[0].getCreateDate(), params[0].getUpdateDate(), params[0].getTitle(), params[0].getDescription());
            }
            else {
                list = mAsyncTaskDao.retrievePhotoWithoutDate(params[0].getTitle(), params[0].getDescription());
            }

            Log.i("MyRepository", "get data" + list.toString());

            return list;

        }
    }


    /**
     * The inner class provide an async process for getPhotoAllData() to retrieve all data from database
     * don't need input parameter and the return should be List<PhotoData>
     * @author Jingbo Lin
     * @since iGallery version 1.0
     */
    private static class retrieveAllAsyncTask extends AsyncTask<Void, Void, List<PhotoData>> {
        private MyDAO mAsyncTaskDao;

        retrieveAllAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * the method allow the query to execute
         * @param voids null
         * @return a list which store PhotoData instance
         * @since iGallery version 1.0
         */
        protected List<PhotoData> doInBackground(Void... voids) {
            List<PhotoData> list = mAsyncTaskDao.retrieveAllPhoto();
            Log.i("MyRepository", "get All data");

            return list;
        }
    }

    /**
     * The inner class provide an async process for storePhoto(PhotoData photoData) to insert photo data into database
     * the input parameter should be PhotoData instance and don't need return
     * @author Jingbo Lin
     * @since iGallery version 1.0
     */
    private static class insertAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private LiveData<PhotoData> photoData;

        insertAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * the method allow the query to execute
         * @param params the photo data which will be inserted
         * @return null
         * @since iGallery version 1.0
         */
        @Override
        protected Void doInBackground(final PhotoData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", "photo used: "+params[0].getPhotoPath()+"");

            return null;
        }
    }

    /**
     * The inner class provide an async process for deletePhoto(PhotoData photoData) to delete photo data from database
     * the input parameter should be PhotoData instance and don't need return
     * @author Jingbo Lin
     * @since iGallery version 1.0
     */
    private static class deleteAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private MyDAO mAsyncTaskDao;

        deleteAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }
        /**
         * the method allow the query to execute
         * @param params the photo data which will be inserted
         * @return null
         * @since iGallery version 1.0
         */
        @Override
        protected Void doInBackground(PhotoData... params) {
            mAsyncTaskDao.delete(params[0]);


            return null;
        }
    }


    /**
     * The inner class provide an async process for updatePhoto(PhotoData photoData) to update specific data of database
     * the input parameter should be PhotoData instance and don't need return
     * @author Jingbo Lin
     * @since iGallery version 1.0
     */
    private static class updateAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private LiveData<PhotoData> photoData;

        updateAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }

        /**
         * the method allow the query to execute
         * @param params the photo data which will be updated
         * @return null
         * @since iGallery version 1.0
         */
        @Override
        protected Void doInBackground(final PhotoData... params) {
            mAsyncTaskDao.update(params[0]);
            Log.i("MyRepository", "photo updated: "+ params[0].getPhotoPath() + "");

            return null;
        }
    }
}
