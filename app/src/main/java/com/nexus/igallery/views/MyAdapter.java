package com.nexus.igallery.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexus.igallery.R;
import com.nexus.igallery.models.PhotoData;

import java.util.List;

/**
 * Code was modified base on Week 4 code
 * Display the content of coordinated layout as the recyclerView
 * This class extends RecyclerView.Adapter<MyAdapter.View_Holder> allow showing photo as recycle view
 * @see EditActivity
 * @see GalleryMapActivity
 * @see MainActivity
 * @see ShowImageActivity
 * @since iGallery version 1.0
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.View_Holder> {
    static private Context context;
    private static List<PhotoData> items;

    /**
     * constructor
     * @param items a list of PhotoData instance
     * @see MainActivity
     * @since iGallery version 1.0
     */
    public MyAdapter(List<PhotoData> items) {
        this.items = items;
    }

    /**
     * constructor
     * @param cont context of current Activity
     * @param items a list of PhotoData instance
     * @since iGallery version 1.0
     */
    public MyAdapter(Context cont, List<PhotoData> items) {
        super();
        this.items = items;
        context = cont;
    }



    /**
     * initiate a view holder
     * @param parent
     * @param viewType
     * @return a view holder which just be created
     * @since iGallery version 1.0
     */
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context= parent.getContext();
        return holder;
    }

    /**
     * set the style of each item
     * @param holder current view holder
     * @param position the index of photo in items list
     * @since iGallery version 1.0
     */
    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && items.get(position)!=null) {

            Bitmap myBitmap = BitmapFactory.decodeFile(items.get(position).getPhotoPath());
            holder.imageView.setImageBitmap(myBitmap);

            if (!items.get(position).getTitle().equals("")) {
                holder.imageTitle.setText(items.get(position).getTitle());
            }
            if (!items.get(position).getDescription().equals("")) {
                holder.imageDescription.setText(items.get(position).getDescription());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }

    }


    /**
     * get the specific data from items list
     * @param id the index of data
     * @return the specific data which is a PhotoData instance
     * @since iGallery version 1.0
     */
    PhotoData getItem(int id) {
        return items.get(id);
    }

    /**
     * get the size of items
     * @return an int value represent items list's size
     * @since iGallery version 1.0
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Inner class which extends RecyclerView.ViewHolder
     * and set the content of item
     * @since iGallery version 1.0
     */
    public class View_Holder extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView imageTitle;
        TextView imageDescription;

        View_Holder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_item);
            imageTitle = itemView.findViewById(R.id.item_title);
            imageDescription = itemView.findViewById(R.id.item_description);

        }


    }

    /**
     * getter of items
     * @return a list of PhotoData intance
     * @see EditActivity
     * @see ShowImageActivity
     * @see GalleryMapActivity
     * @since iGallery version 1.0
     */
    public static List<PhotoData> getItems() {
        return items;
    }

    /**
     * change the specific items after updating
     * @param index the index of photo data in items list
     * @param photoData a PhotoData intance which will replace the old one
     * @see ShowImageActivity
     * @since iGallery version 1.0
     */
    public static void changeItem(int index, PhotoData photoData) {
        items.set(index, photoData);
    }


    /**
     * setter or items
     * @param items
     * @since iGallery version 1.0
     */
    public static void setItems(List<PhotoData> items) {
        MyAdapter.items = items;
    }

    public static void deleteItem(int deletePosition) {
        items.remove(deletePosition);
    }
}
