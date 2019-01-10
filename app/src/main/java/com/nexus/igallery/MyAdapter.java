/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.nexus.igallery;

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

import com.nexus.igallery.database.PhotoData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.View_Holder> {
    static private Context context;
    private static List<PhotoData> items;

    public MyAdapter(List<PhotoData> items) {
        this.items = items;
    }

    public MyAdapter(Context cont, List<PhotoData> items) {
        super();
        this.items = items;
        context = cont;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context= parent.getContext();
        return holder;
    }

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


    // convenience method for getting data at click position
    PhotoData getItem(int id) {
        return items.get(id);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView imageTitle;
        TextView imageDescription;

        View_Holder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_item);
            imageTitle = itemView.findViewById(R.id.item_title);
            imageDescription = itemView.findViewById(R.id.item_description);

        }


    }

    public static List<PhotoData> getItems() {
        return items;
    }



    public static void setItems(List<PhotoData> items) {
        MyAdapter.items = items;
    }
}