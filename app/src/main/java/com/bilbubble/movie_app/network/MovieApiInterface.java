package com.bilbubble.movie_app.network;

import com.bilbubble.movie_app.models.CastResponse;
import com.bilbubble.movie_app.models.MoviesResponse;
import com.bilbubble.movie_app.models.movie.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {
    @GET("now_playing")
    Call<MoviesResponse> getNowPlaying(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );

    @GET("{movie_id}/credits")
    Call<CastResponse> getCast(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );

    @GET("upcoming")
    Call<MoviesResponse> getUpcoming(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("popular")
    Call<MoviesResponse> getPopular(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
}
