package com.nexus.igallery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import com.nexus.igallery.database.PhotoData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.nexus.igallery.MainActivity.REQUEST_READ_EXTERNAL_STORAGE;

public class MyDialogFragment extends DialogFragment {

    private int message;
    private MDFListener mdfListener;

    public interface MDFListener {
        void getDataFromDialog(int message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mdfListener = (MDFListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((String) getArguments().get("title"));
        builder.setMessage((String) getArguments().get("content"))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (((String) getArguments().get("type")).equals("1")) {
                            message = 1;

                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }


                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public MyDialogFragment newInstance(String title, String content, String type) {
        MyDialogFragment frag = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public void onDestroy() {
        if (mdfListener != null) {
            mdfListener.getDataFromDialog(message);
        }
        super.onDestroy();
    }
}
