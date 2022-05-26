package com.example.forager;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PopulateHomePage extends AppCompatActivity {

    void fillHomepage(String searchQuery, ImageView homeImageView, TextView homeTextView, Context context) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" +
                searchQuery +
                "&langRestrict=en" +
                "&printType=books" +
                "&maxResults=1";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("INFO", "FAILED!!!");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = Objects.requireNonNull(response.body()).string();

                    try {
                        JSONObject queryObject = new JSONObject(myResponse);
                        JSONArray queryArray = queryObject.getJSONArray("items");

                        JSONObject jsonVolumeInfo = queryArray.getJSONObject(0).getJSONObject("volumeInfo");

                        String title = jsonVolumeInfo.getString("title");
                        String description = jsonVolumeInfo.getString("description");
                        String smallThumbnail = jsonVolumeInfo.getJSONObject("imageLinks").get("smallThumbnail").toString();

                        int numOfAuthors = jsonVolumeInfo.getJSONArray("authors").length();
                        String[] authors = new String[numOfAuthors];

                        for (int i = 0; i < numOfAuthors; i++) {
                            authors[i] = jsonVolumeInfo.getJSONArray("authors").get(i).toString();
                        }

                        Book newBook = new Book(title, authors, numOfAuthors, description, smallThumbnail);

                        String results = "\nTitle: " + newBook.getTitle() + "\n\n" +
                                "\n\nAuthor(s): " + newBook.getAuthors() + "\n\n" +
                                newBook.getDescription();

                        String imageUrl = newBook.getSmallThumbnailLink();
                        homeTextView.setText(results);
                        Log.i("INFO", "IN GLIDE.");

                        Glide.with(context)
                                .asBitmap()
                                .load(imageUrl)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.no_image_found)
                                .override(300, 300)
                                .into(homeImageView);

                    } catch (JSONException e) {
                        Log.i("INFO", "JSON FAILED.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
