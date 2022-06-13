package com.example.forager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.example.forager.EXTRA_TEXT";
    public static final String EXTRA_IMAGE = "com.example.forager.EXTRA_IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText search_query = findViewById(R.id.search_query);
        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(view -> {
            if (search_query.getText().length() > 0) {
                queryGoogleBooksApi(search_query.getText().toString());
                Snackbar.make(view, "Success", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(view, "Please Enter Book Title", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button getNewMysteryBookButton = findViewById(R.id.mysteryButton);
        getNewMysteryBookButton.setOnClickListener(view -> {
            try {
                getNewMysteryBook();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button getNewFantasyBookButton = findViewById(R.id.fantasyButton);
        getNewFantasyBookButton.setOnClickListener(view -> {
            try {
                getNewFantasyBook();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button getNewSciFiBookButton = findViewById(R.id.sci_fiButton);
        getNewSciFiBookButton.setOnClickListener(view -> {
            try {
                getNewScienceFictionBook();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button getNewRomanceBookButton = findViewById(R.id.romanceButton);
        getNewRomanceBookButton.setOnClickListener(view -> {
            try {
                getNewRomanceBook();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void getNewMysteryBook() {
        Log.i("INFO", "Running thread...");
        Thread getNewMysteryBookThread = new Thread() {
            public void run() {
                try {
                    runOnUiThread(() -> {
                        ImageView mysteryBookImageView = findViewById(R.id.mystery_image);
                        TextView mysteryBookTextView = findViewById(R.id.mystery_info_text1);
                        newBook("Mystery", mysteryBookImageView, mysteryBookTextView);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getNewMysteryBookThread.start();
    }

    private void getNewFantasyBook() {
        Thread getNewFantasyBookThread = new Thread() {
            public void run() {
                try {
                    runOnUiThread(() -> {
                        ImageView fantasyBookImageView = findViewById(R.id.fantasy_image);
                        TextView fantasyBookTextView = findViewById(R.id.fantasy_info_text1);
                        newBook("Fantasy", fantasyBookImageView, fantasyBookTextView);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getNewFantasyBookThread.start();
    }

    private void getNewScienceFictionBook() {
        Thread getNewScienceFictionBookThread = new Thread() {
            public void run() {
                try {
                    runOnUiThread(() -> {
                        ImageView scienceFictionBookImageView = findViewById(R.id.sci_fi_image);
                        TextView scienceFictionBookTextView = findViewById(R.id.sci_fi_info_text1);
                        newBook("Science Fiction", scienceFictionBookImageView, scienceFictionBookTextView);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getNewScienceFictionBookThread.start();
    }

    private void getNewRomanceBook() {
        Thread getNewRomanceBookThread = new Thread() {
            public void run() {
                try {
                    runOnUiThread(() -> {
                        ImageView romanceBookImageView = findViewById(R.id.romance_image);
                        TextView romanceBookTextView = findViewById(R.id.romance_info_text1);
                        newBook("Romance", romanceBookImageView, romanceBookTextView);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getNewRomanceBookThread.start();
    }

    private void newBook(String query, ImageView dashboardImageView, TextView dashboardTextView) {
        final int MIN = 0;
        final int MAX = 11;
        Random random = new Random();
        int indexOfBookToChoose = random.nextInt(MAX + MIN) + MIN;
        String url = "https://www.googleapis.com/books/v1/volumes?q=subject:" +
                query +
                "&langRestrict=en" +
                "&printType=books" +
                "&startIndex=" +
                indexOfBookToChoose +
                "&maxResults=10";

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
                                "Author(s): " + newBook.getAuthors() + "\n\n" +
                                newBook.getDescription();

                        String imageUrl = newBook.getSmallThumbnailLink();
                        dashboardTextView.setText(results);
                        Log.i("INFO", "IN GLIDE.");

                        try {

                            Glide.with(MainActivity.this)
                                    .asBitmap()
                                    .load(imageUrl)
                                    .error(R.drawable.no_image_found)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            dashboardImageView.setImageBitmap(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        } catch (Exception e) {
                            Log.i("INFO", "Enter trace.");
                            e.printStackTrace();
                            Log.i("INFO", "Exit trace.");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void queryGoogleBooksApi(String query) {

        String url = "https://www.googleapis.com/books/v1/volumes?q=" +
                query +
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

                        Book book = new Book(title, authors, numOfAuthors, description, smallThumbnail);
                        String results = "\nTitle: " + book.getTitle() + "\n\n" +
                                "Author(s): " + book.getAuthors() + "\n\n" +
                                book.getDescription();

                        openSearchResultsActivity(results, book.getSmallThumbnailLink());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    protected void openSearchResultsActivity(String results, String image_url) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(EXTRA_TEXT, results);
        intent.putExtra(EXTRA_IMAGE, image_url);
        startActivity(intent);
    }


}