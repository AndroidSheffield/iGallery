package com.nexus.igallery;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nexus.igallery.database.PhotoData;

import java.io.File;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private MyViewModel myViewModel;
    private PhotoData element;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);


        setContentView(R.layout.activity_edit);


        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b !=null) {
            position = b.getInt("position");
            if (position != -1) {
//                ImageView imageView = (ImageView) findViewById(R.id.image);
                element = MyAdapter.getItems().get(position);
                final File file = new File(element.getPhotoPath());
                Button btn = (Button) findViewById(R.id.btn_save);
                btn.setOnClickListener(new View.OnClickListener() {  //为按钮添加单击监听事件
                    @Override

                    public void onClick(View v) {
                        //tittle
                        String titles= ((EditText) findViewById(R.id.edit_title)).getText().toString();
                        //description
                        String descriptions = ((EditText) findViewById(R.id.edit_description)).getText().toString();

                        element.setTitle(titles);
                        element.setDescription(descriptions);
                        element.setUpdateDate(new Date());
                        myViewModel.updatePhoto(element);
                        finish();

                    }
                });
            }
        }




    }
}
