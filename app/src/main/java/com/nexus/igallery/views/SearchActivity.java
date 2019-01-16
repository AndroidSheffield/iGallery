package com.nexus.igallery.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.nexus.igallery.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A searching interface for iGallery app
 * which allow user search the photo data base on its create date, title and description
 * @author Jingbo Lin
 * @see MainActivity
 * @since iGallery version 1.0
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchStartDate, searchEndDate, searchTitle, searchDescription;
    private TextInputLayout searchLayoutStartDate, searchLayoutEndDate, searchLayoutTitle, searchLayoutDescription;
    private Button search;
    private Activity activity;
    final Calendar searchStartCalendar = Calendar.getInstance();
    final Calendar searchEndCalendar = Calendar.getInstance();

    /**
     * the method will be called when clicking the search icon at the menu
     * @param savedInstanceState application current state
     * @since iGallery version 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        searchLayoutStartDate = findViewById(R.id.search_layout_startDate);
        searchLayoutEndDate = findViewById(R.id.search_layout_endDate);
        searchLayoutTitle = findViewById(R.id.search_layout_title);
        searchLayoutDescription = findViewById(R.id.search_layout_description);
        searchStartDate = findViewById(R.id.search_startDate);
        searchEndDate = findViewById(R.id.search_endDate);
        searchTitle = findViewById(R.id.search_title);
        searchDescription = findViewById(R.id.search_description);

        search = findViewById(R.id.btn_search);

        // define start date DatePicker
        final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                searchStartCalendar.set(Calendar.YEAR, year);
                searchStartCalendar.set(Calendar.MONTH, month);
                searchStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }
        };

        // start date listener when clicking it will call the startDate DatePickerDialog
        searchStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchActivity.this, startDate,
                        searchStartCalendar.get(Calendar.YEAR),
                        searchStartCalendar.get(Calendar.MONTH),
                        searchStartCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // define end date DatePicker
        final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                searchEndCalendar.set(Calendar.YEAR, year);
                searchEndCalendar.set(Calendar.MONTH, month);
                searchEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();
            }
        };

        // end date listener when clicking it will call the endDate DatePickerDialog
        searchEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchActivity.this, endDate,
                        searchEndCalendar.get(Calendar.YEAR),
                        searchEndCalendar.get(Calendar.MONTH),
                        searchEndCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // search button listener when clicking will send the intent data to MainActivity
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent();
                mainIntent.putExtra("search_startDate", searchStartDate.getText());
                mainIntent.putExtra("search_endDate", searchEndDate.getText());
                mainIntent.putExtra("title", searchTitle.getText());
                mainIntent.putExtra("description", searchDescription.getText());
                setResult(RESULT_OK, mainIntent);
                finish();

            }
        });

    }

    /**
     * update the input area of start date
     * @since iGallery version 1.0
     */
    private void updateStartLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        searchStartDate.setText(sdf.format(searchStartCalendar.getTime()));
    }

    /**
     * update the input area of end date
     * @since iGallery version 1.0
     */
    private void updateEndLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        searchEndDate.setText(sdf.format(searchEndCalendar.getTime()));
    }


    /**
     * set an icon before title at toolbar which allow going back to the previous page
     * @param item menu elements
     * @return true
     * @since iGallery version 1.0
     */
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
