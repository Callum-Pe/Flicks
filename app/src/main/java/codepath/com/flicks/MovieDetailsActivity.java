package codepath.com.flicks;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.flicks.models.Config;
import codepath.com.flicks.models.Movie;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static codepath.com.flicks.MovieListActiviity.API_BASE_URL;
import static codepath.com.flicks.MovieListActiviity.API_KEY_PARAM;


public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;
    AsyncHttpClient client;
    public static final String TAG = "MovieListActivity";

    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview)TextView tvOverview;
    @BindView(R.id.rbVoteAverage)RatingBar rbVoteAverage;

    @BindView(R.id.ivBackdropImage) ImageView imageView;
    @BindString(R.string.api_key)    String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();

        movie =  Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        Config config = Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


        boolean portrait = this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT;
        String imageUrl = config.getImageUrl(config.getBackdropSize(),movie.getBackdropPath());
        int placeholderID = R.drawable.backdrop;

        GlideApp.with(this)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(25, 0))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(imageView);
    }
    public void onClick(View view)
    {
        String url = API_BASE_URL + String.format("/movie/%s/videos",movie.getId());

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM,API_KEY);
        client.get(url,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray array = response.getJSONArray("results");
                    JSONObject o = array.getJSONObject(0);
                    String videoid = null;
                    videoid = o.getString("key");
                    if(videoid != null)
                    {
                        Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                        intent.putExtra("id",videoid);

                        startActivity(intent);
                    }
                    Log.i(TAG,"here's the key :" + videoid);
                    Log.i(TAG, String.format("Loaded %s movies",array.length()));
                } catch (JSONException e) {
                    logError("failed to parse now playing movies", e, false);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("failed getting from now playing", throwable, false);
            }


        });
    }
    private void logError(String message,Throwable error, boolean silent)
    {
        Log.e(TAG,message,error);
        if(!silent)
        {
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

        }
    }
}