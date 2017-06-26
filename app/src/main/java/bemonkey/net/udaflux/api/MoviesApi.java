package bemonkey.net.udaflux.api;

import bemonkey.net.udaflux.BuildConfig;
import bemonkey.net.udaflux.models.movies.MovieModel;
import bemonkey.net.udaflux.models.reviews.ReviewModel;
import bemonkey.net.udaflux.models.trailers.TrailerModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bmnk on 3/14/17.
 */

public class MoviesApi {
    private static MoviesService moviesService = null;
    /**
     * Service builder
     */
    public static MoviesService get() {
        if (moviesService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            moviesService = retrofit.create(MoviesService.class);
        }
        return moviesService;

    }

    /**
     * In charge of making all the API call
     */
    public interface MoviesService {

        @GET("popular?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN)
        Call<MovieModel> getPopular(
                @Query("language=") String language,
                @Query("language=") int page
        );

        @GET("top_rated?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN)
        Call<MovieModel> getTopRated(
                @Query("language=") String language,
                @Query("language=") int page
        );

        @GET("{movie_id}/videos?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN)
        Call<TrailerModel> getMovieTrailers(@Path("movie_id") String movie_id);

        @GET("{movie_id}/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN)
        Call<ReviewModel> getMovieReviews(@Path("movie_id") String movie_id);
    }
}
