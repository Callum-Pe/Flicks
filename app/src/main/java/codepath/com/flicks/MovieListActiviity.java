package codepath.com.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.flicks.models.Config;
import codepath.com.flicks.models.Movie;
import cz.msebera.android.httpclient.Header;


public class MovieListActiviity extends AppCompatActivity {
    public static final String API_BASE_URL = "https://api.themoviedb.org/3";
    public static final String API_KEY_PARAM = "api_key";
    public static final String TAG = "MovieListActivity";
    @BindString(R.string.api_key)    String API_KEY;
    AsyncHttpClient client;



    ArrayList<Movie> movieList;

    @BindView(R.id.rvMovies) RecyclerView rvMovie;
    MovieAdapter adapter;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list_activiity);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();
        movieList = new ArrayList<Movie>();
        adapter = new MovieAdapter(movieList);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setAdapter(adapter);
        getConfiguration();

    }
    public void getNowPlaying()
    {
        String url = API_BASE_URL + "/movie/now_playing";

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM,API_KEY);
        client.get(url,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        Movie movie = new Movie(array.getJSONObject(i));
                        movieList.add(movie);
                        adapter.notifyItemInserted(movieList.size()-1);
                    }
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
    private void getConfiguration()
    {
        RequestParams params = new RequestParams();
        String url = API_BASE_URL + "/configuration";

        params.put(API_KEY_PARAM, API_KEY);
        client.get(url,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    config = new Config(response);
                    Log.i(TAG,String.format("Loaded configuration with imagebase %s and postersize %s"
                            ,config.getImageBaseUrl(), config.getPosterSize()));
                    adapter.setConfig(config);
                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed to parse config",e,false);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("failed getting configuration", throwable, false);
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
