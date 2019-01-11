package com.nexus.igallery.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.nexus.igallery.R;

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
