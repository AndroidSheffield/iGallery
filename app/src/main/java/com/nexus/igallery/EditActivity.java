package com.nexus.igallery;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setContentView(R.layout.activity_edit);


        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b !=null) {
            position = b.getInt("position");
            if (position != -1) {
                element = MyAdapter.getItems().get(position);
                Button btn = findViewById(R.id.btn_save);
                final int finalPosition = position;
                btn.setOnClickListener(new View.OnClickListener() {  //为按钮添加单击监听事件
                    @Override

                    public void onClick(View v) {
                        //tittle
                        String title= ((EditText) findViewById(R.id.edit_title)).getText().toString();
                        //description
                        String description = ((EditText) findViewById(R.id.edit_description)).getText().toString();

                        element.setTitle(title);
                        element.setDescription(description);
                        element.setUpdateDate(new Date());
                        myViewModel.updatePhoto(element);
                        Intent intent = new Intent();
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        intent.putExtra("new_date", element.getUpdateDate());
                        intent.putExtra("position", finalPosition);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
