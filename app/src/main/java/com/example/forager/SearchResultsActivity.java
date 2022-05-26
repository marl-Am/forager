package com.example.forager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();

        ImageView search_results_image = findViewById(R.id.search_results_image);
        String thumbnail_url = intent.getStringExtra(MainActivity.EXTRA_IMAGE);

        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        TextView search_results_text_view = findViewById(R.id.search_results_text);
        search_results_text_view.setText(text);

        Context context = this;
        Glide.with(context)
                .asBitmap()
                .load(thumbnail_url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image_found)
                .override(300, 300)
                .into(search_results_image);

    }

}