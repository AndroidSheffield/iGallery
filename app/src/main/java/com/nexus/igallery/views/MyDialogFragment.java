package com.nexus.igallery.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.nexus.igallery.R;

/**
 * MyDialogFragment extends DialogFragment which allow developer custom the dialog
 * and don't need to create a new class for new dialog window
 * @see MainActivity
 * @since iGallery version 1.0
 */
public class MyDialogFragment extends DialogFragment {

    private int message;
    private MDFListener mdfListener;

    /**
     * an interface allow data transfer between dialog fragment and activity
     * @see MainActivity
     * @since iGallery version 1.0
     */
    public interface MDFListener {
        void getDataFromDialog(int message);
    }

    /**
     * when the dialog on attach
     * @param context context of current activity
     * @since iGallery version 1.0
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mdfListener = (MDFListener) context;
    }

    /**
     * set the custom dialog content
     * @param savedInstanceState application current state
     * @return a custom dialog
     * @since iGallery version 1.0
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((String) getArguments().get("title"));
        builder.setMessage((String) getArguments().get("content"))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // different dialog have different type so it should be check
                        // and do the various reaction according to its typw
                        if (((String) getArguments().get("type")).equals("1")) {
                            message = 1;
                        }
                        if (((String) getArguments().get("type")).equals("2")) {
                            message = 2;
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

    /**
     * specific method to create a custom dialog and receive the data
     * @param title title of dialog
     * @param content content of dialog
     * @param type type of dialog
     * @return a MyDialogFragment instance
     * @see MainActivity
     * @since iGallery version 1.0
     */
    public MyDialogFragment newInstance(String title, String content, String type) {
        MyDialogFragment frag = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }


    /**
     * the method will be called when dialog close
     * @since iGallery version 1.0
     */
    @Override
    public void onDestroy() {
        if (mdfListener != null) {
            mdfListener.getDataFromDialog(message);
        }
        super.onDestroy();
    }
}
