package com.otonomis.marvelcomics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.otonomis.marvelcomics.pojo.ComicsModel;

public class DetailActivity extends AppCompatActivity {
    TextView mTitle, mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitle = (TextView) findViewById(R.id.detail_title);
        mDescription = (TextView) findViewById(R.id.detail_description);

        ComicsModel comicsModel = (ComicsModel) getIntent().getSerializableExtra("single_comic");
        mTitle.setText(comicsModel.getTitle());
        mDescription.setText(comicsModel.getDescription());

    }
}
