package com.nexus.igallery.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nexus.igallery.R;
import com.nexus.igallery.models.PhotoData;
import com.nexus.igallery.viewModels.MyViewModel;

import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private MyViewModel myViewModel;
    private PhotoData element;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**
         * to add the actionbar and view model
         */


        setContentView(R.layout.activity_edit);


        Bundle b = getIntent().getExtras();
        int position=-1;
        if(b !=null) {
            position = b.getInt("position");
            if (position != -1) {
                element = MyAdapter.getItems().get(position);
                Button btn = findViewById(R.id.btn_save);
                final int finalPosition = position;
                btn.setOnClickListener(new View.OnClickListener() {
                    /**
                     * get a monitor for the button
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        /**
                         * title and description
                         * add a find view by id to defining the text view
                         */
                        String title= ((EditText) findViewById(R.id.edit_title)).getText().toString();
                        String description = ((EditText) findViewById(R.id.edit_description)).getText().toString();

                        element.setTitle(title);
                        element.setDescription(description);
                        element.setUpdateDate(new Date());
                        myViewModel.updatePhoto(element);
                        /**
                         * using the update method by database system
                         */
                        Intent intent = new Intent();
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        intent.putExtra("new_date", element.getUpdateDate());
                        intent.putExtra("position", finalPosition);
                        /**
                         * using the "putExtra" method to update and edit the data of picture
                         */
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
