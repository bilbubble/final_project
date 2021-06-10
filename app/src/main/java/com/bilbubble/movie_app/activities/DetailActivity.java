package com.bilbubble.movie_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bilbubble.movie_app.adapters.CastAdapter;
import com.bilbubble.movie_app.db.AppDatabase;
import com.bilbubble.movie_app.db.entities.Favorite;
import com.bilbubble.movie_app.models.CastResponse;
import com.bilbubble.movie_app.models.Casts;
import com.bilbubble.movie_app.adapters.GenreRecyclerAdapter;
import com.bilbubble.movie_app.models.Genre;
import com.bilbubble.movie_app.models.movie.Movie;
import com.bilbubble.movie_app.network.Const;
import com.bilbubble.movie_app.network.MovieApiClient;
import com.bilbubble.movie_app.network.MovieApiInterface;
import com.bilbubble.movie_app.R;
import com.bilbubble.movie_app.utils.ActionBarTitle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity implements ActionBarTitle {
    LinearLayout linearLayoutTrailer;

    private ImageView posterDetail;
    private ImageView trailerImg;
    private TextView title;
    private TextView releaseYear;
    private TextView description;
    private TextView ratingNumber;

    private String id;
    private RecyclerView recyclerView, recyclerViewCast;
    private ArrayList<String> genres;

    private TextView loadingText;
    private ConstraintLayout constraintLayout;
    private String favoriteTitle, favoriteImgUrl = "";

    private AppDatabase database;

    private Button favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("");

        database = AppDatabase.getInstance(getApplicationContext());

        loadingText = findViewById(R.id.loading_tv);
        loadingText.setVisibility(View.VISIBLE);

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setVisibility(View.GONE);

        id = getIntent().getStringExtra("ID");

        genres = new ArrayList<>();

        linearLayoutTrailer = findViewById(R.id.ll_trailer_clickable);
        title = findViewById(R.id.tv_detail_title);
        releaseYear = findViewById(R.id.tv_detail_release_year);
        trailerImg = findViewById(R.id.iv_trailer_img);
        posterDetail = findViewById(R.id.iv_poster_detail);
        description = findViewById(R.id.tv_detail_description);
        ratingNumber = findViewById(R.id.tv_detail_rating);
        favButton = findViewById(R.id.btn_fav);


        loadData();
        linearLayoutTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = title.getText().toString();
                String replaced = name.replaceAll("\\s+", "+").toLowerCase();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+replaced                                                                                                                                                   ));
                startActivity(intent);
            }
        });

        favButton.setOnClickListener(v -> {
            int movieId = Integer.parseInt(id);
            boolean exists = database.favoriteDao().isExists(movieId);

            if(exists){
                Favorite favorite = database.favoriteDao().findById(movieId);
                database.favoriteDao().delete(favorite).subscribe(() -> {
                    favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border, 0, 0, 0);
                    Toast.makeText(DetailActivity.this, "Removed From favorite", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    Toast.makeText(DetailActivity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
                });

            } else{
                Favorite favorite = new Favorite(movieId,favoriteTitle, favoriteImgUrl);
                database.favoriteDao().addFavorite(favorite).subscribe(() -> {
                    favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite, 0, 0, 0);
                    Toast.makeText(DetailActivity.this, "Added to Favorite", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    Toast.makeText(DetailActivity.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void loadData() {
        MovieApiInterface movieApiInterface = MovieApiClient.getRetrofit().create(MovieApiInterface.class);
        Call<Movie> movieDetail = movieApiInterface.getMovie(id, Const.API_KEY);
        movieDetail.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.isSuccessful() && response.body() != null){
                    Call<CastResponse> casts = movieApiInterface.getCast(id, Const.API_KEY);
                    casts.enqueue(new Callback<CastResponse>() {
                        @Override
                        public void onResponse(Call<CastResponse> call, Response<CastResponse> responseCast) {
                            if(responseCast.isSuccessful() && responseCast.body() !=null){
                                loadingText.setVisibility(View.GONE);
                                constraintLayout.setVisibility(View.VISIBLE);

                                setActivityContent(response.body(), responseCast.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<CastResponse> call, Throwable t) {

                        }
                    });
                }
                else {
                    Toast.makeText(DetailActivity.this, "Error OnResponse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("DetailActivity", "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(DetailActivity.this, "Failed: "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //
    public void setActionBarTitle(String title){
        View view = getLayoutInflater().inflate(R.layout.action_bar,null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );

        TextView titleBar = view.findViewById(R.id.tv_ab_title);
        titleBar.setText(title);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setCustomView(view, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left);

    }


    private void setActivityContent(Movie movie, CastResponse castResponse){
        favoriteTitle = movie.getTitle();
        favoriteImgUrl = movie.getCover();

        Glide.with(DetailActivity.this)
                .load(Const.IMG_URL_ORI + movie.getBackdrop())
                .into(trailerImg);

        Glide.with(DetailActivity.this)
                .load(Const.IMG_URL_200 + movie.getCover())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                .into(posterDetail);
        String yearOfRelease = movie.getReleaseDate().split("-")[0];
        String durationTime = movie.getDuration() + "min";
        String subdetailPlaceholder = String.format("Release:  %s | Duration:  %s", yearOfRelease, durationTime);
        title.setText(movie.getTitle());
        releaseYear.setText(subdetailPlaceholder);
        description.setText(movie.getDescription());
        ratingNumber.setText(String.valueOf(movie.getRating()));

        setGenres(movie.getGenres());

        recyclerView = findViewById(R.id.rv_genre);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(new GenreRecyclerAdapter(genres, this));
        setActionBarTitle(movie.getTitle());

        recyclerViewCast = findViewById(R.id.rv_cast);
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewCast.setAdapter(new CastAdapter(castResponse.getCasts()));

    }

    private void setGenres(List<Genre> genresList){
        for(int i = 0; i< genresList.size(); i++){
            genres.add(genresList.get(i).getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.movie_detail_toolbar, menu);

        int movieId = Integer.parseInt(id);
        boolean exists = database.favoriteDao().isExists(movieId);

        if(!exists){
            favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border, 0, 0, 0);
        }else{
            favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite, 0, 0, 0);
        }
        return true;
    }
}